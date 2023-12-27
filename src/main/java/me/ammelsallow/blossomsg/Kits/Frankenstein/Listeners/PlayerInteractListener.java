package me.ammelsallow.blossomsg.Kits.Frankenstein.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Mobs.Frankenstein;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerInteractListener implements Listener {

    private BlossomSG plugin;

    public PlayerInteractListener(BlossomSG plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getItem() != null && e.getItem().equals(CustomItems.getFrankensteinEgg())){
                Frankenstein frankenstein = new Frankenstein(e.getClickedBlock().getLocation(),player,plugin);
                if(plugin.getGame(player)!= null){
                    plugin.getGame(player).addMob(player.getUniqueId(),frankenstein.getUniqueID());
                }
                frankenstein.spawn();
                player.setItemInHand(null);
                e.setCancelled(true);
            }
        }
        if(e.getItem() != null && e.getItem().isSimilar(CustomItems.getFrankensteinRoses())){
            Game g = plugin.getGame(player);
            if(g != null){
                UUID mobUUID = g.getMob(player.getUniqueId());
                for(LivingEntity entity :  player.getWorld().getLivingEntities()){
                    if(entity.getUniqueId() == mobUUID){
                        entity.teleport(player);
                        ((Frankenstein)(((CraftIronGolem) entity).getHandle())).evaluateTarget();
                        ItemStack roses = CustomItems.getFrankensteinRoses();
                        roses.setAmount(player.getItemInHand().getAmount()-1);
                        player.getInventory().setItemInHand(roses);
                        System.out.println("evaluated");
                    }
                }
            }
        }
    }
}
