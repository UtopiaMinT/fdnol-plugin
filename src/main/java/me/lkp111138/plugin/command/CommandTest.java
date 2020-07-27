package me.lkp111138.plugin.command;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CommandTest implements CommandExecutor {
    private static String[] CODE = {"five", "demands", "not", "one", "less"};
    private static List<String> FIVE_DEMANDS = Arrays.asList(
            "\u00a7r\u00a771. Full withdrawal of the extradition bill",
            "\u00a7r\u00a772. An independent commission of inquiry into alleged police brutality",
            "\u00a7r\u00a773. Retracting the classification of protesters as “rioters”",
            "\u00a7r\u00a774. Amnesty for arrested protesters",
            "\u00a7r\u00a775. Dual universal suffrage, meaning for both the Legislative Council and the Chief Executive"
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == CODE.length) {
                boolean correct = true;
                for (int i = 0; i < CODE.length; i++) {
                    if (!args[i].equals(CODE[i])) {
                        correct = false;
                        break;
                    }
                }
                if (correct) {
                    ItemStack paper = new ItemStack(Material.PAPER);
                    ItemMeta meta = paper.getItemMeta();
                    // set the cyan colored name
                    meta.setDisplayName("\u00a7r\u00a7bThe Five Demands");
                    // set the lore
                    meta.setLore(FIVE_DEMANDS);
                    paper.setItemMeta(meta);
                    // give it 2 extra damage
                    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(paper);
                    NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
                    NBTTagList modifiers = new NBTTagList();
                    NBTTagCompound damage = new NBTTagCompound();
                    damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                    damage.set("Name", new NBTTagString("generic.attackDamage"));
                    damage.set("Amount", new NBTTagInt(2));
                    damage.set("Operation", new NBTTagInt(0));
                    damage.set("UUIDLeast", new NBTTagInt(894654));
                    damage.set("UUIDMost", new NBTTagInt(2872));
                    damage.set("Slot", new NBTTagString("mainhand"));
                    modifiers.add(damage);
                    compound.set("AttributeModifiers", modifiers);
                    // show only the lore
                    compound.set("HideFlags", new NBTTagInt(63));
                    nmsStack.setTag(compound);
                    paper = CraftItemStack.asBukkitCopy(nmsStack);

                    player.getInventory().addItem(paper);
                } else {
                    player.sendMessage("\u00a7cWrong code!");
                }
                return true;
            }
            return false;
        } else {
            System.out.println(sender);
            System.out.println(command);
            System.out.println(label);
            System.out.println(Arrays.toString(args));
            return true;
        }
    }
}
