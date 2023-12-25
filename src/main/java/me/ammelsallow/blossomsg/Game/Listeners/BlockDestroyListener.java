package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlockDestroyListener implements Listener {
    private BlossomSG plugin;

    public BlockDestroyListener(BlossomSG plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block brokenBlock = e.getBlock();
        Material type = brokenBlock.getType();
        Location location = brokenBlock.getLocation();
        World world = location.getWorld();

        if(type == Material.LOG){
            brokenBlock.setType(Material.AIR);
            world.dropItemNaturally(location.add(0,0.5,0), new ItemStack(Material.STICK));
        }
        if(type == Material.IRON_ORE){
            brokenBlock.setType(Material.AIR);
            world.dropItemNaturally(location.add(0,0.5,0), new ItemStack(Material.IRON_INGOT));
            return;
        }
        if(type == Material.GOLD_ORE){
            brokenBlock.setType(Material.AIR);
            if(plugin.getGame(p) !=null) {
                plugin.getGame(p).addGold(p, 5);
            }
            p.sendMessage(ChatColor.GOLD + "+5 Gold");
            p.playSound(p.getLocation(), Sound.ORB_PICKUP,1f,1f);
            return;
        }
        if(type == Material.DIAMOND_ORE) {
            return;
        }
        if(type == Material.LAPIS_ORE) {
            return;
        }
        if(type == Material.COAL_ORE) {
            return;
        }

        if(!p.hasPermission("blossomsg.brockbreak")){
            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hey, you cannot break blocks here!!");
            e.setCancelled(true);
        }
    }
}