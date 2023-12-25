package me.ammelsallow.blossomsg.Kits.Nethermage.Listeners;

import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.projectiles.ProjectileSource;

public class PotionSplashListener implements Listener {
    @EventHandler
    public void onPotionSplash(PotionSplashEvent e){
        ThrownPotion potion = e.getPotion();
        ProjectileSource source = potion.getShooter();
        Player p = (Player) source;
        if(PlayerKitSelection.selectedKit.get(p.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(p.getUniqueId()).equals("nether")){
            for(LivingEntity entity : e.getAffectedEntities()){
                entity.setFireTicks((int) (200*e.getIntensity(entity)));
            }
            e.setCancelled(true);
        }
    }
}
