package me.lkp111138.plugin.rpg.items;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RpgItemEventListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onInventoryEquip(InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();
        if (whoClicked instanceof Player) {
            Player player = (Player) whoClicked;

            boolean shift = event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
            boolean mouse = event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.MIDDLE;
            boolean num = event.getClick() == ClickType.NUMBER_KEY;

            boolean quickBar = event.getSlotType() == InventoryType.SlotType.QUICKBAR;
            boolean container = event.getSlotType() == InventoryType.SlotType.CONTAINER;
            boolean armour = event.getSlotType() == InventoryType.SlotType.ARMOR;
            if (isArmour(event.getCurrentItem())) {
                if (shift) {
                    if (quickBar || container) {
                        // shift click
                        event.setCancelled(onEquip(player, event.getCurrentItem()));
                        return;
                    }
                }
            }
            if (isArmour(event.getCursor())) {
                if (mouse) {
                    if (armour) {
                        // manual place
                        event.setCancelled(onEquip(player, event.getCurrentItem()));
                        return;
                    }
                }
            }
            if (num && armour) {
                // hotbar swap
                event.setCancelled(onEquip(player, event.getCurrentItem()));
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();
        boolean armour = isArmour(event.getNewItems().get(5)) || isArmour(event.getNewItems().get(6)) || isArmour(event.getNewItems().get(7)) || isArmour(event.getNewItems().get(8));
        if (whoClicked instanceof Player) {
            Player player = (Player) whoClicked;
            if (armour) {
                event.setCancelled(onEquip(player, event.getCursor()));
            }
        }
    }

    @EventHandler
    public void onHotbarRightClick(PlayerInteractEvent event) {
        if (isArmour(event.getItem()) && ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))) {
            event.setCancelled(onEquip(event.getPlayer(), event.getItem()));
        }
    }

    private boolean onEquip(Player player, ItemStack item) {
        return true;
    }

    private boolean isArmour(ItemStack item) {
        if (item == null) {
            return false;
        }
        switch (item.getType()) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
            case GOLD_HELMET:
            case GOLD_CHESTPLATE:
            case GOLD_LEGGINGS:
            case GOLD_BOOTS:
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
                return true;
            default:
                return false;
        }
    }
}
