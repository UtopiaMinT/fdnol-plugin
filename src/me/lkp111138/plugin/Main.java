package me.lkp111138.plugin;

import me.lkp111138.plugin.commands.CommandTest;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("test").setExecutor(new CommandTest());
    }

    @Override
    public void onDisable() {

    }
}
