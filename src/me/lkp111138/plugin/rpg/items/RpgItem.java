package me.lkp111138.plugin.rpg.items;

import me.lkp111138.plugin.Util;
import me.lkp111138.plugin.rpg.damage.ElementalDamageRange;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RpgItem {
    private static int positiveMax;
    private static int positiveMin;
    private static int negativeMax;
    private static int negativeMin;
    private static Map<String, String> tierPrefix = new HashMap<>();
    private static Map<String, RpgItem> registry = new HashMap<>();

    // requirements
    public final int reqPower;
    public final int reqDefense;
    public final int reqSpeed;
    public final int reqIntelligence;
    public final int reqLevel;

    // base
    public final int baseBonusPower;
    public final int baseBonusDefense;
    public final int baseBonusSpeed;
    public final int baseBonusIntelligence;
    public final int baseHealth;
    public final int baseEarthDefense;
    public final int baseFireDefense;
    public final int baseWindDefense;
    public final int baseWaterDefense;
    public final ElementalDamageRange baseDamage;

    // bonuses
    public final int bonusMeleePercent;
    public final int bonusMeleeNeutral;
    public final int bonusEarthDefense;
    public final int bonusFireDefense;
    public final int bonusWindDefense;
    public final int bonusWaterDefense;
    public final int bonusEarthDamage;
    public final int bonusFireDamage;
    public final int bonusWindDamage;
    public final int bonusWaterDamage;
    public final int bonusHealthRegen;
    public final int bonusHealth;
    public final int bonusWalkSpeed;

    public final String id;
    public final String name;
    public final String type;
    public final String texture;
    public final String color;
    public final String tier;
    public final List<String> lore;

    public static void init(ConfigurationSection section) {
        List<Integer> positiveRange = section.getIntegerList("positive_range");
        List<Integer> negativeRange = section.getIntegerList("negative_range");
        ConfigurationSection tierSection = section.getConfigurationSection("tiers");
        positiveMax = positiveRange.get(1);
        positiveMin = positiveRange.get(0);
        negativeMax = negativeRange.get(1);
        negativeMin = negativeRange.get(0);
        for (String key : tierSection.getKeys(false)) {
            tierPrefix.put(key, tierSection.getString(key));
        }
    }

    public RpgItem(String id, ConfigurationSection section) {
        this.reqPower = section.getInt("req.power");
        this.reqDefense = section.getInt("req.defense");
        this.reqSpeed = section.getInt("req.speed");
        this.reqIntelligence = section.getInt("req.intelligence");
        this.reqLevel = section.getInt("req.level");

        this.baseBonusPower = section.getInt("base.bonusPower");
        this.baseBonusDefense = section.getInt("base.bonusDefense");
        this.baseBonusSpeed = section.getInt("base.bonusSpeed");
        this.baseBonusIntelligence = section.getInt("base.bonusIntelligence");
        this.baseHealth = section.getInt("base.health");
        this.baseEarthDefense = section.getInt("base.earthDefense");
        this.baseFireDefense = section.getInt("base.fireDefense");
        this.baseWindDefense = section.getInt("base.windDefense");
        this.baseWaterDefense = section.getInt("base.waterDefense");
        this.baseDamage = ElementalDamageRange.fromConfig(section.getConfigurationSection("base.damage"));

        this.bonusMeleePercent = section.getInt("stats.meleePercent");
        this.bonusMeleeNeutral = section.getInt("stats.meleeNeutral");
        this.bonusEarthDefense = section.getInt("stats.earthDefense");
        this.bonusFireDefense = section.getInt("stats.fireDefense");
        this.bonusWindDefense = section.getInt("stats.windDefense");
        this.bonusWaterDefense = section.getInt("stats.waterDefense");
        this.bonusEarthDamage = section.getInt("stats.earthDamage");
        this.bonusFireDamage = section.getInt("stats.fireDamage");
        this.bonusWindDamage = section.getInt("stats.windDamage");
        this.bonusWaterDamage = section.getInt("stats.waterDamage");
        this.bonusHealthRegen = section.getInt("stats.healthRegen");
        this.bonusHealth = section.getInt("stats.health");
        this.bonusWalkSpeed = section.getInt("stats.walkSpeed");

        this.id = id;
        this.name = section.getString("name");
        this.type = section.getString("type");
        this.texture = section.getString("texture");
        this.color = section.getString("color");
        this.tier = section.getString("tier");
        String[] lore = section.getString("lore").split(" ");
        this.lore = new ArrayList<>();
        StringBuilder line = new StringBuilder("\u00a78").append(lore[0]);
        for (int i = 1; i < lore.length; i++) {
            String word = lore[i];
            if (line.length() + 1 + word.length() < 33) {
                line.append(" ").append(word);
            } else {
                this.lore.add(line.toString());
                line.setLength(2);
                line.append(word);
            }
        }
        this.lore.add(line.toString()); // last line
        registry.put(id, this);
    }

    public static RpgItem get(String id) {
        return registry.get(id);
    }

    public static String getItemId(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagCompound rpg = compound.getCompound("RPG");
        if (rpg != null) {
            return rpg.getString("ITEM_ID");
        }
        return null;
    }

    public static ItemStack fixItem(ItemStack itemStack) {
        String itemId = getItemId(itemStack);
        if (itemId != null) {
            if (registry.containsKey(itemId)) {
                return registry.get(itemId).fixStack(itemStack);
            }
        }
        return null;
    }

    public ItemStack fixStack(ItemStack itemStack) {
        // stage 2: show stats on item
        List<String> lore = new ArrayList<>();
        // base stats
        lore.add("\u00a7r\u00a74HP " + nToString(baseHealth));
        if (baseEarthDefense != 0) {
            lore.add("§r§2Earth defense §7" + nToString(baseEarthDefense));
        }
        if (baseFireDefense != 0) {
            lore.add("§r§cFire defense §7" + nToString(baseFireDefense));
        }
        if (baseWindDefense != 0) {
            lore.add("§r§7Wind defense §7" + nToString(baseWindDefense));
        }
        if (baseWaterDefense != 0) {
            lore.add("§r§bWater defense §7" + nToString(baseWaterDefense));
        }
        if (baseBonusPower != 0) {
            lore.add(nToString(baseBonusPower, true) + "\u00a77 Power");
        }
        if (baseBonusDefense != 0) {
            lore.add(nToString(baseBonusDefense, true) + "\u00a77 Defense");
        }
        if (baseBonusSpeed != 0) {
            lore.add(nToString(baseBonusSpeed, true) + "\u00a77 Speed");
        }
        if (baseBonusIntelligence != 0) {
            lore.add(nToString(baseBonusIntelligence, true) + "\u00a77 Intelligence");
        }
        if (baseDamage != null) {
            if (baseDamage.maxNeutral > 0) {
                lore.add("\u00a76Neutral\u00a7r Damage: " + baseDamage.minNeutral + "-" + baseDamage.maxNeutral);
            }
            if (baseDamage.maxEarth > 0) {
                lore.add("\u00a72Earth\u00a7r Damage: " + baseDamage.minEarth + "-" + baseDamage.maxEarth);
            }
            if (baseDamage.maxFire > 0) {
                lore.add("\u00a74Fire\u00a7r Damage: " + baseDamage.minFire + "-" + baseDamage.maxFire);
            }
            if (baseDamage.maxWind > 0) {
                lore.add("\u00a77Wind\u00a7r Damage: " + baseDamage.minWind + "-" + baseDamage.maxWind);
            }
            if (baseDamage.maxWater > 0) {
                lore.add("\u00a7bWater\u00a7r Damage: " + baseDamage.minWater + "-" + baseDamage.maxWater);
            }
        }

        if (reqLevel > 0) {
            lore.add("\u00a77Min Level: " + reqLevel);
        }
        if (reqPower > 0) {
            lore.add("\u00a77Min Power: " + reqPower);
        }
        if (reqDefense > 0) {
            lore.add("\u00a77Min Defense: " + reqDefense);
        }
        if (reqSpeed > 0) {
            lore.add("\u00a77Min Speed: " + reqSpeed);
        }
        if (reqIntelligence > 0) {
            lore.add("\u00a77Min Intelligence: " + reqIntelligence);
        }

        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagCompound rpg = compound.getCompound("RPG");
        if (bonusMeleePercent > 0) {
            lore.add(nToString((int) (bonusMeleePercent * rpg.getInt("MeleePercent") / 100.0), true) + "% \u00a77Melee Damage");
        }
        if (bonusMeleeNeutral > 0) {
            lore.add(nToString((int) (bonusMeleeNeutral * rpg.getInt("MeleeNeutral") / 100.0), true) + " \u00a77Melee Damage");
        }
        if (bonusEarthDefense > 0) {
            lore.add(nToString((int) (bonusEarthDefense * rpg.getInt("EarthDefense") / 100.0), true) + "% \u00a77Earth Defense");
        }
        if (bonusFireDefense > 0) {
            lore.add(nToString((int) (bonusFireDefense * rpg.getInt("FireDefense") / 100.0), true) + "% \u00a77Fire Defense");
        }
        if (bonusWindDefense > 0) {
            lore.add(nToString((int) (bonusWindDefense * rpg.getInt("WindDefense") / 100.0), true) + "% \u00a77Wind Defense");
        }
        if (bonusWaterDefense > 0) {
            lore.add(nToString((int) (bonusWaterDefense * rpg.getInt("WaterDefense") / 100.0), true) + "% \u00a77Water Defense");
        }
        if (bonusEarthDamage > 0) {
            lore.add(nToString((int) (bonusEarthDamage * rpg.getInt("EarthDamage") / 100.0), true) + "% \u00a77Earth Damage");
        }
        if (bonusFireDamage > 0) {
            lore.add(nToString((int) (bonusFireDamage * rpg.getInt("FireDamage") / 100.0), true) + "% \u00a77Fire Damage");
        }
        if (bonusWindDamage > 0) {
            lore.add(nToString((int) (bonusWindDamage * rpg.getInt("WindDamage") / 100.0), true) + "% \u00a77Wind Damage");
        }
        if (bonusWaterDamage > 0) {
            lore.add(nToString((int) (bonusWaterDamage * rpg.getInt("WaterDamage") / 100.0), true) + "% \u00a77Water Damage");
        }
        if (bonusHealthRegen > 0) {
            lore.add(nToString((int) (bonusHealthRegen * rpg.getInt("HealthRegen") / 100.0), true) + " \u00a77Health Regen");
        }
        if (bonusHealth > 0) {
            lore.add(nToString((int) (bonusHealth * rpg.getInt("Health") / 100.0), true) + " \u00a77Health");
        }
        if (bonusWalkSpeed > 0) {
            lore.add(nToString((int) (bonusWalkSpeed * rpg.getInt("WalkSpeed") / 100.0), true) + "% \u00a77Walk Speed");
        }
        lore.add("");
        lore.addAll(this.lore);
        lore.add(tierPrefix.get(tier) + Util.capitalize(tier) + " Item");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        meta.setUnbreakable(true);
        itemStack.setItemMeta(meta);
        itemStack.setDurability((short) 0);
        return itemStack;
    }

    public ItemStack roll() {
        ItemStack itemStack = getItem();
        // stage 1: roll it
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagCompound rpg = new NBTTagCompound();
        rpg.set("MeleePercent", new NBTTagInt(getRollFor(bonusMeleePercent)));
        rpg.set("MeleeNeutral", new NBTTagInt(getRollFor(bonusMeleeNeutral)));
        rpg.set("EarthDefense", new NBTTagInt(getRollFor(bonusEarthDefense)));
        rpg.set("FireDefense", new NBTTagInt(getRollFor(bonusFireDefense)));
        rpg.set("WindDefense", new NBTTagInt(getRollFor(bonusWindDefense)));
        rpg.set("WaterDefense", new NBTTagInt(getRollFor(bonusWaterDefense)));
        rpg.set("EarthDamage", new NBTTagInt(getRollFor(bonusEarthDamage)));
        rpg.set("FireDamage", new NBTTagInt(getRollFor(bonusFireDamage)));
        rpg.set("WindDamage", new NBTTagInt(getRollFor(bonusWindDamage)));
        rpg.set("WaterDamage", new NBTTagInt(getRollFor(bonusWaterDamage)));
        rpg.set("HealthRegen", new NBTTagInt(getRollFor(bonusHealthRegen)));
        rpg.set("Health", new NBTTagInt(getRollFor(bonusHealth)));
        rpg.set("WalkSpeed", new NBTTagInt(getRollFor(bonusWalkSpeed)));
        rpg.set("ITEM_ID", new NBTTagString(id));
        compound.set("RPG", rpg);
        compound.set("HideFlags", new NBTTagInt(63));
        nmsStack.setTag(compound);
        itemStack = CraftItemStack.asBukkitCopy(nmsStack);

        return fixStack(itemStack);
    }

    private int getRollFor(int value) {
        if (value > 0) {
            return (int) (positiveMin + (positiveMax - positiveMin + 1) * Math.random());
        } else {
            return (int) (negativeMin + (negativeMax - negativeMin + 1) * Math.random());
        }
    }

    private String nToString(int n, boolean color) {
        if (n > 0) {
            return (color ? "\u00a7a" : "") + "+" + n;
        } else {
            return (color ? "\u00a7c" : "") + n;
        }
    }

    private String nToString(int n) {
        return nToString(n, false);
    }

    private ItemStack getItem() {
        String itemType;
        if (type.equals("weapon")) {
            itemType = texture.toUpperCase();
        } else {
            itemType = (texture + "_" + type).toUpperCase();
        }
        ItemStack itemStack = new ItemStack(Material.valueOf(itemType));
        ItemMeta meta = itemStack.getItemMeta();
        if (texture.equals("leather")) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            int color = Integer.parseInt(this.color, 16);
            leatherMeta.setColor(Color.fromRGB(color));
        }
        meta.setDisplayName(tierPrefix.get(tier) + name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
