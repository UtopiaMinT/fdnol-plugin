package me.lkp111138.plugin.rpg.mob;

import me.lkp111138.plugin.Main;
import me.lkp111138.plugin.rpg.Stats;
import me.lkp111138.plugin.rpg.damage.ElementalDamage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class CustomMobEventListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
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
        if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM && damager instanceof Player) {
            return;
        }
        if (damagee instanceof LivingEntity) {
            ElementalDamage meleeDamage = damagerStat.getMeleeDamage();
            double damageInHalfHearts = damageeStat.damage(meleeDamage);
            if (!(damagee instanceof Player)) {
                if (damageeStat.getHealth() > 0) {
                    ((LivingEntity) damagee).damage(0.0001);
                } else {
                    ((LivingEntity) damagee).damage(999999);
                    if (damager instanceof Player) {
                        CustomMob customMob = CustomMob.extractFromEntity(damagee);
                        if (!damageeStat.isXpAwarded()) {
                            damageeStat.setXpAwarded(true);
                            if (customMob != null) {
                                // TODO log all damagers and spread the xp
                                int xp = customMob.getXP();
                                damagerStat.addXP(xp);
                                Location loc = damagee.getLocation();
                                Vector adjustment = new Vector(0, damagee.getHeight() - 1.75, 0);
                                adjustment.add(new Vector(Math.random() - 0.5, Math.random() / 2, Math.random() - 0.5));

                                ArmorStand xpEntity = (ArmorStand) loc.getWorld().spawnEntity(loc.add(adjustment), EntityType.ARMOR_STAND);
                                xpEntity.setVisible(false);
                                xpEntity.setInvulnerable(true);
                                xpEntity.setCustomNameVisible(true);
                                xpEntity.setGravity(false);
                                xpEntity.setCustomName("+" + xp + " XP");
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), xpEntity::remove, 40);
                            }
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
