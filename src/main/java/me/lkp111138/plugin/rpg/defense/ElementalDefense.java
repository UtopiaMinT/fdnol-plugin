package me.lkp111138.plugin.rpg.defense;

import org.bukkit.configuration.ConfigurationSection;

public class ElementalDefense {
    public double fire;
    public double wind;
    public double water;
    public double earth;

    public static ElementalDefense fromConfig(ConfigurationSection section) {
        ElementalDefense defense = new ElementalDefense();
        defense.fire = section.getDouble("fire");
        defense.wind = section.getDouble("wind");
        defense.water = section.getDouble("water");
        defense.earth = section.getDouble("earth");
        return defense;
    }
}
