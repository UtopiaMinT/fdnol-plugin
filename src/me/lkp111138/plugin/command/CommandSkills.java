package me.lkp111138.plugin.command;

import me.lkp111138.plugin.Util;
import me.lkp111138.plugin.chestgui.ChestGui;
import me.lkp111138.plugin.rpg.Stats;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
        int strengthSkill = stats.getRawPowerSkill();
        int bonusStrengthSkill = stats.getBuild().getBaseBonusPower();
        ItemStack strengthBook = skillBook("Strength", strengthSkill, bonusStrengthSkill, stats.getFreeSkill());
        gui.set(0, new ChestGui.Slot(strengthBook, event -> onSkillBookClick(event, 0, stats, gui, player)));

        int defenseSkill = stats.getRawDefenseSkill();
        int bonusDefenseSkill = stats.getBuild().getBaseBonusDefense();
        ItemStack defenseBook = skillBook("Defense", defenseSkill, bonusDefenseSkill, stats.getFreeSkill());
        gui.set(1, new ChestGui.Slot(defenseBook, event -> onSkillBookClick(event, 1, stats, gui, player)));

        int speedSkill = stats.getRawSpeedSkill();
        int bonusSpeedSkill = stats.getBuild().getBaseBonusSpeed();
        ItemStack speedBook = skillBook("Speed", speedSkill, bonusSpeedSkill, stats.getFreeSkill());
        gui.set(2, new ChestGui.Slot(speedBook, event -> onSkillBookClick(event, 2, stats, gui, player)));

        int intelligenceSkill = stats.getRawIntelligenceSkill();
        int bonusIntelligenceSkill = stats.getBuild().getBaseBonusIntelligence();
        ItemStack intelligenceBook = skillBook("Intelligence", intelligenceSkill, bonusIntelligenceSkill, stats.getFreeSkill());
        gui.set(3, new ChestGui.Slot(intelligenceBook, event -> onSkillBookClick(event, 3, stats, gui, player)));
    }

    private void onSkillBookClick(InventoryClickEvent event, int i, Stats stats, ChestGui gui, Player player) {
        int p = 1;
        if (event.getClick() == ClickType.RIGHT) {
            p = Math.min(5, stats.getFreeSkill());
        }
        int[] _p = new int[4];
        _p[i] = p;
        stats.allocate(_p[0], _p[1], _p[2], _p[3]);
        reset(gui, stats, player);
        gui.rename(player, "Skills - " + stats.getFreeSkill() + " Points remaining");
    }

    private ItemStack skillBook(String skill, int skillCount, int bonusSkill, int freeSkill) {
        ItemStack book = new ItemStack(freeSkill > 0 ? Material.BOOK_AND_QUILL : Material.BOOK, Math.max(1, Math.min(64, skillCount)));
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("\u00a7r" + skill + " Skill: " + skillCount + " Points");
        int effectiveSkill = Math.max(0, Math.min(400, skillCount + bonusSkill));
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(String.format("\u00a77  %d (%s\u00a77) Points >> %d (%s\u00a77) Points  ", skillCount, Util.nToString(bonusSkill, true), skillCount + 1, Util.nToString(bonusSkill, true)));
        int count = lore.get(1).split(">>")[0].length() - 12;
        lore.add(String.format("\u00a77  %" + count + "s%4.2f%%    %4.2f%%", "", Stats.SKILL_TABLE[effectiveSkill], Stats.SKILL_TABLE[Math.min(400, effectiveSkill + 1)]));
        lore.add("");
        switch (skill) {
            case "Strength":
                lore.add("\u00a77- Increases damage dealt");
                lore.add("\u00a77- Increases the Earth damage you deal");
                break;
            case "Defense":
                lore.add("\u00a77- Decreases damage taken");
                lore.add("\u00a77- Increases the Fire damage you deal");
                break;
            case "Intelligence":
                lore.add("\u00a77- Increases mana efficiency");
                lore.add("\u00a77- Increases the Water damage you deal");
                break;
            case "Speed":
                lore.add("\u00a77- Increases dodge rate");
                lore.add("\u00a77- Increases the Wind damage you deal");
                break;
        }
        lore.add("");
        lore.add("\u00a77Left click to add 1 point, right click to add 5 points");
        meta.setLore(lore);
        book.setItemMeta(meta);
        return book;
    }
}
