package me.ammelsallow.blossomsg.Game.Testing;

import me.ammelsallow.blossomsg.Game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameQueueHandler {

    private Game game;
    private int save;
    private List<Player> players;
    public GameQueueHandler(Game game){
        this.game = game;
        players = new ArrayList<>();
    }

    public void join(Player player) {
        prepPlayer(player);
        if (!game.getStarted()) {
            players.add(player);
            broadcastQueueUpdate();
            queueReadinessCheck();
        }
    }
    private void queueReadinessCheck(){
        //save could be 0 because of taskID = 0, not unassigned as we assume.
        if (players.size() > 1 && save == 0) {
            startCountdown();
        }
    }
    private void prepPlayer(Player p){
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(game.getMap().getCenter());
        p.getInventory().clear();
    }
    public void leave(Player player){
        if(game.getPlayers().contains(player)) {
            game.getPlayers().remove(player);
            if (!game.getStarted()) {
                broadcastQueueUpdate();
            }
            minimumPlayerCountCheck();
        }
    }
    private void broadcastQueueUpdate(){
        for (Player p : players) {
            p.sendMessage(Game.sgPrefix + ChatColor.AQUA + players.size() + "/" + game.getCapacity() + " players in queue");
        }
    }
    private void minimumPlayerCountCheck(){
        if (players.size()  < 2) {
            Bukkit.getScheduler().cancelTask(save);
            for(Player p : players) {
                p.sendMessage(Game.sgPrefix + ChatColor.DARK_RED + "Start canceled, not enough players!");
                return;
            }
        }
    }
    public void startCountdown(){
        save = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable() {
            int countDown = 10;
            @Override
            public void run() {
                if(countDown > 1){
                    countDown--;
                    for(Player p : players) {
                        p.sendMessage(Game.sgPrefix + ChatColor.WHITE + "The game will begin in " + ChatColor.RED + "" + ChatColor.BOLD + countDown + ChatColor.RESET+ " seconds!");
                    }
                }
                else{
                    for(Player p : players) {
                        p.sendMessage(  Game.sgPrefix + ChatColor.GREEN + "" + ChatColor.BOLD +"The game has begun!");

                        p.teleport(game.getMap().getCenter());
                    }

                    Bukkit.getScheduler().cancelTask(save);
                    countDown = 10;
                    System.out.println(players);
                    setPlayers();
                    System.out.println(players);
                    game.getGameMatchHandler().startGame();
                    game.setStarted(true);
                }
            }
        },20,20);

    }
    private void setPlayers(){
        game.setPlayers(players);
    }
    public List<Player> getStartingPlayers(){
        return players;
    }
}
