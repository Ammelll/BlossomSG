package me.ammelsallow.blossomsg.Tasks;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckForWinner extends BukkitRunnable {
    private BlossomSG plugin;

    public CheckForWinner(BlossomSG pl){
        this.plugin = pl;
    }
    @Override
    public void run() {
        for(Game g : plugin.getGames()){
            if(g.getPlayerAmount() == 1 && g.getStarted()){
                g.end(g.getPlayers().get(0));
                break;
            }
        }
    }
}
