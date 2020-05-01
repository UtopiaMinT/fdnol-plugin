package me.lkp111138.plugin.npc;

import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface InteractionHandler {
    void onInteract(PlayerInteractEntityEvent event, NPC npc);
}
