package me.lkp111138.plugin.rpg;

import me.lkp111138.plugin.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

public class CustomMob {
    private String id;
    private EntityType type;
    private String name;
    private double health;
    private double damage;

    private static Map<String, CustomMob> mobRegistry = new HashMap<>();

    public CustomMob(String id, ConfigurationSection section) {
        this.id = id;
        this.name = section.getString("name");
        this.type = EntityType.valueOf(section.getString("type").toUpperCase());
        this.health = section.getInt("health");
        this.damage = section.getInt("damage");

        mobRegistry.put(this.id, this);
    }

    public static CustomMob get(String id) {
        return mobRegistry.get(id);
    }

    public Entity spawnNew(Location location) {
        World world = location.getWorld();
        Entity entity = world.spawnEntity(location, type);
        Stats stats = new Stats();
        stats.setMaxHealth(health);
        stats.fullHeal();
        stats.setDamage(damage);
        entity.setMetadata("rpg", new FixedMetadataValue(Main.getInstance(), stats));
        return entity;
    }

    public double getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }
}
