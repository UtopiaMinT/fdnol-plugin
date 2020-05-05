package me.lkp111138.plugin.npc;

import me.lkp111138.plugin.Main;
import me.lkp111138.plugin.item.CustomItem;
import me.lkp111138.plugin.quest.Quest;
import me.lkp111138.plugin.quest.QuestProgress;
import me.lkp111138.plugin.rpg.Stats;
import me.lkp111138.plugin.util.Util;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

public class NPC {
    private int id;
    private InteractionHandler handler;
    private Location location;
    private boolean eyeContact;
    private String type;
    private String name;
    private int startTrackingRange;
    private int trackingRange;

    private Entity villager;
    private Entity trackingEntity;

    private static List<NPC> activeNPCList = new ArrayList<>();
    private static Map<Integer, NPC> npcRegistry = new HashMap<>();
    private static int nextId;

    @SuppressWarnings("unchecked")
    public NPC(Map map) {
        this.id = (Integer) map.getOrDefault("id", nextId++);
        String worldName = (String) map.get("world");
        World world = getServer().getWorld(worldName);
        float x = (Integer) map.get("x") + 0.5f;
        int y = (Integer) map.get("y");
        float z = (Integer) map.get("z") + 0.5f;
        int yaw = (Integer) map.getOrDefault("yaw", 0);
        int pitch = (Integer) map.getOrDefault("pitch", 0);
        this.eyeContact = (Boolean) map.getOrDefault("eyeContact", true);
        this.type = (String) map.getOrDefault("type", "nitwit");
        this.name = (String) map.getOrDefault("name", "");
        this.startTrackingRange = (Integer) map.getOrDefault("start_tracking_range", 4);
        this.trackingRange = (Integer) map.getOrDefault("tracking_range", 8);
        this.location = new Location(world, x, y, z, yaw, pitch);
        String function = (String) map.get("function");
        switch (function) {
            case "quest":
                this.handler = ((event, npc) -> {
                    Quest quest = Quest.forNPC(npc.id);
                    if (quest == null) {
                        return;
                    }
                    Player clicker = event.getPlayer();
                    Stats stats = Stats.extractFromEntity(clicker);
                    if (stats == null) {
                        return;
                    }
                    QuestProgress progress = stats.getQuestProgress().get(quest.id);
                    Quest.QuestStage currentStage = null;
                    if (progress != null) {
                        if (progress.getStage() == -1) {
                            // todo check for repeatable quest and reset progress
                            if (quest.cooldown > 0) {
                                if (progress.getTimestamp() < System.currentTimeMillis() - quest.cooldown * 1000) {
                                    progress = null;
                                    currentStage = null;
                                    stats.getQuestProgress().remove(quest.id);
                                } else {
                                    Util.sendQuestDialog(clicker, quest.cooldownHint);
                                }
                            }
                        }
                        if (progress != null) {
                            currentStage = quest.stages.get(progress.getStage());
                        }
                    }
                    if (npc.id == quest.startNPC || (currentStage != null && npc.id == currentStage.npc)) {
                        // try to advance in quest
                        PlayerInventory inv = clicker.getInventory();
                        Map<String, Integer> remainingItems = new HashMap<>();
                        List<ItemStack> toBeRemoved = new ArrayList<>();
                        if (currentStage != null) {
                            for (CustomItem customItem : currentStage.reqItems.keySet()) {
                                remainingItems.put(customItem.getId(), currentStage.reqItems.get(customItem));
                            }
                        } else {
                            for (CustomItem customItem : quest.reqItems.keySet()) {
                                remainingItems.put(customItem.getId(), quest.reqItems.get(customItem));
                            }
                            // check other reqs also
                            if (stats.getLevel() < quest.reqLevel) {
                                Util.sendQuestDialog(clicker, quest.reqLevelHint);
                                return;
                            }
                            for (String reqQuest : quest.reqQuests) {
                                if (!stats.getQuestProgress().containsKey(reqQuest)) {
                                    Util.sendQuestDialog(clicker, quest.reqQuestsHint);

                                    return;
                                }
                            }
                        }
                        for (ItemStack slot : inv) {
                            net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(slot);
                            NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
                            String id = compound.getString("CustomItemId");
                            if (id != null && remainingItems.containsKey(id)) {
                                int remaining = remainingItems.get(id);
                                if (slot.getAmount() < remaining) {
                                    toBeRemoved.add(slot);
                                    remainingItems.put(id, remaining - slot.getAmount());
                                } else {
                                    ItemStack clone = slot.clone();
                                    clone.setAmount(remaining);
                                    toBeRemoved.add(clone);
                                    remainingItems.remove(id);
                                }
                            }
                            if (remainingItems.isEmpty()) {
                                break;
                            }
                        }
                        if (!remainingItems.isEmpty()) {
                            // not enough items
                            if (currentStage != null) {
                                Util.sendQuestDialog(clicker, currentStage.hint);
                            } else {
                                Util.sendQuestDialog(clicker, quest.reqItemsHint);
                            }
                        } else {
                            inv.removeItem(toBeRemoved.toArray(new ItemStack[0]));
                            if (currentStage != null) {
                                // enough items, can advance
                                Util.sendQuestDialog(clicker, currentStage.dialog);
                                if (currentStage.isFinal) {
                                    // completed quest, give rewards
                                    stats.addXP(quest.rewardXP);
                                    quest.rewardItems.forEach((key, val) -> inv.addItem(key.getItemStack(val)));
                                    progress.setStage(-1);
                                    clicker.sendMessage(Quest.finishedText.replaceAll("\\$\\{name}", quest.name));
                                    if (quest.rewardXP > 0) {
                                        clicker.sendMessage("\u00a77+" + quest.rewardXP + " XP");
                                    }
                                    for (CustomItem item : quest.rewardItems.keySet()) {
                                        clicker.sendMessage("\u00a77+" + quest.rewardItems.get(item) + " " + item.getName());
                                    }
                                } else {
                                    // completed stage
                                    progress.setStage(progress.getStage() + 1);
                                    clicker.sendMessage(Quest.advanceText);
                                }
                                currentStage.rewardItems.forEach((key, val) -> inv.addItem(key.getItemStack(val)));
                            } else {
                                // enough items, can start
                                Util.sendQuestDialog(clicker, quest.startDialog);
                                stats.getQuestProgress().put(quest.id, new QuestProgress(quest.id, 0, System.currentTimeMillis()));
                                clicker.sendMessage(Quest.startedText.replaceAll("\\$\\{name}", quest.name));
                            }
                        }
                    }
                });
                break;
            default:
                this.handler = (event, npc) -> event.getPlayer().sendMessage("\u00a7aYou clicked on NPC #" + npc.getId());
        }

        npcRegistry.put(this.id, this);
        if (nextId <= this.id) {
            nextId = this.id + 1;
        }
    }

