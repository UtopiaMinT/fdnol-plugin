package me.lkp111138.plugin.chestgui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ChestGuiEventListener implements Listener {
    @EventHandler
    public void onChestClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        HumanEntity _clicker = event.getWhoClicked();
        if (_clicker instanceof Player) {
            Player clicker = (Player) _clicker;
            ChestGui gui = ChestGui.extractFromEntity(clicker);
            if (gui != null) {
                event.setCancelled(true);
                if (gui.sameAs(clickedInventory)) {
                    ChestClickHandler handler = gui.getHandler(event.getRawSlot());
                    if (handler != null) {
                        handler.handle(event);
                    }
                }
            }
        }
    }
}
