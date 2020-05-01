package me.lkp111138.plugin.task;

import me.lkp111138.plugin.model.PlayerStats;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

import static org.bukkit.Bukkit.getServer;

public class HealthBarTask implements Runnable {
    @Override
    public void run() {
        Collection<? extends Player> onlinePlayers = getServer().getOnlinePlayers();
        onlinePlayers.forEach(player -> {
            PlayerStats stats = PlayerStats.extractFromPlayer(player);
            if (stats != null) {
                player.setHealth(stats.getHealthHalfHearts());
                ((CraftPlayer) player).setMaxHealth(stats.getMaxHearts() * 2);
                player.setFoodLevel(20);
                sendActionBar(player, String.format("\u00a74❤ %.0f / %.0f", stats.getHealth(), stats.getMaxHealth()));
                stats.tick();
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
