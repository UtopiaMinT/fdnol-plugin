package me.lkp111138.plugin;

import me.lkp111138.plugin.rpg.Stats;
import me.lkp111138.plugin.rpg.damage.ElementalDamageRange;
import me.lkp111138.plugin.rpg.defense.ElementalDefense;
import me.lkp111138.plugin.rpg.items.RpgItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();
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
                if (!rs.next()) {
                    // new player
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                        stats.setMaxHealth(100);
                        stats.fullHeal();
                        stats.setHealthRegen(1.2);
                        ElementalDamageRange range = new ElementalDamageRange();
                        range.minNeutral = 20;
                        range.maxNeutral = 40;
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
                        sendEquipErrorString(joined, stats.equip(RpgItem.fixItem(joined.getInventory().getHelmet()), "helmet"));
                        sendEquipErrorString(joined, stats.equip(RpgItem.fixItem(joined.getInventory().getChestplate()), "chestplate"));
                        sendEquipErrorString(joined, stats.equip(RpgItem.fixItem(joined.getInventory().getLeggings()), "leggings"));
                        sendEquipErrorString(joined, stats.equip(RpgItem.fixItem(joined.getInventory().getBoots()), "boots"));
                        sendEquipErrorString(joined, stats.equip(RpgItem.fixItem(joined.getInventory().getItemInMainHand()), "weapon"));
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
        // TODO save data
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

    private void sendEquipErrorString(Player p, String s) {
        if (s == null) {
            return;
        }
        p.sendMessage("\u00a7c" + s);
    }
}
