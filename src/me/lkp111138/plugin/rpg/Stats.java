package me.lkp111138.plugin.rpg;

import me.lkp111138.plugin.Main;
import me.lkp111138.plugin.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class Stats {
    private static final double K = 0.261648041296;
    private static final double A = 1.79654388542;

    private final Entity entity;

    // defenses
    private double health;
    private double maxHealth;
    private int maxHearts;
    private double healthRegen;

    // damage
    private double damage;

    // time markers
    private long lastDamage;
    private long lastNaturalHeal;

    // stats
    private int deaths;

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
        return damage;
    }

    public void setHealthRegen(double healthRegen) {
        this.healthRegen = healthRegen;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        this.maxHearts = (int) (A * Math.pow(maxHealth, K));
    }

    public void setDamage(double damage) {
        this.damage = damage;
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
