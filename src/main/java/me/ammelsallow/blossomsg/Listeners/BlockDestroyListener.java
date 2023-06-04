package me.ammelsallow.blossomsg.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDestroyListener implements Listener {

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(!p.hasPermission("blossomsg.brockbreak")){
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hey, you cannot break blocks here!!");
        }
    }
}
