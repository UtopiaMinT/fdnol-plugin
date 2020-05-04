package me.lkp111138.plugin.rpg.items;

import me.lkp111138.plugin.rpg.Stats;
import me.lkp111138.plugin.rpg.mob.CustomMob;
import me.lkp111138.plugin.util.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;

import static me.lkp111138.plugin.rpg.Stats.WEAPON_COOLDOWN;

public class RpgItemEventListener implements Listener {
    @EventHandler
    public void onPlayerAttack(PlayerAnimationEvent event) {
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            event.setCancelled(onPlayerAttack(event.getPlayer()));
        }
    }

    private boolean onPlayerAttack(Player player) {
        // set cooldown on weapon
        Stats stats = Stats.extractFromEntity(player);
        if (stats == null) {
            return true; // hit with no stats?
        }
        Build build = stats.getBuild();
        ItemStack item = build.getWeapon();
        if (item != null) {
            int attackSpeed = Math.max(0, Math.min(6, build.getBaseAttackSpeed() + build.getBonusAttackSpeed()));
            if (!NMSUtil.trySetItemCooldown(player, item.getType(), WEAPON_COOLDOWN[attackSpeed])) {
                return true;
            }
        }
        // all mobs within 3.5 blocks and a 30 degree cone
        Location playerLoc = player.getLocation();
        Vector playerDir = player.getLocation().getDirection();
        Collection<Entity> entities = player.getWorld().getNearbyEntities(playerLoc, 4, 4, 4);
        for (Entity entity : entities) {
            if (entity == player || CustomMob.extractFromEntity(entity) == null) {
                // lets not self harm
                continue;
            }
            Vector entityLoc = entity.getLocation().toVector();
            entityLoc.add(new Vector(0, entity.getHeight() / 2 - 0.9, 0));
            Vector entityDir = entityLoc.clone().add(player.getLocation().toVector().multiply(-1)).normalize();
            double crossSq = playerDir.clone().crossProduct(entityDir).lengthSquared();
            double distSq = playerDir.distanceSquared(entityDir);
            if (crossSq < 0.25) { // sin 30deg = 0.5
                if (distSq < 1) { // we also don't want to hit mobs behind us
                    EntityDamageByEntityEvent e = new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.CUSTOM, 1);
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2, 0));
                    Bukkit.getPluginManager().callEvent(e);
                }
            }
        }
        return false;
    }
}
