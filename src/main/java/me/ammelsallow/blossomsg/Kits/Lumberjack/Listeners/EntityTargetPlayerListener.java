package me.ammelsallow.blossomsg.Kits.Lumberjack.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityTargetPlayerListener implements Listener {

    private BlossomSG plugin;

    public EntityTargetPlayerListener(BlossomSG plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityTargetPlayer(EntityTargetLivingEntityEvent e){
        if(e.getTarget() instanceof Player){
            Player p = (Player) e.getTarget();
            Game g = plugin.getGame(p);
            if(g != null){
                if(g.getMob(p.getUniqueId()) != null && g.getMob(p.getUniqueId()).contains(e.getEntity().getUniqueId())){
                    e.setCancelled(true);
                }
            }
        }
    }
}
