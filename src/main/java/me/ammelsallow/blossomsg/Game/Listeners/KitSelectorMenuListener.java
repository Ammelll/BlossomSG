package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Kits.Kit;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.WorldLoading.Maps.SGMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitSelectorMenuListener implements Listener {
    private BlossomSG plugin;
    public KitSelectorMenuListener(BlossomSG _plugin){
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
            for(int i = 0; i < BlossomSG.KITS.length; i++){
                Kit kit = BlossomSG.KITS[i];
                if(e.getCurrentItem().getType() == kit.material){
                    inventory.setItem(i,addGlow(inventory,i));
                    Player p = (Player) e.getWhoClicked();
                    PlayerKitSelection.selectedKit.put(p.getUniqueId(), kit.stringID);
                }
            }

        } else if(e.getClickedInventory().getTitle().equalsIgnoreCase(MAIN_MENU)){
            Player player = (Player) e.getWhoClicked();
            switch(e.getCurrentItem().getType()){
                case DIAMOND_SWORD:
                    if(plugin.getEmptyGame() != null){
                        System.out.println(plugin.getEmptyGame().getMap().getName());
                        plugin.getEmptyGame().join(player);
                    } else{
                        Game game = new Game(plugin, SGMap.randomFrommPool());
                        plugin.addGame(game);
                        System.out.println("else " + game.getMap().getName());
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