package me.lkp111138.plugin.listener;

import me.lkp111138.plugin.Main;
import me.lkp111138.plugin.model.PlayerStats;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();
        joined.sendMessage("\u00a76Five demands, not one less.");
        joined.setCollidable(false);
        PlayerStats stats = new PlayerStats();
        stats.setMaxHealth(100);
        stats.heal(100);
        stats.setHealthRegen(1);
        joined.setMetadata("rpg", new FixedMetadataValue(Main.getInstance(), stats));
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
            if (entity instanceof Player) {
                PlayerStats stats = PlayerStats.extractFromPlayer((Player) entity);
                if (stats != null) {
                    if (event.getDamage() > 3) {
                        stats.damagePercent(2.5 * (event.getDamage() - 3));
                    }
                }
            }
        }
    }
}
