package me.ammelsallow.blossomsg.Kits.Shepherd.Tasks;

import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.Misc.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShepherdProximityTask extends BukkitRunnable {

    private Game g;

    public ShepherdProximityTask(Game g){
        this.g = g;
    }
    @Override
    public void run() {
        int count = 0;
        for(Player p : g.getPlayers()){
            if(PlayerKitSelection.selectedKit.get(p.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(p.getUniqueId()).equals("shep")){
                List<Entity> assocEntities = new ArrayList<>();
                List<UUID> assocUUID = g.getMob(p.getUniqueId());
                for(Entity e : p.getWorld().getEntities()){
                    if(assocUUID != null && assocUUID.contains(e.getUniqueId())){
                        assocEntities.add(e);
                    }
                }
                for(Entity e : assocEntities){
                    if(e.getType() == EntityType.SHEEP){
                        count++;
                    }
                }
            }
            Util.addHealth(p,count);
        }
    }
}
