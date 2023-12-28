package me.ammelsallow.blossomsg.Game.Tasks;

import me.ammelsallow.blossomsg.Game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CapturePointUpdate extends BukkitRunnable {

    private Game game;

    private HashMap<UUID,Double> playerPercents = new HashMap<>();
    private String capturePointStatus = "0.0";
    private ArmorStand stand;
    public CapturePointUpdate(Game g){
        this.game = g;
        for(Player p : game.getPlayers()) {
            playerPercents.put(p.getUniqueId(), 0.0);
        }
        stand = (ArmorStand) game.getWorld().spawnEntity(new Location(game.getWorld(),game.getMap().getCenter().getX(),game.getWorld().getHighestBlockYAt(game.getMap().getCenter())-3,game.getMap().getCenter().getZ()), EntityType.ARMOR_STAND);
    }
    @Override
    public void run() {
        for(Player p : game.getPlayers()) {
            updateScoreboard(p);
        }

        List<Entity> entitiesInBoundingBox = stand.getNearbyEntities(10,10,10);
        ArrayList<Player> playersInBoundingBox = new ArrayList<>();
        ArrayList<Player> playersInCapturePoint = new ArrayList<>();
        for(Entity e : entitiesInBoundingBox){
            if(e instanceof Player){
                playersInBoundingBox.add((Player) e);
            }
        }
        for(Player p : playersInBoundingBox){
            int xCoordinatePlayer =  (p.getLocation().getBlockX() );
            int zCoordinatePlayer = (p.getLocation().getBlockZ());

            int xCoordinateCenter = (stand.getLocation().getBlockX());
            int zCoordinateCenter =  (stand.getLocation().getBlockZ());

            if(Math.abs(xCoordinateCenter-xCoordinatePlayer) + Math.abs(zCoordinateCenter-zCoordinatePlayer) < 8){
                playersInCapturePoint.add(p);
            }
        }
        if(playersInCapturePoint.size() == 1){
            Player increaser = playersInCapturePoint.get(0);
            if(playerPercents.containsKey(increaser.getUniqueId())){
                double percent = playerPercents.get(increaser.getUniqueId());
                percent +=1.5;
                if(percent >= 100){
                    System.out.println(game.getGameCloseHandler());
                    game.getGameCloseHandler().end(increaser);
                    increaser.sendMessage("INCREASED");
                }
                playerPercents.put(increaser.getUniqueId(),percent);
                capturePointStatus = String.valueOf(getHighestScore());
            }
        } else if(playersInCapturePoint.size() > 1){
            capturePointStatus = "Contested";
        } else {
            capturePointStatus = String.valueOf(getHighestScore());
        }

    }
    public void setScoreboard(Player p){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("Blossom","dummy");
        objective.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Blossom");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        ArrayList<Score> scores = new ArrayList<>();
        scores.add(objective.getScore(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "blossomsg.net"));
        scores.add(objective.getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                     "));
        scores.add(objective.getScore(ChatColor.AQUA + "Players " + ChatColor.DARK_GRAY + ">" + ChatColor.RESET + game.getPlayerAmount() + ""));
        scores.add(objective.getScore(ChatColor.AQUA + "Kills " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + game.getKills(p) + ""));
        scores.add(objective.getScore(ChatColor.AQUA + "Your Score " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + playerPercents.get(p.getUniqueId())+ "%"));
        scores.add(objective.getScore(ChatColor.AQUA + "Objective " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + capturePointStatus + "%"));

        for(int i = 0; i < scores.size(); i++){
            scores.get(i).setScore(i);
        }
        p.setScoreboard(scoreboard);
    }
    public void updateScoreboard(Player p){
        resetScoreboard(p);
        setScoreboard(p);

    }
    public void resetScoreboard(Player p){
        Scoreboard scoreboard = p.getScoreboard();
        scoreboard.getEntries().forEach(scoreboard::resetScores);
    }
    private double getHighestScore(){
        Double highest = 0.0;
        for(Player p : game.getPlayers()){
            if(playerPercents.get(p.getUniqueId()) > highest){
                highest = playerPercents.get(p.getUniqueId());
            }
        }
        return highest;
    }

}