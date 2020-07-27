package me.lkp111138.plugin.util;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

public class NMSUtil {
    /**
     * Sends text above the player's HUD, typically health info.
     * @param player the receiving player
     * @param message the text to send
     */
    @SuppressWarnings("rawtypes")
    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(ppoc);
    }

    /**
     * Try to set cooldown for a type of item for a player. Can fail due to active cooldown.
     * @param player The player to get the cooldown
     * @param type The type of item that needs a cooldown
     * @param ticks The number of ticks for the cooldown
     * @return true if there's no cooldown in effect while setting this cooldown, false otherwise.
     */
    public static boolean trySetItemCooldown(Player player, Material type, int ticks) {
        ItemCooldown tracker = ((CraftPlayer) player).getHandle().getCooldownTracker();
        Item item1 = CraftMagicNumbers.getItem(type);
        if (tracker.a(item1)) {
            return false; // still in cd
        }
        tracker.a(item1, ticks);
        return true;
    }
}
