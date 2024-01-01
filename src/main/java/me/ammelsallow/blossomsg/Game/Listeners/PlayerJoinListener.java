package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.WorldLoading.Maps.SGMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {


    private final BlossomSG plugin;

    public PlayerJoinListener(BlossomSG plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        System.out.println("PLAYER JOIN EVENT");
        Player player = e.getPlayer();
        if(plugin.getEmptyGame() != null){
            System.out.println(plugin.getEmptyGame().getMap().getName());
            plugin.getEmptyGame().getGameQueueHandler().join(player);
        } else{
            Game game = new Game(plugin, SGMap.randomFrommPool());
            plugin.addGame(game);
            System.out.println("else " + game.getMap().getName());
            game.getGameQueueHandler().join(player);
        }
    }
}
