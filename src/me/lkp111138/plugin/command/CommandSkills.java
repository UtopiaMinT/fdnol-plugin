package me.lkp111138.plugin.command;

import me.lkp111138.plugin.chestgui.ChestGui;
import me.lkp111138.plugin.rpg.Stats;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandSkills implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Stats stats = Stats.extractFromEntity(player);
            if (stats == null) {
                return true;
            }
//            ChestGui gui = new ChestGui("Skills - " + stats.getFreeSkill() + " Points remaining", 27);
            ChestGui gui = new ChestGui("Skills - " + stats.getFreeSkill() + " Points remaining", 9);
            reset(gui, stats, player);
            gui.open(player);
        }
        return true;
    }

    private void reset(ChestGui gui, Stats stats, Player player) {
        ItemStack resetItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = resetItem.getItemMeta();
        meta.setDisplayName("\u00a7cReset");
        gui.set(8, new ChestGui.Slot(resetItem, event -> {
            stats.resetSkills();
            reset(gui, stats, player);
            gui.rename(player, "Skills - " + stats.getFreeSkill() + " Points remaining");
        }));
        int strengthSkill = stats.getPowerSkill();
        ItemStack strengthBook = strengthBook(strengthSkill);
        gui.set(0, new ChestGui.Slot(strengthBook, event -> {
            int p = 1;
            if (event.getClick() == ClickType.RIGHT) {
                p = Math.min(5, stats.getFreeSkill());
            }
            stats.allocate(p, 0, 0, 0);
            reset(gui, stats, player);
            gui.rename(player, "Skills - " + stats.getFreeSkill() + " Points remaining");
        }));
        int defenseSkill = stats.getDefenseSkill();
        ItemStack defenseBook = defenseBook(defenseSkill);
        gui.set(1, new ChestGui.Slot(defenseBook, event -> {
            int p = 1;
            if (event.getClick() == ClickType.RIGHT) {
                p = Math.min(5, stats.getFreeSkill());
            }
            stats.allocate(0, p, 0, 0);
            reset(gui, stats, player);
            gui.rename(player, "Skills - " + stats.getFreeSkill() + " Points remaining");
        }));
        int speedSkill = stats.getSpeedSkill();
        ItemStack speedBook = speedBook(speedSkill);
        gui.set(2, new ChestGui.Slot(speedBook, event -> {
            int p = 1;
            if (event.getClick() == ClickType.RIGHT) {
                p = Math.min(5, stats.getFreeSkill());
            }
            stats.allocate(0, 0, p, 0);
            reset(gui, stats, player);
            gui.rename(player, "Skills - " + stats.getFreeSkill() + " Points remaining");
        }));
        int intelligenceSkill = stats.getIntelligenceSkill();
        ItemStack intelligenceBook = intelligenceBook(intelligenceSkill);
        gui.set(3, new ChestGui.Slot(intelligenceBook, event -> {
            int p = 1;
            if (event.getClick() == ClickType.RIGHT) {
                p = Math.min(5, stats.getFreeSkill());
            }
            stats.allocate(0, 0, 0, p);
            reset(gui, stats, player);
            gui.rename(player, "Skills - " + stats.getFreeSkill() + " Points remaining");
        }));
    }

    private ItemStack strengthBook(int strengthSkill) {
        ItemStack strengthBook = new ItemStack(Material.BOOK_AND_QUILL, Math.max(1, Math.min(64, strengthSkill)));
        ItemMeta strengthMeta = strengthBook.getItemMeta();
        strengthMeta.setDisplayName("\u00a7rStrength: " + strengthSkill + " Points");
        strengthBook.setItemMeta(strengthMeta);
        return strengthBook;
    }

    private ItemStack defenseBook(int defenseSkill) {
        ItemStack defenseBook = new ItemStack(Material.BOOK_AND_QUILL, Math.max(1, Math.min(64, defenseSkill)));
        ItemMeta defenseMeta = defenseBook.getItemMeta();
        defenseMeta.setDisplayName("\u00a7rDefense: " + defenseSkill + " Points");
        defenseBook.setItemMeta(defenseMeta);
        return defenseBook;
    }

    private ItemStack speedBook(int speedSkill) {
        ItemStack speedBook = new ItemStack(Material.BOOK_AND_QUILL, Math.max(1, Math.min(64, speedSkill)));
        ItemMeta speedMeta = speedBook.getItemMeta();
        speedMeta.setDisplayName("\u00a7rSpeed: " + speedSkill + " Points");
        speedBook.setItemMeta(speedMeta);
        return speedBook;
    }

    private ItemStack intelligenceBook(int intelligenceSkill) {
        ItemStack speedBook = new ItemStack(Material.BOOK_AND_QUILL, Math.max(1, Math.min(64, intelligenceSkill)));
        ItemMeta speedMeta = speedBook.getItemMeta();
        speedMeta.setDisplayName("\u00a7rIntelligence: " + intelligenceSkill + " Points");
        speedBook.setItemMeta(speedMeta);
        return speedBook;
    }
}
