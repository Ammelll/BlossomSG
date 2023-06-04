package me.ammelsallow.blossomsg.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game;
import me.ammelsallow.blossomsg.PlayerKitSelection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuListener implements Listener {
    private BlossomSG plugin;
    public MenuListener(BlossomSG _plugin){
        this.plugin = _plugin;
    }
    private final String MAIN_MENU = ChatColor.GOLD + "Game Selector";
    private final String sgPrefix = ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "Blossom" + ChatColor.DARK_GRAY + "] ";
    @EventHandler
    public void onMenuClick(InventoryClickEvent e){
        Inventory inventory = e.getInventory();
        if(e.getClickedInventory() == null){
            return;
        }
        if(e.getInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "" + ChatColor.BOLD + "Kit Selector")) {
            e.setCancelled(true);
            if(e.getCurrentItem() == null){
                return;
            }
            if(e.getCurrentItem().getType() == Material.ENDER_PEARL){
                inventory.setItem(3,addGlow(inventory,3));
                Player p = (Player) e.getWhoClicked();
                PlayerKitSelection.selectedKit.put(p.getUniqueId(), "ender");

            }
            else if(e.getCurrentItem().getType() == Material.BOW){
                inventory.setItem(4,addGlow(inventory,4));
                Player p = (Player) e.getWhoClicked();
                PlayerKitSelection.selectedKit.put(p.getUniqueId(), "robin");

            }
            else if(e.getCurrentItem().getType() == Material.FIREBALL){
                inventory.setItem(5,addGlow(inventory,5));
                Player p = (Player) e.getWhoClicked();
                PlayerKitSelection.selectedKit.put(p.getUniqueId(), "nether");
            }
        } else if(e.getClickedInventory().getTitle().equalsIgnoreCase(MAIN_MENU)){
            Player player = (Player) e.getWhoClicked();
            switch(e.getCurrentItem().getType()){
                case DIAMOND_SWORD:
                    Location location = new Location(Bukkit.getWorld("sg4"),0,50,0);
                    player.teleport(location);
                    Inventory inv = player.getInventory();
                    inv.clear();
                    if(plugin.getEmptyGame() != null){
                        Game game = plugin.getEmptyGame();
                        game.join(player);
                    } else {
                        Game game = new Game(plugin,4,"1");
                        plugin.addGame(game);
                        game.join(player);
                    }
                    break;
            }
            e.setCancelled(true);
        }
    }
    public static ItemStack addGlow(Inventory inventory, int index){
        ItemStack item = inventory.getItem(index);
        for(int i = 0; i < 9; i++){
            if(i != index && inventory.getItem(i) != null){
                inventory.setItem(i,removeGlow(inventory,i));
            }
        }
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addEnchant(Enchantment.SILK_TOUCH,1,false);
        item.setItemMeta(itemMeta);
        return item;
    }
    public static ItemStack removeGlow(Inventory inventory, int index){
        ItemStack item = inventory.getItem(index);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.removeEnchant(Enchantment.SILK_TOUCH);
        item.setItemMeta(itemMeta);
        return item;
    }
}
