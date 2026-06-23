package me.ammelsallow.blossomsg.Game.GameHelpers;

import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Misc.PlayerTeam;
import me.ammelsallow.blossomsg.WorldLoading.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameQueueHandler {

    private Game game;
    private int save;
    private List<Player> players;
    private List<PlayerTeam> teams;
    public GameQueueHandler(Game game){
        this.game = game;
        players = new ArrayList<>();
        teams = new ArrayList<>();
    }

    public void join(PlayerTeam team) {
        String worldName = game.getMap().getName();

        World world = Bukkit.getWorld(worldName);

        System.out.println("started" + game.getStarted());
        if (!game.getStarted()) {
            for(Player player : team.getMembers()){
                if (world == null || WorldLoader.isRebuilding(worldName)) {
                    player.sendMessage(ChatColor.RED + "Arena is still loading.");
                    return;
                }
                prepPlayer(player);
                players.add(player);
            }
            teams.add(team);
            broadcastQueueUpdate();
            queueReadinessCheck();
        }


    }
    private void queueReadinessCheck(){
        //save could be 0 because of taskID = 0, not unassigned as we assume.

        if (players.size() > 1 && teams.size() > 1 && save == 0) {
            startCountdown();
        }
    }
    private void prepPlayer(Player p){
        p.setGameMode(GameMode.SPECTATOR);
        String worldName = game.getMap().getName();
        System.out.println(worldName);
        p.teleport(game.getMap().getCenter());
        p.getInventory().clear();
    }
    public void leave(Player player){
        if(game.getPlayers().contains(player)) {
            game.getPlayers().remove(player);
            game.getTeamFromPlayer(player).kill(player);
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
        if (players.size()  < 2 || teams.size() < 2) {
            Bukkit.getScheduler().cancelTask(save);
            for(Player p : players) {
                p.sendMessage(Game.sgPrefix + ChatColor.DARK_RED + "Start canceled, not enough players!");
                return;
            }
        }
    }
    public void startCountdown(){
        save = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable() {
            int countDown = 5;
            @Override
            public void run() {
                if(countDown > 0){
                    for(Player p : players) {
                        p.sendMessage(Game.sgPrefix + ChatColor.WHITE + "The game will begin in " + ChatColor.RED + "" + ChatColor.BOLD + countDown + ChatColor.RESET+ " seconds!");
                    }
                    countDown--;
                }
                else{
                    for(Player p : players) {
                        p.sendMessage(  Game.sgPrefix + ChatColor.GREEN + "" + ChatColor.BOLD +"The game has begun!");

                        p.teleport(game.getMap().getCenter());
                    }

                    Bukkit.getScheduler().cancelTask(save);
                    countDown = 5;
                    System.out.println(players);
                    setPlayers();
                    setTeams();
                    System.out.println(players);
                    game.getGameMatchHandler().startGame();
                    game.setStarted(true);
                    System.out.println("STARTED" + game.getStarted() + " " + game.uuid);
                }
            }
        },20,20);

    }
    private void setPlayers(){
        game.setPlayers(players);
    }
    private void setTeams(){
        game.setTeams(teams);
    }
    public List<Player> getStartingPlayers(){
        return players;
    }
}
