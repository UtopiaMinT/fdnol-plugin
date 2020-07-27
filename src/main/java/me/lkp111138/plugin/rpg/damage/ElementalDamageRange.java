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
    public double minWind;
    public double maxWind;

    public static ElementalDamageRange fromConfig(ConfigurationSection section) {
        ElementalDamageRange range = new ElementalDamageRange();
        if (section == null) {
            return range;
        }
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
        List<Double> wind = section.getDoubleList("wind");
        range.minWind = wind.get(0);
        range.maxWind = wind.get(1);
        return range;
    }

    public ElementalDamage getDamage() {
        ElementalDamage damage = new ElementalDamage();
        damage.neutral = (int) (minNeutral + Math.random() * (maxNeutral - minNeutral));
        damage.earth = (int) (minEarth + Math.random() * (maxEarth - minEarth));
        damage.water = (int) (minWater + Math.random() * (maxWater - minWater));
        damage.fire = (int) (minFire + Math.random() * (maxFire - minFire));
        damage.wind = (int) (minWind + Math.random() * (maxWind - minWind));
        return damage;
    }

    public ElementalDamageRange add(ElementalDamageRange other, int multiplier) {
        if (other == null) {
            return this;
        }
        ElementalDamageRange range = new ElementalDamageRange();
        range.minNeutral = this.minNeutral + multiplier * other.minNeutral;
        range.maxNeutral = this.maxNeutral + multiplier * other.maxNeutral;
        range.minEarth = this.minEarth + multiplier * other.minEarth;
        range.maxEarth = this.maxEarth + multiplier * other.maxEarth;
        range.minWater = this.minWater + multiplier * other.minWater;
        range.maxWater = this.maxWater + multiplier * other.maxWater;
        range.minFire = this.minFire + multiplier * other.minFire;
        range.maxFire = this.maxFire + multiplier * other.maxFire;
        range.minWind = this.minWind + multiplier * other.minWind;
        range.maxWind = this.maxWind + multiplier * other.maxWind;
        return range;
    }
}