    public static NPC get(int id) {
        return npcRegistry.get(id);
    }

    public static void save() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "npc.yml"));
        System.out.println(config.getKeys(false));
        config.set("npc", npcRegistry.values().stream().map(npc -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", npc.id);
            m.put("world", npc.location.getWorld().getName());
            m.put("x", npc.location.getBlockX());
            m.put("y", npc.location.getBlockY());
            m.put("z", npc.location.getBlockZ());
            m.put("yaw", (int) npc.location.getYaw());
            m.put("pitch", (int) npc.location.getPitch());
            m.put("name", npc.name);
            m.put("type", npc.type);
            m.put("eye_contact", npc.eyeContact);
            m.put("tracking_range", npc.trackingRange);
            m.put("start_tracking_range", npc.startTrackingRange);
            m.put("spawned", npc.villager != null);
            return m;
        }).collect(Collectors.toList()));
        Main.getInstance().saveConfig();
    }

    public static void removeAll() {
        npcRegistry.values().forEach(NPC::remove);
    }

    public boolean spawn() {
        if (villager != null) {
            return false;
        }
        villager = createEntity();
        if (villager == null) {
            throw new RuntimeException("Invalid NPC type");
        }
        villager.setInvulnerable(true);
        villager.setSilent(true);
        if (villager instanceof Villager) {
            ((Villager) villager).setAI(false);
            ((Villager) villager).setCollidable(false);
        }
        if (villager instanceof HumanEntity) {
            ((HumanEntity) villager).setAI(false);
            ((HumanEntity) villager).setCollidable(false);
        }
        villager.setMetadata("npc", new FixedMetadataValue(Main.getInstance(), this));
        if (name.length() > 0) {
            villager.setCustomName(name);
            villager.setCustomNameVisible(true);
        }
        activeNPCList.add(this);
        return true;
    }

    private Entity createEntity() {
        Entity entity = null;
        World world = location.getWorld();
        switch (type) {
            case "villager":
            case "nitwit":
                entity = world.spawnEntity(location, EntityType.VILLAGER);
                ((Villager) entity).setProfession(Villager.Profession.NITWIT);
                break;
            case "farmer":
                entity = world.spawnEntity(location, EntityType.VILLAGER);
                ((Villager) entity).setProfession(Villager.Profession.FARMER);
                break;
            case "butcher":
                entity = world.spawnEntity(location, EntityType.VILLAGER);
                ((Villager) entity).setProfession(Villager.Profession.BUTCHER);
                break;
            case "priest":
                entity = world.spawnEntity(location, EntityType.VILLAGER);
                ((Villager) entity).setProfession(Villager.Profession.PRIEST);
                break;
            case "blacksmith":
                entity = world.spawnEntity(location, EntityType.VILLAGER);
                ((Villager) entity).setProfession(Villager.Profession.BLACKSMITH);
                break;
            case "librarian":
                entity = world.spawnEntity(location, EntityType.VILLAGER);
                ((Villager) entity).setProfession(Villager.Profession.LIBRARIAN);
                break;
        }
        return entity;
    }

    public void remove() {
        if (villager != null) {
            villager.remove();
            activeNPCList.remove(this);
            villager = null;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (villager != null) {
            villager.setCustomName(name);
            villager.setCustomNameVisible(true);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHealth() {
        if (villager instanceof LivingEntity) {
            return ((LivingEntity) villager).getHealth();
        }
        return 0;
    }

    public double getMaxHealth() {
        if (villager instanceof LivingEntity) {
            return ((LivingEntity) villager).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }
        return 0;
    }

    InteractionHandler getHandler() {
        return handler;
    }

    public Entity getTrackingEntity() {
        return trackingEntity;
    }

    public boolean isEyeContact() {
        return eyeContact;
    }

    public void setEyeContact(boolean eyeContact) {
        this.eyeContact = eyeContact;
    }

    public int getStartTrackingRange() {
        return startTrackingRange;
    }

    public void setStartTrackingRange(int startTrackingRange) {
        this.startTrackingRange = startTrackingRange;
    }

    public int getTrackingRange() {
        return trackingRange;
    }

    public void setTrackingRange(int trackingRange) {
        this.trackingRange = trackingRange;
    }

    public void setTrackingEntity(Entity trackingEntity) {
        this.trackingEntity = trackingEntity;
    }

    public static List<NPC> getActiveNPCList() {
        return activeNPCList;
    }

    public Entity getVillager() {
        return villager;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        if (villager != null) {
            villager.teleport(location);
        }
    }
}
