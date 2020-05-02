package me.lkp111138.plugin.rpg.mob;

import me.lkp111138.plugin.Main;
import me.lkp111138.plugin.rpg.Stats;
import me.lkp111138.plugin.rpg.damage.ElementalDamageRange;
import me.lkp111138.plugin.rpg.defense.ElementalDefense;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CustomMob {
    private String id;
    private EntityType type;
    private String name;
    private double health;
    private ElementalDamageRange damage;
    private double regen;
    private List<Integer> xp;
    private ElementalDefense elementalDefense;
    private Random random = new Random();

    private static Map<String, CustomMob> mobRegistry = new HashMap<>();

    public CustomMob(String id, ConfigurationSection section) {
        this.id = id;
        this.name = section.getString("name");
        this.type = EntityType.valueOf(section.getString("type").toUpperCase());
        this.health = section.getDouble("health");
        this.damage = ElementalDamageRange.fromConfig(section.getConfigurationSection("damage"));
        this.regen = section.getDouble("regen");
        this.xp = section.getIntegerList("xp");
        this.elementalDefense = ElementalDefense.fromConfig(section.getConfigurationSection("defense"));

        mobRegistry.put(this.id, this);
    }

    public static CustomMob get(String id) {
        return mobRegistry.get(id);
    }

    public Entity spawnNew(Location location) {
        World world = location.getWorld();
        Entity entity = world.spawnEntity(location, type);
        Stats stats = new Stats(entity);
        stats.setMaxHealth(health);
        stats.fullHeal();
        stats.setDamage(damage);
        stats.setShowBar(true);
        stats.setHealthRegen(regen);
        stats.setElementalDefense(elementalDefense);
        entity.setMetadata("rpg", new FixedMetadataValue(Main.getInstance(), stats));
        entity.setMetadata("custommob", new FixedMetadataValue(Main.getInstance(), this));
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);

        return entity;
    }

    public int getXP() {
        int lower = this.xp.get(0);
        int upper = this.xp.get(1);
        return lower + random.nextInt(upper - lower);
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public static CustomMob extractFromEntity(Entity entity) {
        for (MetadataValue value : entity.getMetadata("custommob")) {
            if (value.getOwningPlugin().equals(Main.getInstance())) {
                return (CustomMob) value.value();
            }
        }
        return null;
    }
}
