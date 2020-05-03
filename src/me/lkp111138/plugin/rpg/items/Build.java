package me.lkp111138.plugin.rpg.items;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Build {
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    
    // base
    private int baseBonusPower;
    private int baseBonusDefense;
    private int baseBonusSpeed;
    private int baseBonusIntelligence;
    private int baseHealth;
    private int baseEarthDefense;
    private int baseFireDefense;
    private int baseWindDefense;
    private int baseWaterDefense;

    // bonuses
    private int bonusMeleePercent;
    private int bonusMeleeNeutral;
    private int bonusEarthDefense;
    private int bonusFireDefense;
    private int bonusWindDefense;
    private int bonusWaterDefense;
    private int bonusEarthDamage;
    private int bonusFireDamage;
    private int bonusWindDamage;
    private int bonusWaterDamage;
    private int bonusHealthRegen;
    private int bonusHealth;
    private int bonusWalkSpeed;

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        if (this.helmet != null) {
            adjustStats(this.helmet, -1);
        }
        adjustStats(helmet, 1);
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        if (this.chestplate != null) {
            adjustStats(this.chestplate, -1);
        }
        adjustStats(chestplate, 1);
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        if (this.leggings != null) {
            adjustStats(this.leggings, -1);
        }
        adjustStats(leggings, 1);
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        if (this.boots != null) {
            adjustStats(this.boots, -1);
        }
        adjustStats(boots, 1);
        this.boots = boots;
    }

    private void adjustStats(ItemStack item, int multiplier) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagCompound rpg = compound.getCompound("RPG");
        if (rpg == null) {
            return;
        }
        String itemId = rpg.getString("ITEM_ID");
        RpgItem rpgItem = RpgItem.get(itemId);
        if (rpgItem == null) {
            return;
        }
        // base
        baseBonusPower += multiplier * rpgItem.baseBonusPower;
        baseBonusDefense += multiplier * rpgItem.baseBonusDefense;
        baseBonusSpeed += multiplier * rpgItem.baseBonusSpeed;
        baseBonusIntelligence += multiplier * rpgItem.baseBonusIntelligence;
        baseHealth += multiplier * rpgItem.baseHealth;
        baseEarthDefense += multiplier * rpgItem.baseEarthDefense;
        baseFireDefense += multiplier * rpgItem.baseFireDefense;
        baseWindDefense += multiplier * rpgItem.baseWindDefense;
        baseWaterDefense += multiplier * rpgItem.baseWaterDefense;
        // bonus
        bonusMeleePercent += multiplier * (int) (rpgItem.bonusMeleePercent * rpg.getInt("MeleePercent") / 100.0);
        bonusMeleeNeutral += multiplier * (int) (rpgItem.bonusMeleeNeutral * rpg.getInt("MeleeNeutral") / 100.0);
        bonusEarthDefense += multiplier * (int) (rpgItem.bonusEarthDefense * rpg.getInt("EarthDefense") / 100.0);
        bonusFireDefense += multiplier * (int) (rpgItem.bonusFireDefense * rpg.getInt("FireDefense") / 100.0);
        bonusWindDefense += multiplier * (int) (rpgItem.bonusWindDefense * rpg.getInt("WindDefense") / 100.0);
        bonusWaterDefense += multiplier * (int) (rpgItem.bonusWaterDefense * rpg.getInt("WaterDefense") / 100.0);
        bonusEarthDamage += multiplier * (int) (rpgItem.bonusEarthDamage * rpg.getInt("EarthDamage") / 100.0);
        bonusFireDamage += multiplier * (int) (rpgItem.bonusFireDamage * rpg.getInt("FireDamage") / 100.0);
        bonusWindDamage += multiplier * (int) (rpgItem.bonusWindDamage * rpg.getInt("WindDamage") / 100.0);
        bonusWaterDamage += multiplier * (int) (rpgItem.bonusWaterDamage * rpg.getInt("WaterDamage") / 100.0);
        bonusHealthRegen += multiplier * (int) (rpgItem.bonusHealthRegen * rpg.getInt("HealthRegen") / 100.0);
        bonusHealth += multiplier * (int) (rpgItem.bonusHealth * rpg.getInt("Health") / 100.0);
        bonusWalkSpeed += multiplier * (int) (rpgItem.bonusWalkSpeed * rpg.getInt("WalkSpeed") / 100.0);
    }

    public int getBaseBonusPower() {
        return baseBonusPower;
    }

    public int getBaseBonusDefense() {
        return baseBonusDefense;
    }

    public int getBaseBonusSpeed() {
        return baseBonusSpeed;
    }

    public int getBaseBonusIntelligence() {
        return baseBonusIntelligence;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getBaseEarthDefense() {
        return baseEarthDefense;
    }

    public int getBaseFireDefense() {
        return baseFireDefense;
    }

    public int getBaseWindDefense() {
        return baseWindDefense;
    }

    public int getBaseWaterDefense() {
        return baseWaterDefense;
    }

    public int getBonusMeleePercent() {
        return bonusMeleePercent;
    }

    public int getBonusMeleeNeutral() {
        return bonusMeleeNeutral;
    }

    public int getBonusEarthDefense() {
        return bonusEarthDefense;
    }

    public int getBonusFireDefense() {
        return bonusFireDefense;
    }

    public int getBonusWindDefense() {
        return bonusWindDefense;
    }

    public int getBonusWaterDefense() {
        return bonusWaterDefense;
    }

    public int getBonusEarthDamage() {
        return bonusEarthDamage;
    }

    public int getBonusFireDamage() {
        return bonusFireDamage;
    }

    public int getBonusWindDamage() {
        return bonusWindDamage;
    }

    public int getBonusWaterDamage() {
        return bonusWaterDamage;
    }

    public int getBonusHealthRegen() {
        return bonusHealthRegen;
    }

    public int getBonusHealth() {
        return bonusHealth;
    }

    public int getBonusWalkSpeed() {
        return bonusWalkSpeed;
    }
}
