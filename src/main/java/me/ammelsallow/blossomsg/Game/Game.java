package me.ammelsallow.blossomsg.Game;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.DB.Model.PlayerStats;
import me.ammelsallow.blossomsg.Game.Misc.RandomEvent;
import me.ammelsallow.blossomsg.Game.Tasks.CapturePointUpdate;
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
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.SQLException;
import java.util.*;
//I'm puking...
public class Game {
    int save;
    int savePVP;
    int taskID;
    RandomEvent randomEvent;
    private SGMap map;
    private boolean started;
    private boolean queueStarted;
    private Map<UUID,Integer> playerKills;
    private Map<UUID,Integer> playerDeaths;
    private Map<UUID,Integer> playerGold;
    private Map<UUID,UUID> mobMap;
    private final static String sgPrefix = ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "Blossom" + ChatColor.DARK_GRAY + "] ";
    private BukkitTask capturePointUpdate;
    private BukkitTask vehicleUpdate;
    private CheckVehicleTask checkVehicleTask;
    private ArrayList<Player> players;
    private ArrayList<Player> startingPlayers = new ArrayList<>();
    private BlossomSG plugin;
    public Game(BlossomSG _plugin, SGMap map){
        this.map = map;
        plugin = _plugin;
        players = new ArrayList<>();
        playerKills = new HashMap<>();
        playerDeaths = new HashMap<>();
        playerGold = new HashMap<>();
        mobMap = new HashMap<>();
        started = false;
        queueStarted = false;
        randomEvent = getRandomEvent((int) (Math.random() *3));
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
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public RandomEvent getRandomEvent(int i){
        return new RandomEvent(i,this);
    }
    private CapturePointUpdate capture;
    public static String getSGPrefix(){
        return sgPrefix;
    }
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
    public void join(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(map.getCenter());
        player.getInventory().clear();
        if (!started) {
            players.add(player);
            for (Player p : players) {
                p.sendMessage(sgPrefix + ChatColor.AQUA + getPlayerAmount() + "/" + getCapacity() + " players in queue");
            }
            if (getPlayerAmount() > 1 && !queueStarted) {
                queueStarted = true;
                startCountdown();
            }
        }
    }
    public void leave(Player player){
        if(players.contains(player)) {
            if (!started) {
                players.remove(player);
                for (Player p : players) {
                    p.sendMessage(sgPrefix + ChatColor.AQUA + getPlayerAmount() + "/" + getCapacity() + " players in queue");
                }
            }else{
                players.remove(player);
            }
        }
    }

    public void startCountdown(){
        save = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int countDown = 10;
            @Override
            public void run() {


                if (getPlayerAmount()  < 2) {
                    Bukkit.getScheduler().cancelTask(save);
                    for(Player p : players) {
                        p.sendMessage(sgPrefix + ChatColor.DARK_RED + "Start canceled, not enough players!");
                        return;
                    }
                }
                if(countDown > 1){
                    countDown--;
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(sgPrefix + ChatColor.WHITE + "The game will begin in " + ChatColor.RED + "" + ChatColor.BOLD + countDown + ChatColor.RESET+ " seconds!");
                    }
                } else{
                    for(Player p : players) {
                        p.sendMessage(  sgPrefix + ChatColor.GREEN + "" + ChatColor.BOLD +"The game has begun!");

                        p.teleport(map.getCenter());
                    }
                    Bukkit.getScheduler().cancelTask(save);
                    countDown = 10;
                    startGame();
                    started = true;
                }
            }
        },20,20);


    }
    public void startGame(){
        capture = new CapturePointUpdate(this);
        checkVehicleTask = new CheckVehicleTask(this);
        vehicleUpdate = checkVehicleTask.runTaskTimer(plugin,0,20);
        Inventory inventory;
        for(Player p : players){
            p.setHealth(20.0);
            p.setTotalExperience(0);
            p.setSaturation(20);
            startingPlayers.add(p);
            capture.setScoreboard(p);
            p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,630,4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,630,4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,630,4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1000000,1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,630,1));
            p.setGameMode(GameMode.SURVIVAL);
            inventory = p.getInventory();
            inventory.clear();
            p.getInventory().setArmorContents(null);
            if(PlayerKitSelection.selectedKit.containsKey(p.getUniqueId())){
                String stringID = PlayerKitSelection.selectedKit.get(p.getUniqueId());
                for(int i = 0; i < BlossomSG.KITS.length; i++){
                    Kit kit = BlossomSG.KITS[i];
                    if(stringID.equals(kit.stringID)) {
                        for (int j = 0; j < kit.items.length; j++) {
                            inventory.setItem(j, kit.items[j]);
                        }
                    }
                }
            }
            else{
                p.sendMessage(sgPrefix + ChatColor.RED + "No kit selecting, defaulting to robinhood");
                PlayerKitSelection.selectedKit.put(p.getUniqueId(),"robin");
                inventory = p.getInventory();
                inventory.clear();
                inventory.setItem(0,CustomItems.getRobinhoodBow());
                inventory.setItem(1,new ItemStack(Material.ARROW,5));
            }
        }
        savePVP = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int countDown = 32;
            @Override
            public void run() {
                if(countDown > 1){
                    countDown--;
                    for(Player p : players){
                        GameScoreboardHandler.setScoreboard(p,countDown,"PVP: ",true,true);
                    }
                } else{
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(  sgPrefix + ChatColor.DARK_RED + "" + ChatColor.BOLD +"The grace period is over!");
                        GameScoreboardHandler.setScoreboard(p,countDown,"PVP: ",false,true);
                    }
                    randomEventStartTimer();
                    Bukkit.getScheduler().cancelTask(savePVP);
                    countDown = 32;
                }
            }
        },6L,20L);
    }


    public void end(Player winner) {
        vehicleUpdate.cancel();
        if (started) {
            started = false;
            PlayerConnection connection = ((CraftPlayer) winner.getPlayer()).getHandle().playerConnection;
            String titleString = ChatColor.GOLD + "" + ChatColor.BOLD + "Victory!";
            IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a("{'text': ' " + titleString + " ' }");
            PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, text, 1, 80, 1);
            connection.sendPacket(packet);
            for (int i = 0; i < 5; i++) {
                winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
            }
            save = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                int countDown = 5;

                @Override
                public void run() {
                    if (countDown > 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(sgPrefix + ChatColor.WHITE + "The lobby will close in " + ChatColor.RED + "" + ChatColor.BOLD + countDown + ChatColor.RESET + " seconds!");
                        }
                        countDown--;
                    } else {
                        if(capturePointUpdate != null) {
                            capturePointUpdate.cancel();
                        }
                        try {
                            closeGame(winner);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Bukkit.getScheduler().cancelTask(save);
                    }

                }
            }, 20, 20);
        }
        players.clear();
    }
    public void closeGame(Player winner) throws SQLException {
        plugin.getWorldLoader().rebuild(getWorld());
        //Update Database Stats
        for(Player player : startingPlayers){
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
        PlayerStats winnerStats = getPlayerStatsFromDatabase(winner);
        winnerStats.setWins(winnerStats.getWins() +1);
        this.plugin.getDatabased().updatePlayerStats(winnerStats);
        //Update Kills

        //Update Deaths
        //Update Gold
        //Update Wins




        plugin.removeGame(this);
        for(Player p : players){
            p.setTotalExperience(0);
            p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
            if(playerKills.containsKey(p.getUniqueId())){
                playerKills.remove(p.getUniqueId());
            }
            PlayerInventory i = p.getInventory();
            i.setHelmet(null);
            i.setChestplate(null);
            i.setLeggings(null);
            i.setBoots(null);
            i.clear();
            i.setItem(0,CustomItems.getCompassSelector());
            p.teleport(new Location(Bukkit.getWorld("world"),0,150,0));
            p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "The game has ended and you have been sent back to the lobby!");
        }
    }
    public void randomEventStartTimer(){
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int countDown = 62;

            @Override
            public void run() {
                if(countDown > 1){
                    countDown--;
                    for(Player p : players){

                        GameScoreboardHandler.setScoreboard(p,countDown,"Random Event: ",true,true);


                    }
                } else{
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(  sgPrefix + ChatColor.DARK_RED + "" + ChatColor.BOLD +"The grace period is over!");
                        GameScoreboardHandler.setScoreboard(p,countDown,"Random Event: ",false,true);
                    }
                    countDown = 62;
                    randomEvent.trigger();
                    Bukkit.getScheduler().cancelTask(taskID);
                    capturePointUpdate = capture.runTaskTimer(plugin,1L,20L);
                }
            }
        },6L,20L);
    }

    public BlossomSG getPlugin() {
        return plugin;
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

}
