package me.lkp111138.plugin;

import me.lkp111138.plugin.chestgui.ChestGuiEventListener;
import me.lkp111138.plugin.command.*;
import me.lkp111138.plugin.item.CustomItem;
import me.lkp111138.plugin.npc.NPC;
import me.lkp111138.plugin.npc.NPCEventListener;
import me.lkp111138.plugin.npc.task.TrackingTask;
import me.lkp111138.plugin.rpg.CustomMob;
import me.lkp111138.plugin.rpg.CustomMobEventListener;
import me.lkp111138.plugin.rpg.task.HealthBarTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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
        FileConfiguration npcConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "npc.yml"));
        List<Map<?, ?>> npcSection = npcConfig.getMapList("npc");
        npcSection.forEach(map -> {
            NPC npc = new NPC(map);
            if ((Boolean) ((Map) map).getOrDefault("spawned", false)) {
                System.out.println(String.format("Spawning NPC #%d at %s %s", npc.getId(), npc.getLocation(), npc.spawn()));
            }
        });
        // custom items
        FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "items.yml"));
        ConfigurationSection itemSection = itemConfig.getConfigurationSection("items");
        for (String key : itemSection.getKeys(false)) {
            new CustomItem(key, itemSection.getConfigurationSection(key));
        }
        // custom mobs
        FileConfiguration mobConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "mobs.yml"));
        ConfigurationSection mobSection = mobConfig.getConfigurationSection("mobs");
        for (String key : mobSection.getKeys(false)) {
            new CustomMob(key, mobSection.getConfigurationSection(key));
        }
        saveConfig();

        // repeating tasks
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TrackingTask(), 0, 5);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new HealthBarTask(), 0, 2);

        // commands
        this.getCommand("fdnol").setExecutor(new CommandTest());
        this.getCommand("customitem").setExecutor(new CommandCustomItem());
        this.getCommand("npc").setExecutor(new CommandNpc());
        this.getCommand("custommob").setExecutor(new CommandCustomMob());
        this.getCommand("chestgui").setExecutor(new CommandChestGui());

        // module listeners
        this.getServer().getPluginManager().registerEvents(new MyListener(), this);
        this.getServer().getPluginManager().registerEvents(new NPCEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new CustomMobEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChestGuiEventListener(), this);
    }

    @Override
    public void onDisable() {
        NPC.removeAll();
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }
}
