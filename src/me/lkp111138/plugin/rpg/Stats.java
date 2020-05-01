package me.lkp111138.plugin.rpg;

import me.lkp111138.plugin.Main;
import me.lkp111138.plugin.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.MetadataValue;

public class Stats {
    private static final double K = 0.261648041296;
    private static final double A = 1.79654388542;

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

    // health bar
    private boolean showBar;
    private ArmorStand barEntity;

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

    public boolean damage(double amount) {
        health -= amount;
        lastDamage = System.currentTimeMillis();
        return health > 0;
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
