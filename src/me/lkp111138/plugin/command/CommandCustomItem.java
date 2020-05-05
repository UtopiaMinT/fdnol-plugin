package me.lkp111138.plugin.command;

import me.lkp111138.plugin.item.CustomItem;
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
            if (args.length > 0) {
                CustomItem item = CustomItem.getItem(args[0]);
                if (item != null) {
                    int amount = 1;
                    if (args.length > 1) {
                        amount = Integer.parseInt(args[1]);
                    }
                    ((Player) sender).getInventory().addItem(item.getItemStack(amount));
                } else {
                    sender.sendMessage("\u00a7cNo such item: " + args[0]);
                }
            } else {
                sender.sendMessage("\u00a7cUsage: /customitem <id> [amount]");
            }
        } else {
            sender.sendMessage("Cannot give item to console");
        }
        return true;
    }
}
