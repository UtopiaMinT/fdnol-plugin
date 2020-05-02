package me.lkp111138.plugin.rpg.mob;

import me.lkp111138.plugin.rpg.Stats;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
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
                    event.setDamage(0.00000); // so that we still get knockback
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
        if (damagee instanceof LivingEntity) {
            double damageInHalfHearts = damageeStat.damage(damagerStat.getDamage());
            if (!(damagee instanceof Player)) {
                if (damageeStat.getHealth() > 0) {
                    ((LivingEntity) damagee).damage(0.0001);
                } else {
                    ((LivingEntity) damagee).damage(999999);
                    if (damager instanceof Player) {
                        CustomMob customMob = CustomMob.extractFromEntity(damagee);
                        if (customMob != null) {
                            // TODO log all damagers and spread the xp
                            damagerStat.addXP(customMob.getXP());
                        }
                    }
                }
            } else {
                ((LivingEntity) damagee).damage(damageInHalfHearts);
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

    // zombies no longer burn in the sun
    @EventHandler
    public void onMobCombust(EntityCombustEvent event) {
        if (event instanceof EntityCombustByEntityEvent) {return;}
        if (event instanceof EntityCombustByBlockEvent) {return;}
        event.setCancelled(true);
    }

    // mobs only target players
    @EventHandler
    public void onMobTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player)) {
            event.setCancelled(true);
        }
    }
}
