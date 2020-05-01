package me.lkp111138.plugin.command;

import me.lkp111138.plugin.model.CustomItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCustomItem implements CommandExecutor {

    public CommandCustomItem() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            CustomItem item = CustomItem.getItem(args[0]);
            if (item != null) {
                ((Player) sender).getInventory().addItem(item.getItemStack());
            } else {
                sender.sendMessage("\u00a7cNo such item: " + args[0]);
            }
        } else {
            System.out.println("Cannot give item to console");
        }
        return true;
    }
}
