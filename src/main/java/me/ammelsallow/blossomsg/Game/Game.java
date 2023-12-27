package me.ammelsallow.blossomsg.Game;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.DB.Model.PlayerStats;
import me.ammelsallow.blossomsg.Game.Misc.RandomEvent;
import me.ammelsallow.blossomsg.Game.Tasks.CapturePointUpdate;
import me.ammelsallow.blossomsg.Game.Testing.GameCloseHandler;
import me.ammelsallow.blossomsg.Game.Testing.GameMatchHandler;
import me.ammelsallow.blossomsg.Game.Testing.GameQueueHandler;
import me.ammelsallow.blossomsg.Game.Testing.GameScoreboardHandler;
import me.ammelsallow.blossomsg.Kits.Captain.Tasks.CheckVehicleTask;
import me.ammelsallow.blossomsg.Kits.Kit;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import me.ammelsallow.blossomsg.WorldLoading.Maps.SGMap;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;


import java.sql.SQLException;
import java.util.*;
//I'm puking...
public class Game {
    RandomEvent randomEvent;
    private SGMap map;
    private boolean started;
    private Map<UUID,Integer> playerKills;
    private Map<UUID,Integer> playerDeaths;
    private Map<UUID,Integer> playerGold;
    private Map<UUID,UUID> mobMap;
    public final static String sgPrefix = ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "Blossom" + ChatColor.DARK_GRAY + "] ";

    private ArrayList<Player> players;
    private ArrayList<Player> startingPlayers;
    private BlossomSG plugin;
    private GameQueueHandler gqh;
    private GameMatchHandler gmh;
    private GameCloseHandler gmc;
    public Game(BlossomSG _plugin, SGMap map){
        this.map = map;
        plugin = _plugin;
        players = new ArrayList<>();
        playerKills = new HashMap<>();
        playerDeaths = new HashMap<>();
        playerGold = new HashMap<>();
        mobMap = new HashMap<>();
        started = false;
        randomEvent = generateRandomEvent((int) (Math.random() *3));
        this.gqh = new GameQueueHandler(this);
        this.gmh = new GameMatchHandler(this);
        this.gmc = new GameCloseHandler(this);
    }
    public GameMatchHandler getGameMatchHandler(){return  gmh;}
    public GameQueueHandler getGameQueueHandler(){return gqh;}
    public GameCloseHandler getGameCloseHandler(){return gmc;}

    //NOT DRY
    public void updateDatabase() throws SQLException {
        PlayerStats winnerStats = getPlayerStatsFromDatabase(gmc.getWinner());
        winnerStats.setWins(winnerStats.getWins() +1);
        this.plugin.getDatabased().updatePlayerStats(winnerStats);
        for(Player player : getGameQueueHandler().getStartingPlayers()){
            if(playerKills.containsKey(player.getUniqueId())) {
                PlayerStats pStats = getPlayerStatsFromDatabase(player);
                pStats.setKills(pStats.getKills() + playerKills.get(player.getUniqueId()));
                Bukkit.broadcastMessage(playerKills.get(player.getUniqueId()) + "");
                this.plugin.getDatabased().updatePlayerStats(pStats);
            }
            if(playerDeaths.containsKey(player.getUniqueId())){
                PlayerStats pStats = getPlayerStatsFromDatabase(player);
                pStats.setDeaths(pStats.getDeaths() + playerDeaths.get(player.getUniqueId()));
                this.plugin.getDatabased().updatePlayerStats(pStats);
            }
            if(playerGold.containsKey(player.getUniqueId())){
                PlayerStats pStats = getPlayerStatsFromDatabase(player);
                pStats.setGold(pStats.getGold() + playerGold.get(player.getUniqueId()));
                this.plugin.getDatabased().updatePlayerStats(pStats);
            }
        }
    }
    private PlayerStats getPlayerStatsFromDatabase(Player p) throws SQLException {
        PlayerStats stats = this.plugin.getDatabased().findPlayerStatsByUUID(p.getUniqueId().toString());
        if(stats == null){
            stats = new PlayerStats(p.getUniqueId().toString(), 0,0,0,0,new Date(),new Date());

            this.plugin.getDatabased().createPlayerStats(stats);
            return stats;
        }
        return stats;
    }

    public int getCapacity(){
        return map.getCapacity();
    }
    public SGMap getMap(){
        return map;
    }
    public World getWorld(){
        return map.getCenter().getWorld();
    }
    public int getPlayerAmount(){
        return players.size();
    }
    public boolean getStarted(){return started;}
    public void setStarted(boolean started){this.started = started;}
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public void setPlayers(List<Player> players){
        this.players.clear();
        this.players.addAll(players);
    }
    public RandomEvent generateRandomEvent(int i){
        return new RandomEvent(i,this);
    }
    public RandomEvent getRandomEvent(){return randomEvent;}
    private CapturePointUpdate capture;
    public void addKill(Player p){
        if(playerKills.containsKey(p.getUniqueId())){
            int kills = playerKills.get(p.getUniqueId());
            playerKills.put(p.getUniqueId(),kills+1);
        } else{
            playerKills.put(p.getUniqueId(),1);
        }
    }
    public void addMob(UUID playerUUID, UUID mobUUID){
        this.mobMap.put(playerUUID,mobUUID);
    }
    public void addDeath(Player p){
        if(playerDeaths.containsKey(p.getUniqueId())){
            int deaths = playerDeaths.get(p.getUniqueId());
            playerDeaths.put(p.getUniqueId(),deaths+1);
        } else{
            playerDeaths.put(p.getUniqueId(),1);
        }
    }
    public void addGold(Player p, int goldAmount){
        if(playerGold.containsKey(p.getUniqueId())){
            int gold = playerGold.get(p.getUniqueId());
            playerGold.put(p.getUniqueId(),gold+goldAmount);
        } else{
            playerGold.put(p.getUniqueId(),goldAmount);
        }
    }
    public int getKills(Player p){
        if(playerKills.containsKey(p.getUniqueId())){
            return playerKills.get(p.getUniqueId());
        }
        return 0;
    }
    public UUID getMob(UUID playerUUID){
        if(mobMap.containsKey(playerUUID)){
            return mobMap.get(playerUUID);
        }
        return null;
    }
    public Map<UUID,Integer> getPlayerKills(){
        return playerKills;
    }
    
    public BlossomSG getPlugin() {
        return plugin;
    }


}
