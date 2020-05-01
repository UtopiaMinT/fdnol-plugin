package me.lkp111138.plugin.task;

import me.lkp111138.plugin.model.PlayerStats;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
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
                PlayerStats stats = PlayerStats.extractFromEntity(entity);
                if (stats != null) {
                    if (entity instanceof Player) {
                        entity.setHealth(stats.getHealthHalfHearts());
                        ((CraftPlayer) entity).setMaxHealth(stats.getMaxHearts() * 2);
                        Player player = (Player) entity;
                        player.setFoodLevel(20);
                        sendActionBar(player, String.format("\u00a74❤ %.0f / %.0f", stats.getHealth(), stats.getMaxHealth()));
                        stats.tick();
                    } else {
                        // TODO show a bar above them
                    }
                }
            }
        });
    }

    @SuppressWarnings("rawtypes")
    private static void sendActionBar(Player player, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(ppoc);
    }
}
