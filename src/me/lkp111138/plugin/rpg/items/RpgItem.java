package me.lkp111138.plugin.rpg.items;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RpgItem {
    private static int positiveMax;
    private static int positiveMin;
    private static int negativeMax;
    private static int negativeMin;
    private static Map<String, String> tierPrefix = new HashMap<>();

    // requirements
    private final int reqPower;
    private final int reqDefense;
    private final int reqSpeed;
    private final int reqIntelligence;
    private final int reqLevel;

    // base
    private final int baseBonusPower;
    private final int baseBonusDefense;
    private final int baseBonusSpeed;
    private final int baseBonusIntelligence;
    private final int baseHealth;
    private final int baseEarthDefense;
    private final int baseFireDefense;
    private final int baseWindDefense;
    private final int baseWaterDefense;

    // bonuses
    private final int bonusMeleePercent;
    private final int bonusMeleeNeutral;
    private final int bonusEarthDefense;
    private final int bonusFireDefense;
    private final int bonusWindDefense;
    private final int bonusWaterDefense;
    private final int bonusEarthDamage;
    private final int bonusFireDamage;
    private final int bonusWindDamage;
    private final int bonusWaterDamage;
    private final int bonusHealthRegen;
    private final int bonusHealth;
    private final int bonusWalkSpeed;

    private final String id;
    private final String name;
    private final String type;
    private final String texture;
    private final String color;
    private final String tier;
    private final List<String> lore;

    public static void init(ConfigurationSection section) {
        List<Integer> positiveRange = section.getIntegerList("positive_range");
        List<Integer> negativeRange = section.getIntegerList("negative_range");
        ConfigurationSection tierSection = section.getConfigurationSection("tiers");
        positiveMax = positiveRange.get(1);
        positiveMin = positiveRange.get(0);
        negativeMax = negativeRange.get(1);
        negativeMin = negativeRange.get(0);
        for (String key : tierSection.getKeys(false)) {
            tierPrefix.put(key, tierSection.getString(key));
        }
    }

    public RpgItem(String id, ConfigurationSection section) {
        this.reqPower = section.getInt("req.power");
        this.reqDefense = section.getInt("req.defense");
        this.reqSpeed = section.getInt("req.speed");
        this.reqIntelligence = section.getInt("req.intelligence");
        this.reqLevel = section.getInt("req.level");

        this.baseBonusPower = section.getInt("base.bonusPower");
        this.baseBonusDefense = section.getInt("base.bonusDefense");
        this.baseBonusSpeed = section.getInt("base.bonusSpeed");
        this.baseBonusIntelligence = section.getInt("base.bonusIntelligence");
        this.baseHealth = section.getInt("base.health");
        this.baseEarthDefense = section.getInt("base.earthDefense");
        this.baseFireDefense = section.getInt("base.fireDefense");
        this.baseWindDefense = section.getInt("base.windDefense");
        this.baseWaterDefense = section.getInt("base.waterDefense");

        this.bonusMeleePercent = section.getInt("stats.meleePercent");
        this.bonusMeleeNeutral = section.getInt("stats.meleeNeutral");
        this.bonusEarthDefense = section.getInt("stats.earthDefense");
        this.bonusFireDefense = section.getInt("stats.fireDefense");
        this.bonusWindDefense = section.getInt("stats.windDefense");
        this.bonusWaterDefense = section.getInt("stats.waterDefense");
        this.bonusEarthDamage = section.getInt("stats.earthDamage");
        this.bonusFireDamage = section.getInt("stats.fireDamage");
        this.bonusWindDamage = section.getInt("stats.windDamage");
        this.bonusWaterDamage = section.getInt("stats.waterDamage");
        this.bonusHealthRegen = section.getInt("stats.healthRegen");
        this.bonusHealth = section.getInt("stats.health");
        this.bonusWalkSpeed = section.getInt("stats.walkSpeed");

        this.id = id;
        this.name = section.getString("name");
        this.type = section.getString("type");
        this.texture = section.getString("texture");
        this.color = section.getString("color");
        this.tier = section.getString("tier");
        this.lore = section.getStringList("lore");
    }

    public ItemStack roll() {
        ItemStack itemStack = getItem();
        // stage 1: roll it
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagCompound rpg = new NBTTagCompound();
        rpg.set("meleePercent", new NBTTagInt(getRollFor(bonusMeleePercent)));
        rpg.set("meleeNeutral", new NBTTagInt(getRollFor(bonusMeleeNeutral)));
        rpg.set("earthDefense", new NBTTagInt(getRollFor(bonusEarthDefense)));
        rpg.set("fireDefense", new NBTTagInt(getRollFor(bonusFireDefense)));
        rpg.set("windDefense", new NBTTagInt(getRollFor(bonusWindDefense)));
        rpg.set("waterDefense", new NBTTagInt(getRollFor(bonusWaterDefense)));
        rpg.set("earthDamage", new NBTTagInt(getRollFor(bonusEarthDamage)));
        rpg.set("fireDamage", new NBTTagInt(getRollFor(bonusFireDamage)));
        rpg.set("windDamage", new NBTTagInt(getRollFor(bonusWindDamage)));
        rpg.set("waterDamage", new NBTTagInt(getRollFor(bonusWaterDamage)));
        rpg.set("healthRegen", new NBTTagInt(getRollFor(bonusHealthRegen)));
        rpg.set("health", new NBTTagInt(getRollFor(bonusHealth)));
        rpg.set("walkSpeed", new NBTTagInt(getRollFor(bonusWalkSpeed)));
        compound.set("RPG", rpg);
        nmsStack.setTag(compound);
        itemStack = CraftItemStack.asBukkitCopy(nmsStack);
        // stage 2: show stats on item
        List<String> lore = new ArrayList<>();
        // base stats
        lore.add("\u00a74HP " + nToString(baseHealth));



        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private int getRollFor(int value) {
        if (value > 0) {
            return(int) (positiveMin + (positiveMax - positiveMin + 1) * Math.random());
        } else {
            return (int) (negativeMin + (negativeMax - negativeMin + 1) * Math.random());
        }
    }

    private String nToString(int n) {
        if (n > 0) {
            return "+" + n;
        } else {
            return "" + n;
        }
    }

    private ItemStack getItem() {
        String itemType = (texture + "_" + type).toUpperCase();
        ItemStack itemStack = new ItemStack(Material.valueOf(itemType));
        ItemMeta meta = itemStack.getItemMeta();
        if (texture.equals("leather")) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            int color = Integer.parseInt(this.color, 16);
            leatherMeta.setColor(Color.fromRGB(color));
        }
        meta.setDisplayName(tierPrefix.get(tier) + name);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
