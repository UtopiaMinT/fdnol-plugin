package me.lkp111138.plugin.rpg.damage;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class ElementalDamageRange {
    public double minNeutral;
    public double maxNeutral;
    public double minEarth;
    public double maxEarth;
    public double minWater;
    public double maxWater;
    public double minFire;
    public double maxFire;
    public double minThunder;
    public double maxThunder;

    public static ElementalDamageRange fromConfig(ConfigurationSection section) {
        ElementalDamageRange range = new ElementalDamageRange();
        List<Double> neutral = section.getDoubleList("neutral");
        range.minNeutral = neutral.get(0);
        range.maxNeutral = neutral.get(1);
        List<Double> earth = section.getDoubleList("earth");
        range.minEarth = earth.get(0);
        range.maxEarth = earth.get(1);
        List<Double> water = section.getDoubleList("water");
        range.minWater = water.get(0);
        range.maxWater = water.get(1);
        List<Double> fire = section.getDoubleList("fire");
        range.minFire = fire.get(0);
        range.maxFire = fire.get(1);
        List<Double> thunder = section.getDoubleList("thunder");
        range.minThunder = thunder.get(0);
        range.maxThunder = thunder.get(1);
        return range;
    }

    public ElementalDamage getDamage() {
        ElementalDamage damage = new ElementalDamage();
        damage.neutral = (int) (minNeutral + Math.random() * (maxNeutral - minNeutral));
        damage.earth = (int) (minEarth + Math.random() * (maxEarth - minEarth));
        damage.water = (int) (minWater + Math.random() * (maxWater - minWater));
        damage.fire = (int) (minFire + Math.random() * (maxFire - minFire));
        damage.thunder = (int) (minThunder + Math.random() * (maxThunder - minThunder));
        return damage;
    }
}
