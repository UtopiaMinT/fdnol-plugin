package me.lkp111138.plugin.command;

import me.lkp111138.plugin.model.CustomMob;
import me.lkp111138.plugin.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCustomMob implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        switch (args[0]) {
            case "spawn":
                if (sender instanceof Player) {
                    if (args.length > 1) {
                        try {
                            CustomMob mob = CustomMob.get(args[1]);
                            if (mob == null) {
                                sender.sendMessage("\u00a7cNo such mob: " + args[1]);
                                NPC.save();
                            } else {
                                mob.spawnNew(((Player) sender).getLocation());
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage("\u00a7cInvalid number: " + args[1]);
                        }
                    } else {
                        sender.sendMessage("\u00a7cUsage: /npc spawn <npc_id>");
                    }
                }
                break;
        }
        return true;
    }
}
