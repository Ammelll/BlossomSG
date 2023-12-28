package me.ammelsallow.blossomsg.Kits.Shepherd.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Mobs.Frankenstein;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import net.minecraft.server.v1_8_R3.EntitySheep;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerInteractListenerShepherd implements Listener {

    private BlossomSG plugin;

    public PlayerInteractListenerShepherd(BlossomSG plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getItem() != null && e.getItem().isSimilar(CustomItems.getShepherdEggs())){
                Entity ent = player.getWorld().spawn(e.getClickedBlock().getLocation().add(0,1,0),Sheep.class);
                ent.setCustomName(ChatColor.LIGHT_PURPLE + player.getName() + "'s Sheep");
                ((Damageable) ent).setMaxHealth(20.0);
                ((Damageable) ent).setHealth(20.0);

                if(plugin.getGame(player)!= null){
                    plugin.getGame(player).addMob(player.getUniqueId(),ent.getUniqueId());
                }
                ItemStack newEggs = CustomItems.getShepherdEggs();
                newEggs.setAmount(player.getItemInHand().getAmount()-1);
                player.setItemInHand(newEggs);
                e.setCancelled(true);
            }
        }
    }
}
