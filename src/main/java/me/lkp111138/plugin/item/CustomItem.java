package me.lkp111138.plugin.item;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class CustomItem {
    private String id;
    private String name;
    private Material material;
    private int damage;
    private List<String> lore;
    private ConfigurationSection enchantments;
    private int currencyValue;
    private int hideFlags;
    private boolean unbreakable;

    private static Map<String, CustomItem> itemRegistry = new HashMap<>();
    private static List<CustomItem> currencies = new ArrayList<>();

    public CustomItem(String id, ConfigurationSection m) {
        this.id = id;
        this.name = (String) m.get("name");
        String materialString = m.getString("material", "air");
        this.material = Material.getMaterial(materialString.toUpperCase());
        try {
            int material = Integer.parseInt(materialString);
            this.material = Material.getMaterial(material);
        } catch (NumberFormatException ignored) {}
        this.material.hashCode();
        this.damage = m.getInt("damage", 0);
        this.lore = m.getStringList("lore");
        this.currencyValue = m.getInt("currency_value", 0);
        this.hideFlags = m.getInt("hideflags", 0);
        this.enchantments = m.getConfigurationSection("enchantments");
        this.unbreakable = m.getBoolean("unbreakable", false);
        itemRegistry.put(id, this);
        if (currencyValue > 0) {
            int i = 0;
            for (int j = 0; j < currencies.size(); ++j) {
                if (currencies.get(i).currencyValue < currencyValue) {
                    break;
                }
            }
            currencies.add(i, this);
        }
    }

    public ItemStack getItemStack(int amount) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(amount);
        itemStack.setDurability((short) damage);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("\u00a7r" + name);
        if (lore != null) {
            List<String> lore = this.lore.stream().map(x -> "\u00a7r" + x).collect(Collectors.toList());
            meta.setLore(lore);
        }
        if (enchantments != null) {
            for (String key : enchantments.getKeys(false)) {
                Enchantment ench = Enchantment.getByName(key.toUpperCase());
                if (ench != null) {
                    meta.addEnchant(ench, enchantments.getInt(key), true);
                }
            }
        }
        meta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(meta);
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
        // show only the lore
        compound.set("HideFlags", new NBTTagInt(hideFlags));
        compound.set("CustomItemId", new NBTTagString(id));
        nmsStack.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public ItemStack getItemStack() {
        return getItemStack(1);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CustomItem getItem(String id) {
        return itemRegistry.get(id);
    }

    public static List<ItemStack> getCurrencyStacks(int value) {
        List<ItemStack> stacks = new ArrayList<>();
        ItemStack currentStack = null;
        Iterator<CustomItem> currencyIterator = currencies.iterator();
        while (value > 0 && currencyIterator.hasNext()) {
            CustomItem currentItem = currencyIterator.next();
            int count = value / currentItem.currencyValue;
            if (count == 0) {
                continue;
            }
            currentStack = currentItem.getItemStack(count);
            value -= count * currentItem.currencyValue;
            stacks.add(currentStack);
        }
        return stacks;
    }
}
