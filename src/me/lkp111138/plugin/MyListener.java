package me.lkp111138.plugin;

import me.lkp111138.plugin.rpg.Stats;
import me.lkp111138.plugin.rpg.damage.ElementalDamageRange;
import me.lkp111138.plugin.rpg.defense.ElementalDefense;
import me.lkp111138.plugin.rpg.items.RpgItem;
import me.lkp111138.plugin.rpg.mob.CustomMob;
import me.lkp111138.plugin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();
        System.out.println(joined.getWalkSpeed());
        joined.sendMessage("\u00a76Five demands, not one less.");
        joined.setCollidable(false);
        joined.setInvulnerable(true);
        joined.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000, 3, true), true);
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection conn = Main.getInstance().getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("SELECT uuid, username, power_skill, defense_skill, speed_skill, intel_skill, free_skill, total_xp, deaths, health FROM player_stats WHERE uuid=?");
                stmt.setBytes(1, Util.getBytesFromUUID(joined.getUniqueId()));
                ResultSet rs = stmt.executeQuery();
                Stats stats = new Stats(joined);
                PlayerInventory inventory = joined.getInventory();
                inventory.setContents(Arrays.stream(inventory.getContents()).map(RpgItem::fixItem).toArray(ItemStack[]::new));
                if (!rs.next()) {
                    // new player
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                        stats.setMaxHealth(100);
                        stats.setHealthRegen(0);
                        ElementalDamageRange range = new ElementalDamageRange();
                        range.minNeutral = 1;
                        range.maxNeutral = 1;
                        stats.setDamage(range);
                        stats.setElementalDefense(new ElementalDefense());
                    }, 0);
                } else {
                    long totalXP = rs.getLong(8);
                    double health = rs.getDouble(10);
                    int power = rs.getInt(3);
                    int defense = rs.getInt(4);
                    int speed = rs.getInt(5);
                    int intelligence = rs.getInt(6);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                        stats.addXP(totalXP);
                        stats.setMaxHealth(100 + 10 * stats.getLevel());
                        stats.allocate(power, defense, speed, intelligence);
                        stats.resetBuild(true);
                        stats.heal(health);
                        stats.setHealthRegen(1.2);
                        ElementalDamageRange range = new ElementalDamageRange();
                        range.minNeutral = 20;
                        range.maxNeutral = 40;
                        stats.setDamage(range);
                        stats.setElementalDefense(new ElementalDefense());
                    }, 0);
                }
                joined.setMetadata("rpg", new FixedMetadataValue(Main.getInstance(), stats));
                joined.setInvulnerable(false);
                joined.removePotionEffect(PotionEffectType.INVISIBILITY);
            } catch (SQLException e) {
                e.printStackTrace();
                joined.kickPlayer("Sorry, there was an error");
            }
        });
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Stats stats = Stats.extractFromEntity(player);
        if (stats != null) {
            try {
                stats.save();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerAttack(PlayerAnimationEvent event) {
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            onPlayerAttack(event.getPlayer());
        }
    }

    @EventHandler
    public void onInterractWithArmorStand(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            // not cool equipping a stand with a helmet
            event.setCancelled(true);
        }
    }

    private boolean onPlayerAttack(Player player) {
        // all mobs within 3.5 blocks and a 30 degree cone
        Location playerLoc = player.getLocation();
        Vector playerDir = player.getLocation().getDirection();
        Collection<Entity> entities = player.getWorld().getNearbyEntities(playerLoc, 4, 4, 4);
        boolean hit = false;
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
                    hit = true;
                }
            }
        }
        return hit;
    }

    private void sendEquipErrorString(Player p, String s) {
        if (s == null) {
            return;
        }
        p.sendMessage("\u00a7c" + s);
    }
}
