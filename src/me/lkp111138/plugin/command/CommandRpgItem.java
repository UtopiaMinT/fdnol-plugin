package me.lkp111138.plugin.command;

import me.lkp111138.plugin.rpg.items.RpgItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandRpgItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (args[0]) {
                case "give":
                    if (args.length < 2) {
                        sender.sendMessage("\u00a7cUsage: /rpgitem give <item_id>");
                        break;
                    }
                    RpgItem rpgItem = RpgItem.get(args[1]);
                    if (rpgItem != null) {
                        player.getInventory().addItem(rpgItem.roll());
                    } else {
                        sender.sendMessage("\u00a7cNo such rpgItem: " + args[1]);
                    }
                    break;
                case "fix":
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemStack itemStack = RpgItem.fixItem(item);
                    if (itemStack != item) {
                        player.getInventory().setItemInMainHand(itemStack);
                    } else {
                        sender.sendMessage("\u00a7cThis is not a rpgItem");
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
