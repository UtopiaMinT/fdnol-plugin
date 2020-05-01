package me.lkp111138.plugin.rpg;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CustomMobEventListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (Stats.extractFromEntity(entity) != null) {
            switch (event.getCause()) {
                case CUSTOM:
                    // do nothing
                    entity.setLastDamageCause(null);
                    break;
                case ENTITY_ATTACK:
                    event.setDamage(0.00001); // so that we still get knockback
                    break;
                case FALL:
                    // player fall damage
                    if (entity instanceof Player) {
                        Stats stats = Stats.extractFromEntity(entity);
                        if (stats != null) {
                            if (event.getDamage() > 3) {
                                stats.damagePercent(2.5 * (event.getDamage() - 3));
                            }
                        }
                    }
                default:
                    // cancel all others
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();
        Stats damagerStat = Stats.extractFromEntity(damager);
        Stats damageeStat = Stats.extractFromEntity(damagee);
        if (damagerStat == null || damageeStat == null) {
            return;
        }
        if (!damageeStat.damage(damagerStat.getDamage())) {
            if (damagee instanceof LivingEntity) {
                if (!(damagee instanceof Player)) {
                    ((LivingEntity) damagee).damage(999999);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Stats stats = Stats.extractFromEntity(entity);
        if (stats != null) {
            event.setCancelled(true);
            if (event.getHand() == EquipmentSlot.HAND) {
                event.getPlayer().sendMessage(String.valueOf(stats.getHealth()));
            }
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        e.setDroppedExp(0);
        Entity entity = e.getEntity();
        Stats stats = Stats.extractFromEntity(entity);
        if (stats != null) {
            stats.setShowBar(false);
        }
    }
}
