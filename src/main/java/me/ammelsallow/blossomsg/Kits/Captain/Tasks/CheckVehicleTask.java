package me.ammelsallow.blossomsg.Kits.Captain.Tasks;

import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckVehicleTask extends BukkitRunnable {
    private Game game;

    public CheckVehicleTask(Game game){
        this.game = game;
    }
    @Override
    public void run() {
        for(Player p : game.getPlayers()){
            if(PlayerKitSelection.selectedKit.get(p.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(p.getUniqueId()).equals("cap")){
                if(p.getVehicle() != null && p.getVehicle().getType() == EntityType.BOAT){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,40,0));
                }
            }
        }
    }
}
