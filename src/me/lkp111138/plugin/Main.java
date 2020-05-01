package me.lkp111138.plugin;

import me.lkp111138.plugin.command.CommandCustomItem;
import me.lkp111138.plugin.command.CommandNpc;
import me.lkp111138.plugin.command.CommandTest;
import me.lkp111138.plugin.listener.MyListener;
import me.lkp111138.plugin.model.CustomItem;
import me.lkp111138.plugin.npc.NPC;
import me.lkp111138.plugin.npc.NPCEventListener;
import me.lkp111138.plugin.npc.TrackingTask;
import me.lkp111138.plugin.task.HealthBarTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        FileConfiguration config = this.getConfig();
        config.set("cute", true);
        // npc
        List<Map<?, ?>> npcSection = config.getMapList("npc");
        npcSection.forEach(map -> {
            NPC npc = new NPC(map);
            if ((Boolean) ((Map) map).getOrDefault("spawned", false)) {
                System.out.println(String.format("Spawning NPC #%d at %s %s", npc.getId(), npc.getLocation(), npc.spawn()));
            }
        });
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TrackingTask(), 0, 5);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new HealthBarTask(), 0, 2);
        // custom items
        ConfigurationSection itemSection = config.getConfigurationSection("item");
        for (String key : itemSection.getKeys(false)) {
            new CustomItem(key, itemSection.getConfigurationSection(key));
        }
        saveConfig();

        this.getCommand("fdnol").setExecutor(new CommandTest());
        this.getCommand("customitem").setExecutor(new CommandCustomItem());
        this.getCommand("npc").setExecutor(new CommandNpc());
        this.getServer().getPluginManager().registerEvents(new MyListener(), this);
        this.getServer().getPluginManager().registerEvents(new NPCEventListener(), this);
    }

    @Override
    public void onDisable() {
        NPC.removeAll();
        System.out.println(getConfig().getKeys(false));
        System.out.println(getConfig().getConfigurationSection("item"));
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }
}
