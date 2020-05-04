package me.lkp111138.plugin.rpg.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class RpgItemEventListener implements Listener {
//    @EventHandler(ignoreCancelled = true)
//    public void onInventoryEquip(InventoryClickEvent event) {
//        HumanEntity whoClicked = event.getWhoClicked();
//        if (whoClicked instanceof Player) {
//            Player player = (Player) whoClicked;
//
//            boolean shift = event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
//            boolean mouse = event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.MIDDLE;
//            boolean drop = event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP;
//            boolean num = event.getClick() == ClickType.NUMBER_KEY;
//
//            boolean quickBar = event.getSlotType() == InventoryType.SlotType.QUICKBAR;
//            boolean container = event.getSlotType() == InventoryType.SlotType.CONTAINER;
//            boolean armour = event.getSlotType() == InventoryType.SlotType.ARMOR;
//
//            int rawSlot = event.getRawSlot();
//            boolean alreadyFilled = rawSlot >= 0 && isArmour(player.getInventory().getItem(rawSlot));
//
//            System.out.println("currentItem: " + event.getCurrentItem().getType());
//            System.out.println("cursor: " + event.getCursor().getType());
//            System.out.println("slot 0: " + event.getClickedInventory().getItem(0));
//            System.out.println("slot slot: " + event.getClickedInventory().getItem(event.getSlot()));
////            System.out.println("slot rawSlot: " + event.getClickedInventory().getItem(event.getRawSlot()));
//            System.out.println("slot: " + event.getSlot());
//            System.out.println("rawSlot: " + event.getRawSlot());
//            System.out.println("slotType: " + event.getSlotType());
//            System.out.println("click: " + event.getClick());
//            // TODO handle unequip
//            if (isArmour(event.getCurrentItem())) {
//                if (shift) {
//                    if ((quickBar || container) && !alreadyFilled) {
//                        // shift click
//                        event.setCancelled(onEquip(player, event.getCurrentItem(), armourType(event.getCurrentItem())));
//                        return;
//                    }
//                    if (armour) {
//                        // shift click
//                        event.setCancelled(onEquip(player, null, Stats.SLOTS_REVERSE.get(rawSlot)));
//                    }
//                }
//                if (armour && (mouse || drop)) {
//                    // manual unequip
//                    event.setCancelled(onEquip(player, null, Stats.SLOTS_REVERSE.get(rawSlot)));
//                }
//            }
//            if (isArmour(event.getCursor())) {
//                if (mouse) {
//                    if (armour) {
//                        // manual place
//                        event.setCancelled(onEquip(player, event.getCursor(), armourType(event.getCursor())));
//                        return;
//                    }
//                }
//            }
//            if (num && armour) {
//                // hotbar swap
//                event.setCancelled(onEquip(player, event.getClickedInventory().getItem(event.getHotbarButton()), armourType(event.getRawSlot())));
//            }
//        }
//    }

//    @EventHandler
//    public void onHotbarDrop(PlayerDropItemEvent event) {
//        Player player = event.getPlayer();
//        Stats stats = Stats.extractFromEntity(player);
//        if (stats != null && event.getItemDrop().getItemStack().equals(stats.getBuild().getWeapon())) {
//            onEquip(player, new ItemStack(Material.PISTON_MOVING_PIECE), "weapon");
//        }
//    }
//
//    @EventHandler
//    public void onInventoryDrag(InventoryDragEvent event) {
//        HumanEntity whoClicked = event.getWhoClicked();
//        boolean armour = isArmour(event.getNewItems().get(5)) || isArmour(event.getNewItems().get(6)) || isArmour(event.getNewItems().get(7)) || isArmour(event.getNewItems().get(8));
//        if (whoClicked instanceof Player) {
//            if (armour) {
//                // just disallow
//                event.setCancelled(true);
//            }
//        }
//    }

//    @EventHandler
//    public void onHotbarRightClick(PlayerInteractEvent event) {
//        if (isArmour(event.getItem()) && ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))) {
//            String type = armourType(event.getItem());
//            ItemStack currentEquipped = null;
//            if (type != null) {
//                switch (type) {
//                    case "helmet":
//                        currentEquipped = event.getPlayer().getInventory().getHelmet();
//                        break;
//                    case "chestplate":
//                        currentEquipped = event.getPlayer().getInventory().getChestplate();
//                        break;
//                    case "leggings":
//                        currentEquipped = event.getPlayer().getInventory().getLeggings();
//                        break;
//                    case "boots":
//                        currentEquipped = event.getPlayer().getInventory().getBoots();
//                        break;
//                }
//            }
//            System.out.println(currentEquipped);
//            if (!isArmour(currentEquipped)) {
//                event.setCancelled(onEquip(event.getPlayer(), event.getItem(), armourType(event.getItem())));
//            }
//        }
//    }

    @EventHandler
    public void onHotbarSwitch(PlayerItemHeldEvent event) {
//        onEquip(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()), "weapon");
    }

//    private boolean onEquip(Player player, ItemStack item, String slot) {
//        Stats stats = Stats.extractFromEntity(player);
//        if (stats == null) {
//            return true;
//        }
//        if (item == null || item.getType() == Material.AIR) {
//            if (!slot.equals("weapon")) {
//                stats.unequip(slot);
//            } else {
//                // anything not air/null works
//                stats.equip(new ItemStack(Material.PISTON_MOVING_PIECE), slot);
//            }
//        } else {
//            String error = stats.equip(item, slot);
//            if (error != null) {
//                player.sendMessage("\u00a7c" + error);
//                return true;
//            }
//        }
//        return false;
//    }

    private boolean isArmour(ItemStack item) {
        if (item == null) {
            return false;
        }
        switch (item.getType()) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
            case GOLD_HELMET:
            case GOLD_CHESTPLATE:
            case GOLD_LEGGINGS:
            case GOLD_BOOTS:
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
                return true;
            default:
                return false;
        }
    }

    private String armourType(ItemStack item) {
        if (item == null) {
            return null;
        }
        switch (item.getType()) {
            case LEATHER_HELMET:
            case CHAINMAIL_HELMET:
            case GOLD_HELMET:
            case IRON_HELMET:
            case DIAMOND_HELMET:
                return "helmet";
            case LEATHER_CHESTPLATE:
            case CHAINMAIL_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case IRON_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
                return "chestplate";
            case LEATHER_LEGGINGS:
            case CHAINMAIL_LEGGINGS:
            case GOLD_LEGGINGS:
            case IRON_LEGGINGS:
            case DIAMOND_LEGGINGS:
                return "leggings";
            case LEATHER_BOOTS:
            case CHAINMAIL_BOOTS:
            case GOLD_BOOTS:
            case IRON_BOOTS:
            case DIAMOND_BOOTS:
                return "boots";
            default:
                return null;
        }
    }

    private String armourType(int rawSlot) {
        switch (rawSlot) {
            case 5:
                return "helmet";
            case 6:
                return "chestplate";
            case 7:
                return "leggings";
            case 8:
                return "boots";
            default:
                return null;
        }
    }
}
