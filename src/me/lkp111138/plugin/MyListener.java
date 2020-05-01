package me.lkp111138.plugin;

import me.lkp111138.plugin.rpg.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();
        joined.sendMessage("\u00a76Five demands, not one less.");
        joined.setCollidable(false);
        Stats stats = new Stats(joined);
        stats.setMaxHealth(100);
        stats.fullHeal();
        stats.setHealthRegen(1.2);
        stats.setDamage(20, 40);
        joined.setMetadata("rpg", new FixedMetadataValue(Main.getInstance(), stats));
    }
}
