package me.ammelsallow.blossomsg.Kits.Captain.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class EntityMountListener implements Listener {

    @EventHandler
    public void onEntityMount(EntityMountEvent e){
        if(!(e.getEntity() instanceof Player)){
            System.out.println(e.getEntity());
            return;
        }
        Player p = (Player) e.getEntity();
        Entity mount = e.getMount();
        if(mount.getType() == EntityType.BOAT){
            if(PlayerKitSelection.selectedKit.get(p.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(p.getUniqueId()).equals("cap")){
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,1000000,0));
            }
        }
    }
    @EventHandler
    public void onEntityDismount(EntityDismountEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        Player p = (Player) e.getEntity();
        Entity mount = e.getDismounted();

        if(mount.getType() == EntityType.BOAT){
            if(PlayerKitSelection.selectedKit.get(p.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(p.getUniqueId()).equals("cap")){
                p.removePotionEffect(PotionEffectType.REGENERATION);
            }
        }
    }
}

