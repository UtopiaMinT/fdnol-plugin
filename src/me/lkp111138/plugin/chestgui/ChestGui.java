package me.lkp111138.plugin.chestgui;

import me.lkp111138.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.Arrays;
import java.util.Objects;

public class ChestGui {
    private String name;
    private final Slot[] slots;
    private Inventory inventory;
    private boolean open;

    public ChestGui(String name, int size) {
        this.name = name;
        if (size % 9 != 0) {
            throw new RuntimeException("size % 9 != 0");
        }
        this.slots = new Slot[size];
        for (int i = 0; i < size; i++) {
            this.slots[i] = new Slot(new ItemStack(Material.AIR), event -> {
                event.getWhoClicked().sendMessage(event.getClick().toString());
            });
        }
    }

    public void set(int i, Slot item) {
        slots[i] = item;
        inventory.setItem(i, slots[i].itemStack);
    }

    public void set(int i, ItemStack itemStack) {
        set(i, new Slot(itemStack, slots[i].handler));
    }

    public ChestClickHandler getHandler(int i) {
        return slots[i].handler;
    }

    public void open(Player player) {
        inventory = Bukkit.getServer().createInventory(null, slots.length, name);
        inventory.setContents(Arrays.stream(slots).map(x -> x.itemStack).toArray(ItemStack[]::new));
        player.setMetadata("chestgui", new FixedMetadataValue(Main.getInstance(), this));
        player.openInventory(inventory);
        open = true;
    }

    public void rename(Player player, String newName) {
        name = newName;
//        player.closeInventory();
        if (open) {
            open(player);
        }
    }

    public boolean sameAs(Inventory inventory) {
        return Objects.equals(inventory, this.inventory);
    }

    void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    public static class Slot {
        private final ItemStack itemStack;
        private final ChestClickHandler handler;

        public Slot(ItemStack itemStack, ChestClickHandler handler) {
            this.itemStack = itemStack;
            this.handler = handler;
        }
    }

    public static ChestGui extractFromEntity(Entity entity) {
        if (entity instanceof Player) {
            for (MetadataValue value : entity.getMetadata("chestgui")) {
                if (value.getOwningPlugin().equals(Main.getInstance())) {
                    return (ChestGui) value.value();
                }
            }
        }
        return null;
    }
}
