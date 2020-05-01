package me.lkp111138.plugin.model;

import me.lkp111138.plugin.Main;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class PlayerStats {
    private static final double K = 0.261648041296;
    private static final double A = 1.79654388542;

    private double health;
    private double maxHealth;
    private int maxHearts;
    public double healthRegen;

    private long lastDamage;
    private long lastNaturalHeal;

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getHealthRegen() {
        return healthRegen;
    }

    public void setHealthRegen(double healthRegen) {
        this.healthRegen = healthRegen;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        this.maxHearts = (int) (A * Math.pow(maxHealth, K));
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

    public int getMaxHearts() {
        return maxHearts;
    }

    public double getHealthHalfHearts() {
        return health / maxHealth * maxHearts * 2;
    }

    public void tick() {
        long now = System.currentTimeMillis();
        if (lastDamage < now - 2000) {
            if (lastNaturalHeal < now - 1000) {
                heal(healthRegen);
                lastNaturalHeal = now;
            }
        }
    }

    public static PlayerStats extractFromPlayer(Player player) {
        for (MetadataValue value : player.getMetadata("rpg")) {
            if (value.getOwningPlugin().equals(Main.getInstance())) {
                return (PlayerStats) value.value();
            }
        }
        return null;
    }
}
