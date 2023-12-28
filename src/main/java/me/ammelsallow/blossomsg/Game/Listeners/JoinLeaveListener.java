package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class JoinLeaveListener implements Listener {
    BlossomSG plugin;
    public JoinLeaveListener(BlossomSG p){
        this.plugin = p;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        World world = player.getWorld();
        if(world.getName().equals("world")) {
            Location location = new Location(world, 14.5, 108 ,24.5);
            player.teleport(location);
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(0, CustomItems.getCompassSelector());
        }
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player p = event.getPlayer();
        if(p.getWorld().getName().equals("sg4")){
            if(this.plugin.getEmptyGame() == null){
                return;
            }
            if(!this.plugin.getEmptyGame().getPlayers().contains(p)){
                return;
            }
            this.plugin.getEmptyGame().getGameQueueHandler().leave(p);
        }
    }
}