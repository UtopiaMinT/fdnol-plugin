package me.lkp111138.plugin.command;

import me.lkp111138.plugin.chestgui.ChestGui;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandChestGui implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ChestGui gui = new ChestGui("Test", 9);
            gui.set(4, new ChestGui.Slot(new ItemStack(Material.DIAMOND), null));
            gui.open(player);
        }
        return true;
    }
}
