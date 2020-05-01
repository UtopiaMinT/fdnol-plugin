package me.lkp111138.plugin.command;

import me.lkp111138.plugin.npc.NPC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class CommandNpc implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }
        switch (args[0]) {
            case "get":
                if (args.length > 1) {
                    try {
                        NPC npc = NPC.get(Integer.parseInt(args[1]));
                        if (npc == null) {
                            sender.sendMessage("\u00a7cNo such NPC #" + args[1]);
                        } else {
                            sender.sendMessage("NPC #" + npc.getId());
                            sender.sendMessage("Name: " + npc.getName());
                            sender.sendMessage("Type: " + npc.getType());
                            sender.sendMessage("Location: " + npc.getLocation());
                            sender.sendMessage("Health: " + npc.getHealth() + "/" + npc.getMaxHealth());
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage("\u00a7cInvalid number: " + args[1]);
                    }
                } else {
                    sender.sendMessage("\u00a7cUsage: /npc get <npc_id>");
                }
                break;
            case "spawn":
                if (args.length > 1) {
                    try {
                        NPC npc = NPC.get(Integer.parseInt(args[1]));
                        if (npc == null) {
                            sender.sendMessage("\u00a7cNo such NPC #" + args[1]);
                            NPC.save();
                        } else {
                            if (!npc.spawn()) {
                                sender.sendMessage("\u00a7cNPC #" + args[1] + " already spawned");
                            }
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage("\u00a7cInvalid number: " + args[1]);
                    }
                } else {
                    sender.sendMessage("\u00a7cUsage: /npc spawn <npc_id>");
                }
                break;
            case "despawn":
            case "remove":
                if (args.length > 1) {
                    try {
                        NPC npc = NPC.get(Integer.parseInt(args[1]));
                        if (npc == null) {
                            sender.sendMessage("\u00a7cNo such NPC #" + args[1]);
                        } else {
                            npc.remove();
                            NPC.save();
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage("\u00a7cInvalid number: " + args[1]);
                    }
                } else {
                    sender.sendMessage("\u00a7cUsage: /npc spawn <npc_id>");
                }
                break;
            case "create":
                if (sender instanceof Player) {
                    Map m = new HashMap();
                    Location location = ((Player) sender).getLocation();
                    m.put("world", location.getWorld().getName());
                    m.put("x", location.getBlockX());
                    m.put("y", location.getBlockY());
                    m.put("z", location.getBlockZ());
                    m.put("name", "");
                    m.put("type", "nitwit");
                    NPC npc = new NPC(m);
                    npc.spawn();
                    sender.sendMessage("\u00a7aCreated NPC #" + npc.getId());
                    NPC.save();
                }
                break;
            case "set":
                if (args.length < 4) {
                    sender.sendMessage("\u00a7cUsage: /npc set <npc_id> <attribute> <value>");
                } else {
                    try {
                        NPC npc = NPC.get(Integer.parseInt(args[1]));
                        if (npc == null) {
                            sender.sendMessage("\u00a7cNo such NPC #" + args[1]);
                        } else {
                            switch (args[2]) {
                                case "location":
                                    String[] segments = args[3].split(",");
                                    if (segments.length < 4) {
                                        sender.sendMessage("\u00a7cUsage: /npc set <npc_id> location <world,x,y,z,[yaw,pitch]>");
                                        break;
                                    }
                                    Location loc = new Location(getServer().getWorld(segments[0]), Integer.parseInt(segments[1]) +.5f, Integer.parseInt(segments[2]), Integer.parseInt(segments[3]) + .5f);
                                    if (segments.length > 4) {
                                        loc.setYaw(Integer.parseInt(segments[4]));
                                    }
                                    if (segments.length > 5) {
                                        loc.setPitch(Integer.parseInt(segments[5]));
                                    }
                                    npc.setLocation(loc);
                                    break;
                                case "name":
                                    String[] _name = new String[args.length - 3];
                                    System.arraycopy(args, 3, _name, 0, _name.length);
                                    String name = String.join(" ", _name);
                                    npc.setName(name);
                                    break;
                                case "eye_contact":
                                    npc.setEyeContact(Boolean.parseBoolean(args[3]));
                                    break;
                                case "type":
                                    npc.setType(args[3]);
                                    break;
                                case "start_tracking_range":
                                    npc.setStartTrackingRange(Integer.parseInt(args[3]));
                                    break;
                                case "tracking_range":
                                    npc.setTrackingRange(Integer.parseInt(args[3]));
                                    break;
                            }
                            NPC.save();
                            sender.sendMessage("\u00a7aSuccess");
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage("\u00a7cInvalid number: " + args[1]);
                    }
                }
                break;
            case "save":
                NPC.save();
                break;
        }
        return true;
    }
}
