package me.ammelsallow.blossomsg.Tasks;

import me.ammelsallow.blossomsg.Game;
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

public class    CapturePointUpdate extends BukkitRunnable {

    private Game game;

    private HashMap<UUID,Double> playerPercents = new HashMap<>();
    private String capturePointStatus = "0.0";
    private ArmorStand stand = (ArmorStand) Bukkit.getWorld("sg4").spawnEntity(new Location(Bukkit.getWorld("sg4"),0.5,28,-2.5),EntityType.ARMOR_STAND);
    public CapturePointUpdate(Game g){
        this.game = g;
        for(Player p : game.getPlayers()) {
            playerPercents.put(p.getUniqueId(), 0.0);
        }
    }
    @Override
    public void run() {
        for(Player p : game.getPlayers()) {
            updateScoreboard(p);
        }


        List<Entity> entitiesInBoundingBox = stand.getNearbyEntities(5,3,5);
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

            if(Math.abs(xCoordinateCenter-xCoordinatePlayer) + Math.abs(zCoordinateCenter-zCoordinatePlayer) < 9){
                playersInCapturePoint.add(p);
            }
        }
        if(playersInCapturePoint.size() == 1){
            Player increaser = playersInCapturePoint.get(0);
            if(playerPercents.containsKey(increaser.getUniqueId())){
                double percent = playerPercents.get(increaser.getUniqueId());
                percent +=1.5;
                if(percent == 100){
                    game.end(increaser);
                }
                playerPercents.put(increaser.getUniqueId(),percent);
                capturePointStatus = String.valueOf(getHighestScore());
            }
        } else if(playersInCapturePoint.size() > 1){
            capturePointStatus = "Contested";
        } else if(playersInCapturePoint.size() == 0){
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
        scores.add(objective.getScore(ChatColor.AQUA + "Players " + ChatColor.DARK_GRAY + ">" + ChatColor.RESET + game.getPlayerAmount() + ""));
        scores.add(objective.getScore(ChatColor.AQUA + "Kills " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + game.getKills(p) + ""));
        scores.add(objective.getScore(ChatColor.AQUA + "Your Score " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + playerPercents.get(p.getUniqueId())+ "%"));
        scores.add(objective.getScore(ChatColor.AQUA + "Objective " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + capturePointStatus + "%"));
        scores.add(objective.getScore(""));
        for(int i = 0; i < scores.size(); i++){
            scores.get(i).setScore(i);
        }
        p.setScoreboard(scoreboard);
    }
    public void updateScoreboard(Player p){
        Scoreboard scoreboard = p.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Score score;
        scoreboard.resetScores(ChatColor.AQUA + "Players " + ChatColor.DARK_GRAY + ">" + ChatColor.RESET + (game.getPlayerAmount()+1) + "");
        score = objective.getScore(ChatColor.AQUA + "Players " + ChatColor.DARK_GRAY + ">" + ChatColor.RESET + game.getPlayerAmount() + "");
        score.setScore(1);
        scoreboard.resetScores(ChatColor.AQUA + "Kills " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + (game.getKills(p)-1) + "");
        score = objective.getScore(ChatColor.AQUA + "Kills " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + game.getKills(p) + "");
        score.setScore(2);
        score = objective.getScore(ChatColor.AQUA + "Your Score " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + playerPercents.get(p.getUniqueId())+ "%");
        if(playerPercents.get(p.getUniqueId()) != null) {
            scoreboard.resetScores(ChatColor.AQUA + "Your Score " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + (playerPercents.get(p.getUniqueId())-1.5) + "%");
        }
        score.setScore(3);
        scoreboard.resetScores(ChatColor.AQUA + "Objective " + ChatColor.DARK_GRAY + "> "  + ChatColor.RESET + "Contested");
        scoreboard.resetScores(ChatColor.AQUA + "Objective " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET+ (getHighestScore()-1.5) + "%");
        scoreboard.resetScores(ChatColor.AQUA + "Objective " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET+ (getHighestScore()) + "%");
        if(capturePointStatus.equals("Contested")) {
            score = objective.getScore(ChatColor.AQUA + "Objective " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + capturePointStatus);
        } else{
            score = objective.getScore(ChatColor.AQUA + "Objective " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET+ capturePointStatus + "%");
        }
        score.setScore(4);
        p.setScoreboard(scoreboard);
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
