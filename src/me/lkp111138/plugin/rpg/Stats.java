package me.lkp111138.plugin.rpg;

import me.lkp111138.plugin.Main;
import me.lkp111138.plugin.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.Random;

public class Stats {
    private static final double K = 0.261648041296;
    private static final double A = 1.79654388542;
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
             0.00,  0.50,  0.99,  1.49,  1.97,  2.46,  2.95,  3.43,  3.91,  4.39,
             4.86,  5.34,  5.81,  6.28,  6.74,  7.21,  7.67,  8.13,  8.59,  9.05,
             9.50,  9.96, 10.41, 10.85, 11.30, 11.75, 12.19, 12.63, 13.07, 13.50,
            13.94, 14.37, 14.80, 15.23, 15.65, 16.08, 16.50, 16.92, 17.34, 17.76,
            18.17, 18.58, 18.99, 19.40, 19.81, 20.22, 20.62, 21.02, 21.42, 21.82,
            22.22, 22.61, 23.00, 23.39, 23.78, 24.17, 24.56, 24.94, 25.32, 25.70,
            26.08, 26.46, 26.83, 27.20, 27.58, 27.95, 28.31, 28.68, 29.05, 29.41,
            29.77, 30.13, 30.49, 30.84, 31.20, 31.55, 31.90, 32.25, 32.60, 32.95,
            33.30, 33.64, 33.98, 34.32, 34.66, 35.00, 35.33, 35.67, 36.00, 36.33,
            36.66, 36.99, 37.32, 37.64, 37.97, 38.29, 38.61, 38.93, 39.25, 39.56,
            39.88, 40.19, 40.51, 40.82, 41.13, 41.43, 41.74, 42.04, 42.35, 42.65,
            42.95, 43.25, 43.55, 43.85, 44.14, 44.44, 44.73, 45.02, 45.31, 45.60,
            45.89, 46.17, 46.46, 46.74, 47.02, 47.30, 47.58, 47.86, 48.14, 48.42,
            48.69, 48.96, 49.24, 49.51, 49.78, 50.04, 50.31, 50.58, 50.84, 51.11,
            51.37, 51.63, 51.89, 52.15, 52.41, 52.66, 52.92, 53.17, 53.42, 53.68,
            53.93, 54.18, 54.42, 54.67, 54.92, 55.16, 55.41, 55.65, 55.89, 56.13,
            56.37, 56.61, 56.84, 57.08, 57.32, 57.55, 57.78, 58.01, 58.25, 58.47,
            58.70, 58.93, 59.16, 59.38, 59.61, 59.83, 60.05, 60.27, 60.50, 60.71,
            60.93, 61.15, 61.37, 61.58, 61.80, 62.01, 62.22, 62.43, 62.64, 62.85,
            63.06, 63.27, 63.48, 63.68, 63.89, 64.09, 64.29, 64.50, 64.70, 64.90,
            65.10, 65.29, 65.49, 65.69, 65.88, 66.08, 66.27, 66.47, 66.66, 66.85,
            67.04, 67.23, 67.42, 67.61, 67.79, 67.98, 68.16, 68.35, 68.53, 68.71,
            68.90, 69.08, 69.26, 69.44, 69.61, 69.79, 69.97, 70.14, 70.32, 70.49,
            70.67, 70.84, 71.01, 71.18, 71.36, 71.52, 71.69, 71.86, 72.03, 72.20,
            72.36, 72.53, 72.69, 72.85, 73.02, 73.18, 73.34, 73.50, 73.66, 73.82,
            73.98, 74.14, 74.29, 74.45, 74.61, 74.76, 74.92, 75.07, 75.22, 75.37,
            75.52, 75.68, 75.83, 75.97, 76.12, 76.27, 76.42, 76.56, 76.71, 76.86,
            77.00, 77.14, 77.29, 77.43, 77.57, 77.71, 77.85, 77.99, 78.13, 78.27,
            78.41, 78.55, 78.68, 78.82, 78.96, 79.09, 79.23, 79.36, 79.49, 79.63,
            79.76, 79.89, 80.02, 80.15, 80.28, 80.41, 80.54, 80.66, 80.79, 80.92,
            81.04, 81.17, 81.29, 81.42, 81.54, 81.66, 81.79, 81.91, 82.03, 82.15,
            82.27, 82.39, 82.51, 82.63, 82.75, 82.87, 82.98, 83.10, 83.22, 83.33,
            83.45, 83.56, 83.67, 83.79, 83.90, 84.01, 84.12, 84.24, 84.35, 84.46,
            84.57, 84.68, 84.79, 84.89, 85.00, 85.11, 85.22, 85.32, 85.43, 85.53,
            85.64, 85.74, 85.85, 85.95, 86.05, 86.16, 86.26, 86.36, 86.46, 86.56,
            86.66, 86.76, 86.86, 86.96, 87.06, 87.16, 87.25, 87.35, 87.45, 87.54,
            87.64, 87.73, 87.83, 87.92, 88.02, 88.11, 88.20, 88.30, 88.39, 88.48,
            88.57, 88.66, 88.75, 88.84, 88.93, 89.02, 89.11, 89.20, 89.29, 89.38,
            89.46, 89.55, 89.64, 89.72, 89.81, 89.89, 89.98, 90.06, 90.15, 90.23,
            90.31, 90.40, 90.48, 90.56, 90.64, 90.73, 90.81, 90.89, 90.97, 91.05
    };

    private final Entity entity;
    private final Random random = new Random();
    private double baseSpeed;

    // base defenses
    private double health;
    private double maxHealth;
    private int maxHearts;
    private double healthRegen;

    // base damage
    private double damageMin;
    private double damageMax;

    // time markers
    private long lastDamage;
    private long lastNaturalHeal;

    // stats
    private int deaths;
    private long totalXP;
    private int level;
    private int levelXP;

    // skills
    private int strengthSkill; // increases damage, slightly decreases hp
    private int defenseSkill; // decreases damage taken, increases hp, decreases speed
    private int speedSkill; // increases speed, slightly decreases damage
    private int freeSkill; // does nothing

    // health bar
    private boolean showBar;
    private ArmorStand barEntity;

    public Stats(Entity entity) {
        this.entity = entity;
        if (entity instanceof Player)
        this.baseSpeed = ((Player) entity).getWalkSpeed();
    }

    public void tick(Location entityLoc) {
        long now = System.currentTimeMillis();
        if (lastDamage < now - 2000) {
            if (lastNaturalHeal < now - 1000) {
                heal(healthRegen);
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
            barEntity.teleport(entityLoc);
        } else {
            if (barEntity != null) {
                barEntity.remove();
                barEntity = null;
            }
        }
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.setExp((float) levelXP / XP_TABLE[level]);
            player.setLevel(level);
        }
    }

    public double damage(double amount) {
        // apply skills
        amount *= (100 - SKILL_TABLE[defenseSkill]) / 100;
        if (health < amount && entity instanceof Player) {
            // players don't really die, they get a death stat and get tped to spawn instead
            double damage = getHealthHalfHearts() - 0.0001;
            fullHeal();
            ++deaths;
            entity.teleport(new Location(entity.getWorld(), -111.5, 104, 272.5));
            ((Player) entity).sendTitle("\u00a74You have died!", "Soul Integrity dropped by 18%", 10, 50, 10);
            return damage;
        }
        health -= amount;
        lastDamage = System.currentTimeMillis();
        return amount / maxHealth * maxHearts * 2;
    }

    public void addXP(int amount) {
        totalXP += amount;
        levelXP += amount;
        while (level < XP_TABLE.length && levelXP >= XP_TABLE[level]) {
            levelXP -= XP_TABLE[level];
            ++level;
            freeSkill += 3;
            setMaxHealth(maxHealth + 10);
            fullHeal();
            if (entity instanceof Player) {
                ((Player) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHearts() * 2);
            }
        }
    }

    public void allocate(int strengthSkill, int defenseSkill, int speedSkill) {
        int total = strengthSkill + defenseSkill + speedSkill;
        if (total <= freeSkill) {
            this.strengthSkill += strengthSkill;
            this.defenseSkill += defenseSkill;
            this.speedSkill += speedSkill;
            this.freeSkill -= total;
            double speedAttribute = baseSpeed * (100 + SKILL_TABLE[this.speedSkill]) / 100;
            System.out.println(speedAttribute);
            if (entity instanceof Player) {
                ((Player) entity).setWalkSpeed((float) speedAttribute);
            }
        }
    }

    public void resetSkills() {
        freeSkill += strengthSkill + defenseSkill + speedSkill;
        strengthSkill = 0;
        defenseSkill = 0;
        speedSkill = 0;
        if (entity instanceof Player) {
            ((Player) entity).setWalkSpeed((float) baseSpeed);
        }
    }

    public void damagePercent(double percent) {
        damage(percent / 100 * maxHealth);
    }

    public void heal(double amount) {
        health += amount;
        health = health > maxHealth ? maxHealth : health;
    }

    public void fullHeal() {
        health = maxHealth;
    }

    public int getMaxHearts() {
        return maxHearts;
    }

    public double getHealthHalfHearts() {
        return health / maxHealth * maxHearts * 2;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getHealthRegen() {
        return healthRegen;
    }

    public double getDamage() {
        double damage = damageMin + (damageMax - damageMin) * random.nextFloat();
        return damage * (100 + SKILL_TABLE[strengthSkill]) / 100;
    }

    public int getStrengthSkill() {
        return strengthSkill;
    }

    public int getDefenseSkill() {
        return defenseSkill;
    }

    public int getSpeedSkill() {
        return speedSkill;
    }

    public int getFreeSkill() {
        return freeSkill;
    }

    public void setHealthRegen(double healthRegen) {
        this.healthRegen = healthRegen;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        this.maxHearts = (int) (A * Math.pow(maxHealth, K));
    }

    public void setDamage(double damageMin, double damageMax) {
        this.damageMin = damageMin;
        this.damageMax = damageMax;
    }

    public void setShowBar(boolean showBar) {
        this.showBar = showBar;
    }

    public static Stats extractFromEntity(Entity entity) {
        for (MetadataValue value : entity.getMetadata("rpg")) {
            if (value.getOwningPlugin().equals(Main.getInstance())) {
                return (Stats) value.value();
            }
        }
        return null;
    }
}
