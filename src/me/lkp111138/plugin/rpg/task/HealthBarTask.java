package me.lkp111138.plugin.rpg.task;

import me.lkp111138.plugin.Util;
import me.lkp111138.plugin.rpg.Stats;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class HealthBarTask implements Runnable {
    @Override
    public void run() {
        List<Entity> entities = getServer().getWorld("world").getEntities();
        entities.forEach(_entity -> {
            if (_entity instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) _entity;
                Stats stats = Stats.extractFromEntity(entity);
                if (stats != null) {
                    if (entity instanceof Player) {
                        entity.setHealth(stats.getHealthHalfHearts());
                        ((CraftPlayer) entity).setMaxHealth(stats.getMaxHearts() * 2);
                        Player player = (Player) entity;
                        player.setFoodLevel(20);
                        Util.sendActionBar(player, String.format("\u00a74❤ %.0f / %.0f", stats.getHealth(), stats.getMaxHealth()));
                    }
                    stats.tick(entity.getLocation());
                }
            }
        });
    }
}
