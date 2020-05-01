package me.lkp111138.plugin;

import me.lkp111138.plugin.rpg.Stats;
import me.lkp111138.plugin.rpg.damage.ElementalDamageRange;
import me.lkp111138.plugin.rpg.defense.ElementalDefense;
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
        ElementalDamageRange range = new ElementalDamageRange();
        range.minNeutral = 20;
        range.maxNeutral = 40;
        stats.setDamage(range);
        stats.setElementalDefense(new ElementalDefense());
        joined.setMetadata("rpg", new FixedMetadataValue(Main.getInstance(), stats));
    }
}
