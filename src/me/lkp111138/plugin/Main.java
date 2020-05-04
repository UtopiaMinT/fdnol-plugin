package me.lkp111138.plugin;

import me.lkp111138.plugin.chestgui.ChestGuiEventListener;
import me.lkp111138.plugin.command.*;
import me.lkp111138.plugin.database.ConnectionPool;
import me.lkp111138.plugin.item.CustomItem;
import me.lkp111138.plugin.npc.NPC;
import me.lkp111138.plugin.npc.NPCEventListener;
import me.lkp111138.plugin.npc.task.TrackingTask;
import me.lkp111138.plugin.rpg.items.RpgItem;
import me.lkp111138.plugin.rpg.items.RpgItemEventListener;
import me.lkp111138.plugin.rpg.mob.CustomMob;
import me.lkp111138.plugin.rpg.mob.CustomMobEventListener;
import me.lkp111138.plugin.rpg.task.HealthBarTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {
    private static Main instance;
    private ConnectionPool pool;

    @Override
    public void onEnable() {
        instance = this;

        FileConfiguration config = this.getConfig();
        config.set("cute", true);
        String connString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&useSSL=false", config.get("db.host"), config.get("db.name"), config.get("db.user"), config.get("db.pass"));
        try {
            pool = new ConnectionPool(connString, 3, 10);
        } catch (SQLException e) {
            e.printStackTrace();
            // database init much succeed, or die
            getServer().shutdown();
            return;
        }
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
        // rpi items
        FileConfiguration rpgItemConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "rpgitems.yml"));
        RpgItem.init(rpgItemConfig.getConfigurationSection("config"));
        ConfigurationSection rpgItemSection = rpgItemConfig.getConfigurationSection("rpgitems");
        for (String key : rpgItemSection.getKeys(false)) {
            new RpgItem(key, rpgItemSection.getConfigurationSection(key));
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
        this.getCommand("skills").setExecutor(new CommandSkills());
        this.getCommand("rpgitem").setExecutor(new CommandRpgItem());

        // module listeners
        this.getServer().getPluginManager().registerEvents(new MyListener(), this);
        this.getServer().getPluginManager().registerEvents(new NPCEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new CustomMobEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChestGuiEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new RpgItemEventListener(), this);
    }

    @Override
    public void onDisable() {
        NPC.removeAll();
        // save all stats
        new HealthBarTask(true).run();
        if (pool != null) {
            // if we failed to initialize the pool, this won't run
            pool.shutdown();
            pool = null;
        }
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }
}
