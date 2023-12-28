package me.ammelsallow.blossomsg.Kits.Frankenstein.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Mobs.Frankenstein;
import me.ammelsallow.blossomsg.Kits.Cooldown;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.ChatColor;
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

    private Cooldown cooldown = new Cooldown(10000);

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
                    System.out.println("ADDED");
                }
                System.out.println(plugin.getGame(player));
                cooldown.use(player);
                frankenstein.spawn();
                player.setItemInHand(null);
                e.setCancelled(true);
            }
        }
        if(e.getItem() != null && e.getItem().isSimilar(CustomItems.getFrankensteinRoses())){
            Game g = plugin.getGame(player);
            if(g != null){
                for(UUID mobUUID : g.getMob(player.getUniqueId())){
                    for(LivingEntity entity :  player.getWorld().getLivingEntities()) {
                        if (entity.getUniqueId() == mobUUID && entity instanceof CraftIronGolem) {
                            if(cooldown.ready(player)) {
                                cooldown.use(player);
                                entity.teleport(player);
                                ((Frankenstein) (((CraftIronGolem) entity).getHandle())).evaluateTarget();
                                ItemStack roses = CustomItems.getFrankensteinRoses();
                                roses.setAmount(player.getItemInHand().getAmount() - 1);
                                player.getInventory().setItemInHand(roses);
                                System.out.println("evaluated");
                            } else{
                                player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You cannot use that ability for (" + cooldown.left(player) + "s)");
                            }
                        }
                    }
                }
            }
        }
    }
}
