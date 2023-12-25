package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChestMenuGoldListener implements Listener {
    private BlossomSG plugin;
    public ChestMenuGoldListener(BlossomSG _plugin){
        this.plugin = _plugin;
    }
    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        if (e.getClickedInventory() == null) {
            return;
        }
        if (inventory.getType() == InventoryType.CHEST) {
            if (e.getCurrentItem().getType() == Material.GOLD_INGOT) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().matches(ChatColor.LIGHT_PURPLE + ""+ e.getCurrentItem().getAmount() + " Gold")) {
                    e.setCancelled(true);
                    inventory.setItem(e.getSlot(), new ItemStack(Material.AIR));
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.LEVEL_UP, 2f, 2f);
                    if (plugin.getGame((Player) e.getWhoClicked()) != null) {
                        plugin.getGame((Player) e.getWhoClicked()).addGold((Player) e.getWhoClicked(), e.getCurrentItem().getAmount());
                    }
                }
            }
        }
    }
}