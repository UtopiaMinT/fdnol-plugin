package me.lkp111138.plugin.npc;

import me.lkp111138.plugin.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.MetadataValue;

public class NPCEventListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        NPC npc = extractFromEntity(entity);
        if (npc != null) {
            event.setCancelled(true);
            if (event.getHand() == EquipmentSlot.HAND) {
                npc.getHandler().onInteract(event, npc);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        NPC npc = extractFromEntity(event.getEntity());
        if (npc != null) {
            event.setCancelled(true);
        }
    }

    private NPC extractFromEntity(Entity entity) {
        if (entity instanceof Villager) {
            for (MetadataValue value : entity.getMetadata("npc")) {
                if (value.getOwningPlugin().equals(Main.getInstance())) {
                    return (NPC) value.value();
                }
            }
        }
        return null;
    }
}
