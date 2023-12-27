package me.ammelsallow.blossomsg.Game.Tasks;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
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
                g.getGameCloseHandler().end(g.getPlayers().get(0));
                break;
            }
        }
    }
}