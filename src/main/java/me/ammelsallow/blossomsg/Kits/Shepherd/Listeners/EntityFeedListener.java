package me.ammelsallow.blossomsg.Kits.Shepherd.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Kits.Cooldown;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class EntityFeedListener implements Listener {

    private BlossomSG plugin;

    private Cooldown cooldown = new Cooldown(16000);

    public EntityFeedListener(BlossomSG plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
        Entity clicked = e.getRightClicked();
        Player p = e.getPlayer();
        Game g = plugin.getGame(p);
        if(g != null){
            List<UUID> assocUUID = g.getMob(p.getUniqueId());
            if(assocUUID != null){
                if(assocUUID.contains(clicked.getUniqueId())){
                    if(p.getItemInHand().isSimilar(CustomItems.getShepherdWheat())){
                        if(cooldown.ready(p)) {
                            cooldown.use(p);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 140, 0), true);
                            ItemStack wheat = CustomItems.getShepherdWheat();
                            wheat.setAmount(p.getItemInHand().getAmount() - 1);
                            p.setItemInHand(wheat);
                        } else{
                            p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You cannot use that ability for (" + cooldown.left(p) + "s)");
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
