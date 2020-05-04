package me.lkp111138.plugin.rpg;

import me.lkp111138.plugin.Main;
import me.lkp111138.plugin.Util;
import me.lkp111138.plugin.rpg.damage.ElementalDamage;
import me.lkp111138.plugin.rpg.damage.ElementalDamageRange;
import me.lkp111138.plugin.rpg.defense.ElementalDefense;
import me.lkp111138.plugin.rpg.items.Build;
import me.lkp111138.plugin.rpg.items.RpgItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Stats {
    private static final double K = 0.261648041296;
    private static final double A = 1.79654388542;
    private static final int MAX_LEVEL = 100;
    private static final int[] XP_TABLE = {
            80, 96, 115, 138, 165, 198, 236, 282, 336, 400,
            476, 566, 672, 797, 944, 1118, 1322, 1561, 1843, 2173,
            2559, 3011, 3539, 4155, 4874, 5711, 6686, 7818, 9134, 10659,
            12426, 14471, 16836, 19567, 22717, 26347, 30526, 35331, 40849, 47181,
            54437, 62745, 72244, 83095, 95476, 109588, 125653, 143923, 164677, 188226,
            214916, 245133, 279305, 317905, 361458, 410544, 465803, 527941, 597735, 676038,
            763788, 862011, 971832, 1094477, 1231286, 1383719, 1553363, 1741942, 1951323, 2183531,
            2440751, 2725342, 3039847, 3386997, 3769728, 4191183, 4654728, 5163955, 5722695, 6335024,
            7005269, 7738020, 8538132, 9410729, 10361212, 11395261, 12518834, 13738169, 15059781, 16490460,
            18037265, 19707515, 21508782, 23448875, 25535824, 27777870, 30183433, 32761099, 35519583, 38467708, -1};
    private static final double[] SKILL_TABLE = {
             0.00,  0.50,  1.00,  1.49,  1.98,  2.47,  2.96,  3.44,  3.92,  4.40,
             4.88,  5.35,  5.82,  6.29,  6.76,  7.23,  7.69,  8.15,  8.61,  9.06,
             9.52,  9.97, 10.42, 10.86, 11.31, 11.75, 12.19, 12.63, 13.06, 13.50,
            13.93, 14.36, 14.79, 15.21, 15.63, 16.05, 16.47, 16.89, 17.30, 17.72,
            18.13, 18.54, 18.94, 19.35, 19.75, 20.15, 20.55, 20.94, 21.34, 21.73,
            22.12, 22.51, 22.89, 23.28, 23.66, 24.04, 24.42, 24.80, 25.17, 25.55,
            25.92, 26.29, 26.66, 27.02, 27.39, 27.75, 28.11, 28.47, 28.82, 29.18,
            29.53, 29.88, 30.23, 30.58, 30.93, 31.27, 31.61, 31.95, 32.29, 32.63,
            32.97, 33.30, 33.63, 33.97, 34.30, 34.62, 34.95, 35.27, 35.60, 35.92,
            36.24, 36.56, 36.87, 37.19, 37.50, 37.81, 38.12, 38.43, 38.74, 39.04,
            39.35, 39.65, 39.95, 40.25, 40.55, 40.84, 41.14, 41.43, 41.73, 42.02,
            42.31, 42.59, 42.88, 43.16, 43.45, 43.73, 44.01, 44.29, 44.57, 44.84,
            45.12, 45.39, 45.66, 45.94, 46.21, 46.47, 46.74, 47.01, 47.27, 47.53,
            47.80, 48.06, 48.31, 48.57, 48.83, 49.08, 49.34, 49.59, 49.84, 50.09,
            50.34, 50.59, 50.84, 51.08, 51.32, 51.57, 51.81, 52.05, 52.29, 52.53,
            52.76, 53.00, 53.23, 53.47, 53.70, 53.93, 54.16, 54.39, 54.62, 54.84,
            55.07, 55.29, 55.51, 55.74, 55.96, 56.18, 56.40, 56.61, 56.83, 57.04,
            57.26, 57.47, 57.68, 57.89, 58.10, 58.31, 58.52, 58.73, 58.93, 59.14,
            59.34, 59.55, 59.75, 59.95, 60.15, 60.35, 60.54, 60.74, 60.94, 61.13,
            61.33, 61.52, 61.71, 61.90, 62.09, 62.28, 62.47, 62.66, 62.84, 63.03,
            63.21, 63.40, 63.58, 63.76, 63.94, 64.12, 64.30, 64.48, 64.65, 64.83,
            65.01, 65.18, 65.35, 65.53, 65.70, 65.87, 66.04, 66.21, 66.38, 66.55,
            66.71, 66.88, 67.04, 67.21, 67.37, 67.53, 67.70, 67.86, 68.02, 68.18,
            68.34, 68.49, 68.65, 68.81, 68.96, 69.12, 69.27, 69.43, 69.58, 69.73,
            69.88, 70.03, 70.18, 70.33, 70.48, 70.62, 70.77, 70.92, 71.06, 71.21,
            71.35, 71.49, 71.63, 71.78, 71.92, 72.06, 72.20, 72.33, 72.47, 72.61,
            72.75, 72.88, 73.02, 73.15, 73.29, 73.42, 73.55, 73.68, 73.82, 73.95,
            74.08, 74.21, 74.33, 74.46, 74.59, 74.72, 74.84, 74.97, 75.09, 75.22,
            75.34, 75.46, 75.59, 75.71, 75.83, 75.95, 76.07, 76.19, 76.31, 76.43,
            76.54, 76.66, 76.78, 76.89, 77.01, 77.12, 77.24, 77.35, 77.46, 77.58,
            77.69, 77.80, 77.91, 78.02, 78.13, 78.24, 78.35, 78.45, 78.56, 78.67,
            78.78, 78.88, 78.99, 79.09, 79.20, 79.30, 79.40, 79.51, 79.61, 79.71,
            79.81, 79.91, 80.01, 80.11, 80.21, 80.31, 80.41, 80.50, 80.60, 80.70,
            80.80, 80.89, 80.99, 81.08, 81.18, 81.27, 81.36, 81.46, 81.55, 81.64,
            81.73, 81.82, 81.91, 82.00, 82.09, 82.18, 82.27, 82.36, 82.45, 82.54,
            82.62, 82.71, 82.80, 82.88, 82.97, 83.05, 83.14, 83.22, 83.30, 83.39,
            83.47, 83.55, 83.63, 83.72, 83.80, 83.88, 83.96, 84.04, 84.12, 84.20,
            84.28, 84.35, 84.43, 84.51, 84.59, 84.66, 84.74, 84.82, 84.89, 84.97,
            85.04, 85.12, 85.19, 85.27, 85.34, 85.41, 85.49, 85.56, 85.63, 85.70,
            85.77, 85.84, 85.91, 85.98, 86.05, 86.12, 86.19, 86.26, 86.33, 86.40, 86.47
    };
    public static Map<String, Integer> SLOTS = new HashMap<String, Integer>() {{
        put("helmet", 5);
        put("chestplate", 6);
        put("leggings", 7);
        put("boots", 8);
    }};
    public static Map<Integer, String> SLOTS_REVERSE = new HashMap<Integer, String>() {{
        put(5, "helmet");
        put(6, "chestplate");
        put(7, "leggings");
        put(8, "boots");
    }};

    // owner of the stats
    private final Entity entity;
    private final Player player;
    private UUID uuid;
    private final Build build;
    private int buildHash;
    private boolean xpAwarded;

    // base defenses
    private double health;
    private double maxHealth;
    private int maxHearts;
    private double healthRegen;
    private ElementalDefense elementalDefense;

    // base damage
    private ElementalDamageRange damage;

    // time markers
    private long lastDamage;
    private long lastNaturalHeal;

    // stats
    private int deaths;
    private long totalXP;
    private int level;
    private int levelXP;

    // skills
    private int powerSkill; // +earth, -wind, +all dmg
    private int defenseSkill; // +fire, -water, +less dmg intake
    private int speedSkill; // +wind, -fire, +dodge
    private int intelligenceSkill; // +water, -earth, +mana
    private int freeSkill; // does nothing

    // health bar
    private boolean showBar;
    private ArmorStand barEntity;


    public Stats(Entity entity) {
        this.entity = entity;
        this.build = new Build();
        if (entity instanceof Player) {
            this.uuid = entity.getUniqueId();
            this.player = (Player) entity;
        } else {
            this.player = null;
        }
        this.elementalDefense = new ElementalDefense();
        this.damage = new ElementalDamageRange();
    }

    public void tick(Location entityLoc) {
        long now = System.currentTimeMillis();
        if (lastDamage < now - 2000) {
            if (lastNaturalHeal < now - 1000) {
                heal(getHealthRegen());
                lastNaturalHeal = now;
            }
        }
        if (showBar && lastDamage > 0) {
            if (barEntity == null) {
                World world = entityLoc.getWorld();
                barEntity = (ArmorStand) world.spawnEntity(entityLoc, EntityType.ARMOR_STAND);
                barEntity.setInvulnerable(true);
                barEntity.setVisible(false);
                barEntity.setCustomNameVisible(true);
                barEntity.setVelocity(entity.getVelocity());
                barEntity.setGravity(false);
            }
            StringBuilder nameBuilder = new StringBuilder("[");
            int digitCount = Util.digitCount((int) health);
            int leftBars = (10 - digitCount) / 2;
            int rightBars = 10 - digitCount - leftBars;
            for (int i = 0; i < leftBars; i++) {
                nameBuilder.append("|");
            }
            nameBuilder.append((int) health);
            for (int i = 0; i < rightBars; i++) {
                nameBuilder.append("|");
            }
            nameBuilder.append("]");
            String name = nameBuilder.toString();
            int fraction = (int) ((24 * health / maxHealth + 1) / 2);
            barEntity.setCustomName("\u00a74" + name.substring(0, fraction) + "\u00a78" + name.substring(fraction));
            Vector adjustment = new Vector(0, entity.getHeight() - 2.05, 0);
            barEntity.teleport(entityLoc.add(adjustment));
        } else {
            if (barEntity != null) {
                barEntity.remove();
                barEntity = null;
            }
        }
        if (player != null) {
            player.setExp(level >= MAX_LEVEL ? 0 : (float) levelXP / XP_TABLE[level]);
            player.setLevel(level);
        }
    }

    public double damage(ElementalDamage elementalDamage) {
        // apply skills
        ElementalDefense effectiveDefense = getEffectiveElementalDefense();
        elementalDamage.neutral = (int) Math.max(0, elementalDamage.neutral * (100 - SKILL_TABLE[defenseSkill]) / 100.0);
        elementalDamage.wind = (int) Math.max(0, (elementalDamage.wind - effectiveDefense.wind) * (100 - SKILL_TABLE[defenseSkill]) / 100.0);
        elementalDamage.fire = (int) Math.max(0, (elementalDamage.fire - effectiveDefense.fire) * (100 - SKILL_TABLE[defenseSkill]) / 100.0);
        elementalDamage.earth = (int) Math.max(0, (elementalDamage.earth - effectiveDefense.earth) * (100 - SKILL_TABLE[defenseSkill]) / 100.0);
        elementalDamage.water = (int) Math.max(0, (elementalDamage.water - effectiveDefense.water) * (100 - SKILL_TABLE[defenseSkill]) / 100.0);
        double amount = elementalDamage.sum();
        if (health < amount && player != null) {
            // players don't really die, they get a death stat and get tped to spawn instead
            double damage = getHealthHalfHearts() - 0.0001;
            fullHeal();
            ++deaths;
            player.teleport(new Location(player.getWorld(), -111.5, 104, 272.5));
            player.sendTitle("\u00a74You have died!", "Soul Integrity dropped by 18%", 10, 50, 10);
            return damage;
        }
        health -= amount;
        // damage indicator
        lastDamage = System.currentTimeMillis();
        Vector adjustment = new Vector(0, entity.getHeight() - 1.75, 0);
        adjustment.add(new Vector(Math.random() - 0.5, Math.random() / 2, Math.random() - 0.5));
        Location entityLoc = entity.getLocation().add(adjustment);
        ArmorStand indicatorEntity = (ArmorStand) entityLoc.getWorld().spawnEntity(entityLoc, EntityType.ARMOR_STAND);
        indicatorEntity.setCustomNameVisible(true);
        indicatorEntity.setInvulnerable(true);
        indicatorEntity.setVisible(false);
        indicatorEntity.setGravity(false);
        StringBuilder damageBuilder = new StringBuilder();
        if (elementalDamage.neutral >= 1) {
            damageBuilder.append("\u00a76-").append(elementalDamage.neutral);
        }
        if (elementalDamage.fire >= 1) {
            if (damageBuilder.length() > 0) {
                damageBuilder.append("   ");
            }
            damageBuilder.append("\u00a7c-").append(elementalDamage.fire);
        }
        if (elementalDamage.water >= 1) {
            if (damageBuilder.length() > 0) {
                damageBuilder.append("   ");
            }
            damageBuilder.append("\u00a7b-").append(elementalDamage.water);
        }
        if (elementalDamage.earth >= 1) {
            if (damageBuilder.length() > 0) {
                damageBuilder.append("   ");
            }
            damageBuilder.append("\u00a72-").append(elementalDamage.earth);
        }
        if (elementalDamage.wind >= 1) {
            if (damageBuilder.length() > 0) {
                damageBuilder.append("   ");
            }
            damageBuilder.append("\u00a77-").append(elementalDamage.wind);
        }
        indicatorEntity.setCustomName(damageBuilder.toString());
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), indicatorEntity::remove, 40);
        return amount / maxHealth * maxHearts * 2;
    }

    private ElementalDefense getEffectiveElementalDefense() {
        ElementalDefense effective = new ElementalDefense();
        effective.fire = (elementalDefense.fire + build.getBaseFireDefense()) * (100 + build.getBonusFireDefense()) / 100.0;
        effective.earth = (elementalDefense.earth + build.getBaseEarthDefense()) * (100 + build.getBonusEarthDefense()) / 100.0;
        effective.water = (elementalDefense.water + build.getBaseWaterDefense()) * (100 + build.getBonusWaterDefense()) / 100.0;
        effective.wind = (elementalDefense.wind + build.getBaseWindDefense()) * (100 + build.getBonusWindDefense()) / 100.0;
        return effective;
    }

    private String equip(ItemStack item, String slot) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        // check if its a valid item
        String itemId = RpgItem.getItemId(item);
        RpgItem rpgItem = RpgItem.get(itemId);
        if (itemId == null || rpgItem == null) {
            if (slot.equals("weapon")) {
                // you can use anything as a "weapon" it just deals no damage
                return null;
            }
            return "Invalid Item!";
        }
        if (!rpgItem.type.equals(slot)) {
            // right thing in wrong slot
            return null;
        }
        // check of stats fulfilled
        if (level < rpgItem.reqLevel) {
            return String.format("%s requires your Level to be at least %s.", rpgItem.name, rpgItem.reqLevel);
        }
        if (getEffectivePowerSkill() < rpgItem.reqPower) {
            return String.format("%s requires your Power Skill to be at least %s.", rpgItem.name, rpgItem.reqPower);
        }
        if (getEffectiveIntelligenceSkill() < rpgItem.reqIntelligence) {
            return String.format("%s requires your Intelligence Skill to be at least %s.", rpgItem.name, rpgItem.reqIntelligence);
        }
        if (getEffectiveSpeedSkill() < rpgItem.reqSpeed) {
            return String.format("%s requires your Speed Skill to be at least %s.", rpgItem.name, rpgItem.reqSpeed);
        }
        if (getEffectiveDefenseSkill() < rpgItem.reqDefense) {
            return String.format("%s requires your Defense Skill to be at least %s.", rpgItem.name, rpgItem.reqDefense);
        }
        // ok real equip
        switch (rpgItem.type) {
            case "helmet":
                build.setHelmet(item);
                break;
            case "chestplate":
                build.setChestplate(item);
                break;
            case "leggings":
                build.setLeggings(item);
                break;
            case "boots":
                build.setBoots(item);
                break;
            case "weapon":
                build.setWeapon(item);
                break;
        }
        if (player != null) {
            player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(-200);
            Util.setWalkSpeed((Player) entity, 0.2f * (100 + build.getBonusWalkSpeed()) / 100f);
        }
        return null;
    }

    public void resetBuild(boolean send) {
//        long start = System.nanoTime();
        if (player != null) {
            PlayerInventory inv = player.getInventory();
            Map<String, ItemStack> items = new HashMap<>();
            items.put("helmet", inv.getHelmet());
            items.put("chestplate", inv.getChestplate());
            items.put("leggings", inv.getLeggings());
            items.put("boots", inv.getBoots());
            items.put("weapon", inv.getItemInMainHand());
            items.keySet().removeIf(key -> items.get(key) == null);
            int hash = items.hashCode() + powerSkill + defenseSkill + speedSkill + intelligenceSkill;
            if (hash == buildHash) {
                return;
            }
            buildHash = hash;
            build.setHelmet(null);
            build.setChestplate(null);
            build.setLeggings(null);
            build.setBoots(null);
            build.setWeapon(null);
            while (items.keySet().removeIf(key -> this.equip(items.get(key), key) == null));
            for (String key : items.keySet()) {
                if (send || !key.equals("weapon")) {
                    player.sendMessage("\u00a7c" + equip(items.get(key), key));
                }
                ItemStack air = new ItemStack(Material.AIR);
                switch (key) {
                    case "helmet":
                        if (inv.addItem(inv.getHelmet()).isEmpty()) {
                            inv.setHelmet(air);
                        }
                        break;
                    case "chestplate":
                        if (inv.addItem(inv.getChestplate()).isEmpty()) {
                            inv.setChestplate(air);
                        }
                        break;
                    case "leggings":
                        if (inv.addItem(inv.getLeggings()).isEmpty()) {
                            inv.setLeggings(air);
                        }
                        break;
                    case "boots":
                        if (inv.addItem(inv.getBoots()).isEmpty()) {
                            inv.setBoots(air);
                        }
                        break;
                }
            }
            Util.setWalkSpeed(player, 0.002f * (100 + build.getBonusWalkSpeed()));
            heal(0);
            setMaxHealth(maxHealth);
        }
//        System.out.println("resetBuild: " + (System.nanoTime() - start));
    }

    public void addXP(long amount) {
        totalXP += amount;
        levelXP += amount;
        while (level < MAX_LEVEL && levelXP >= XP_TABLE[level]) {
            levelXP -= XP_TABLE[level];
            ++level;
            freeSkill += 3;
            setMaxHealth(maxHealth + 10);
            fullHeal();
            if (player != null) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHearts() * 2);
            }
        }
    }

    public void allocate(int strengthSkill, int defenseSkill, int speedSkill, int intelligenceSkill) {
        int total = strengthSkill + defenseSkill + speedSkill + intelligenceSkill;
        if (total <= freeSkill) {
            this.powerSkill += strengthSkill;
            this.defenseSkill += defenseSkill;
            this.speedSkill += speedSkill;
            this.intelligenceSkill += intelligenceSkill;
            this.freeSkill -= total;
            double speedAttribute = 0.2 * (100 + SKILL_TABLE[this.speedSkill]) / 100;
            if (player != null) {
//                player.setWalkSpeed((float) speedAttribute);
            }
        }
    }

    public void resetSkills() {
        freeSkill += powerSkill + defenseSkill + speedSkill + intelligenceSkill;
        powerSkill = 0;
        defenseSkill = 0;
        speedSkill = 0;
        intelligenceSkill = 0;
        if (player != null) {
            Util.setWalkSpeed(player, 0.2f);
        }
    }

    public void damagePercent(double percent) {
        ElementalDamage elementalDamage = new ElementalDamage();
        elementalDamage.neutral = (int) (percent / 100 * getMaxHealth());
        damage(elementalDamage);
    }

    public void heal(double amount) {
        health += amount;
        health = health > getMaxHealth() ? getMaxHealth() : health;
    }

    public void fullHeal() {
        health = getMaxHealth();
    }

    public int getMaxHearts() {
        return maxHearts;
    }

    public double getHealthHalfHearts() {
        return health / getMaxHealth() * maxHearts * 2;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth + build.getBaseHealth() + build.getBonusHealth();
    }

    public double getHealthRegen() {
        return (healthRegen + build.getBonusHealthRegen());
    }

    public ElementalDamage getMeleeDamage() {
        ElementalDamage damage = this.build.getBaseDamage().add(this.damage, 1).getDamage();
        damage.earth *= (100 + SKILL_TABLE[getEffectivePowerSkill()] + SKILL_TABLE[getEffectivePowerSkill()] + build.getBonusMeleePercent() + build.getBonusEarthDamage()) / 100;
        damage.water *= (100 + SKILL_TABLE[getEffectivePowerSkill()] + SKILL_TABLE[getEffectiveIntelligenceSkill()] + build.getBonusMeleePercent() + build.getBonusWaterDamage()) / 100;
        damage.wind *= (100 + SKILL_TABLE[getEffectivePowerSkill()] + SKILL_TABLE[getEffectiveSpeedSkill()] + build.getBonusMeleePercent() + build.getBonusWindDamage()) / 100;
        damage.fire *= (100 + SKILL_TABLE[getEffectivePowerSkill()] + SKILL_TABLE[getEffectiveDefenseSkill()] + build.getBonusMeleePercent() + build.getBonusFireDamage()) / 100;
        damage.neutral *= (100 + SKILL_TABLE[getEffectivePowerSkill()] + build.getBonusMeleePercent()) / 100;
        damage.neutral += build.getBonusMeleeNeutral();
        return damage;
    }

    public long getTotalXP() {
        return totalXP;
    }

    public int getRawPowerSkill() {
        return powerSkill;
    }

    public int getRawDefenseSkill() {
        return defenseSkill;
    }

    public int getRawSpeedSkill() {
        return speedSkill;
    }

    public int getRawIntelligenceSkill() {
        return intelligenceSkill;
    }

    public int getEffectivePowerSkill() {
        return Math.max(0, Math.min(400, powerSkill + build.getBaseBonusPower()));
    }

    public int getEffectiveDefenseSkill() {
        return Math.max(0, Math.min(400, defenseSkill + build.getBaseBonusDefense()));
    }

    public int getEffectiveSpeedSkill() {
        return Math.max(0, Math.min(400, speedSkill + build.getBaseBonusSpeed()));
    }

    public int getEffectiveIntelligenceSkill() {
        return Math.max(0, Math.min(400, intelligenceSkill + build.getBaseBonusIntelligence()));
    }

    public int getFreeSkill() {
        return freeSkill;
    }

    public int getLevel() {
        return level;
    }

    public void setHealthRegen(double healthRegen) {
        this.healthRegen = healthRegen;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        this.maxHearts = (int) (A * Math.pow(getMaxHealth(), K));
    }

    public void setDamage(ElementalDamageRange damage) {
        this.damage = damage;
    }

    public void setShowBar(boolean showBar) {
        this.showBar = showBar;
    }

    public void setElementalDefense(ElementalDefense elementalDefense) {
        this.elementalDefense = elementalDefense;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public Build getBuild() {
        return build;
    }

    public boolean isXpAwarded() {
        return xpAwarded;
    }

    public void setXpAwarded(boolean xpAwarded) {
        this.xpAwarded = xpAwarded;
    }

    public static Stats extractFromEntity(Entity entity) {
        for (MetadataValue value : entity.getMetadata("rpg")) {
            if (value.getOwningPlugin().equals(Main.getInstance())) {
                return (Stats) value.value();
            }
        }
        return null;
    }

    public void save() throws SQLException{
        try (Connection conn = Main.getInstance().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("REPLACE INTO player_stats (uuid, username, power_skill, defense_skill, speed_skill, intel_skill, free_skill, total_xp, deaths, health) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setBytes(1, Util.getBytesFromUUID(uuid));
            stmt.setString(2, entity.getName());
            stmt.setInt(3, powerSkill);
            stmt.setInt(4, defenseSkill);
            stmt.setInt(5, speedSkill);
            stmt.setInt(6, intelligenceSkill);
            stmt.setInt(7, freeSkill);
            stmt.setLong(8, totalXP);
            stmt.setInt(9, deaths);
            stmt.setDouble(10, health);
            stmt.execute();
            stmt.close();
        }
    }
}
