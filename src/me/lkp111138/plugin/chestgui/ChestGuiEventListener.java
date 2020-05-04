package me.lkp111138.plugin.chestgui;

import me.lkp111138.plugin.Main;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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

    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        Inventory closed = event.getInventory();
        HumanEntity _closer = event.getPlayer();
        if (_closer instanceof Player) {
            Player closer = (Player) _closer;
            ChestGui gui = ChestGui.extractFromEntity(closer);
            if (gui != null) {
                if (gui.sameAs(closed)) {
                    gui.setOpen(false);
                }
                closer.removeMetadata("chestgui", Main.getInstance());
            }
        }
    }
}
