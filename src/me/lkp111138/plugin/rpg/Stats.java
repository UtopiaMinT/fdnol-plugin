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

    private final Entity entity;
    private final Random random = new Random();

    // defenses
    private double health;
    private double maxHealth;
    private int maxHearts;
    private double healthRegen;

    // damage
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

    // health bar
    private boolean showBar;
    private ArmorStand barEntity;

    public Stats(Entity entity) {
        this.entity = entity;
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
            setMaxHealth(maxHealth + 10);
            fullHeal();
            if (entity instanceof Player) {
                ((Player) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHearts() * 2);
            }
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
        return damageMin + (damageMax - damageMin) * random.nextFloat();
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
