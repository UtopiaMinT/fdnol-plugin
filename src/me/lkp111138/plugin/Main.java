package me.lkp111138.plugin;

import me.lkp111138.plugin.commands.CommandTest;
import me.lkp111138.plugin.listeners.MyListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("fdnol").setExecutor(new CommandTest());
        this.getServer().getPluginManager().registerEvents(new MyListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
