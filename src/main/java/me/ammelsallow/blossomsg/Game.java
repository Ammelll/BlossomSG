package me.ammelsallow.blossomsg;

import me.ammelsallow.blossomsg.Model.PlayerStats;
import me.ammelsallow.blossomsg.Tasks.CapturePointUpdate;
import me.ammelsallow.blossomsg.Tasks.CheckForWinner;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Game {
    int save;
    int savePVP;
    int taskID;
    RandomEvent randomEvent;
    public String name;
    private boolean started;
    private HashMap<UUID,Integer> playerKills;
    private HashMap<UUID,Integer> playerDeaths;
    private HashMap<UUID,Integer> playerGold;
    private final static String sgPrefix = ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "Blossom" + ChatColor.DARK_GRAY + "] ";

    private int capacity;
    private BukkitTask capturePointUpdate;
    private ArrayList<Player> players;
    private ArrayList<Player> startingPlayers;
    private BlossomSG plugin;
    public Game(BlossomSG _plugin, int _capacity, String name){
        this.name = name;
        plugin = _plugin;
        capacity = _capacity;
        players = new ArrayList<>();
        playerKills = new HashMap<>();
        started = false;
        randomEvent = getRandomEvent((int) (Math.random() *3));
    }

    public int getCapacity(){
        return capacity;
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
    public void addDeath(Player p){
        if(playerDeaths.containsKey(p.getUniqueId())){
            int deaths = playerDeaths.get(p.getUniqueId());
            playerDeaths.put(p.getUniqueId(),deaths+1);
        } else{
            playerDeaths.put(p.getUniqueId(),1);
        }
    }
    public void addGold(Player p){
        if(playerGold.containsKey(p.getUniqueId())){
            int gold = playerGold.get(p.getUniqueId());
            playerGold.put(p.getUniqueId(),gold+1);
        } else{
            playerGold.put(p.getUniqueId(),1);
        }
    }
    public int getKills(Player p){
        if(playerKills.containsKey(p.getUniqueId())){
            return playerKills.get(p.getUniqueId());
            }
        return 0;
    }
    public void join(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        if (!started) {
            players.add(player);
            for (Player p : players) {
                p.sendMessage(sgPrefix + ChatColor.AQUA + getPlayerAmount() + "/" + capacity + " players in queue");
            }
            if (getPlayerAmount() > 1) {
                startCountdown();
            }
        }
    }
    public void leave(Player player){
        if(players.contains(player)) {
            if (!started) {
                players.remove(player);
                for (Player p : players) {
                    p.sendMessage(sgPrefix + ChatColor.AQUA + getPlayerAmount() + "/" + capacity + " players in queue");
                }
            }else{
                players.remove(player);
            }
        }
    }

    public void setScoreboardPvP(Player p, int countdown){
        Scoreboard scoreboard = p.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Score score;
        score = objective.getScore(ChatColor.AQUA + "PVP Enabled " + ChatColor.RESET + "0:" + countdown);
    }
    public void updateScoreboardPvP(Player p, int countdown){
        Scoreboard scoreboard = p.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Score score;
        score = objective.getScore(ChatColor.AQUA + "PVP Enabled " + ChatColor.RESET  + "0:"+ countdown);
        scoreboard.resetScores(ChatColor.AQUA + "PVP Enabled " + ChatColor.RESET +"0:"+  (countdown+1));
        if(countdown < 10){
            score = objective.getScore(ChatColor.AQUA + "PVP Enabled " + ChatColor.RESET  + "0:0"+ countdown);
            scoreboard.resetScores(ChatColor.AQUA + "PVP Enabled " + ChatColor.RESET +"0:0"+  (countdown+1));
        }
        score.setScore(6);
    }
    public void endScoreboardPvP(Player p){
        Scoreboard scoreboard = p.getScoreboard();
        scoreboard.resetScores(ChatColor.AQUA + "PVP Enabled " + ChatColor.RESET + "0:0"+ 0);
    }

    public void setScoreboardRandomEvent(Player p, int countdown){
        Scoreboard scoreboard = p.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Score score;
        if((double) countdown / 60 >= 1){
            score = objective.getScore(ChatColor.AQUA + "Random Event " + ChatColor.RESET + "1:0"+ (countdown % 60));
        } else{
            score = objective.getScore(ChatColor.AQUA + "Random Event " +ChatColor.RESET + "0:" + countdown);
        }
        score.setScore(6);
    }
    public void updateScoreboardRandomEvent(Player p, int countdown){
        Scoreboard scoreboard = p.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Score score;
        if((double) countdown / 60 >= 1){
            score = objective.getScore(ChatColor.AQUA + "Random Event " + ChatColor.RESET + "1:0" + (countdown % 60));
            scoreboard.resetScores(ChatColor.AQUA + "Random Event " + ChatColor.RESET + "1:0" +  ((countdown+1) % 60));
        } else {
            score = objective.getScore(ChatColor.AQUA + "Random Event " + ChatColor.RESET + "0:" + (countdown % 60));
            if(countdown % 60 + 1 == 60){
                scoreboard.resetScores(ChatColor.AQUA + "Random Event " + ChatColor.RESET + "1:0" + ((countdown+1) % 60));
            } else {
                scoreboard.resetScores(ChatColor.AQUA + "Random Event " +ChatColor.RESET + "0:"+ ((countdown+1) % 60));
            }
            if(countdown < 10){
                score = objective.getScore(ChatColor.AQUA + "Random Event " + ChatColor.RESET + "0:0" + (countdown % 60));
                scoreboard.resetScores(ChatColor.AQUA + "Random Event " +ChatColor.RESET + "0:"+ ((countdown) % 60));
                scoreboard.resetScores(ChatColor.AQUA + "Random Event " +ChatColor.RESET + "0:0"+ ((countdown+1) % 60));
            }
        }

        score.setScore(6);
    }
    public void endScoreboardRandomEvent(Player p){
        Scoreboard scoreboard = p.getScoreboard();
        scoreboard.resetScores(ChatColor.AQUA + "Random Event " +ChatColor.RESET + "0:00");
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

                            p.teleport(new Location(p.getWorld(),0,40,-3));
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
        Inventory inventory;
        for(Player p : players){
            startingPlayers.add(p);
            capture.setScoreboard(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,630,4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,630,4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,630,4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,630,1));
            p.setGameMode(GameMode.SURVIVAL);
            if(PlayerKitSelection.selectedKit.containsKey(p.getUniqueId())){
                switch (PlayerKitSelection.selectedKit.get(p.getUniqueId())){
                    case "ender":
                        inventory = p.getInventory();
                        inventory.clear();
                        inventory.setItem(0,CustomItems.getEndermanEnderheart());
                        inventory.setItem(1,CustomItems.getEndermanBlocks());
                        break;
                    case "robin":
                        inventory = p.getInventory();
                        inventory.clear();
                        inventory.setItem(0,CustomItems.getRobinhoodBow());
                        inventory.setItem(1,new ItemStack(Material.ARROW,5));
                        break;
                    case "nether":
                        inventory = p.getInventory();
                        inventory.clear();
                        inventory.setItem(0,CustomItems.getNethermageAxe());
                        inventory.setItem(1,CustomItems.getNethermagePotions());
                        break;
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
        randomEvent.trigger();
        savePVP = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int countDown = 32;
            @Override
            public void run() {
                if(countDown > 1){
                    countDown--;
                    for(Player p : players){

                        Scoreboard scoreboard = p.getScoreboard();
                        setScoreboardPvP(p,countDown-1);
                        updateScoreboardPvP(p,countDown-1);



                    }
                } else{
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(  sgPrefix + ChatColor.DARK_RED + "" + ChatColor.BOLD +"The grace period is over!");
                        endScoreboardPvP(p);
                    }
                    randomEventStartTimer();
                    Bukkit.getScheduler().cancelTask(savePVP);
                    countDown = 32;
                }
            }
        },6L,20L);
    }


    public void end(Player winner) {
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
                            capturePointUpdate.cancel();
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
        }
        public void closeGame(Player winner) throws SQLException {
        //Update Database Stats
            //TODO
            for(Player player : startingPlayers){
                if(playerKills.containsKey(player.getUniqueId())) {
                    PlayerStats pStats = getPlayerStatsFromDatabase(player);
                    pStats.setKills(pStats.getKills() + playerKills.get(player.getUniqueId()));
                }
                if(playerDeaths.containsKey(player.getUniqueId())){
                    PlayerStats pStats = getPlayerStatsFromDatabase(player);
                    pStats.setDeaths(pStats.getDeaths() + playerDeaths.get(player.getUniqueId()));
                }
                if(playerGold.containsKey(player.getUniqueId())){
                    PlayerStats pStats = getPlayerStatsFromDatabase(player);
                    pStats.setGold(pStats.getGold() + playerGold.get(player.getUniqueId()));
                }
            }
            PlayerStats winnerStats = getPlayerStatsFromDatabase(winner);
            winnerStats.setWins(winnerStats.getWins() +1);
            //Update Kills

            //Update Deaths
            //Update Gold
            //Update Wins




            plugin.removeGame(this);
            for(Player p : players){
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

                            setScoreboardRandomEvent(p,countDown-1);
                            updateScoreboardRandomEvent(p,countDown-1);



                        }
                    } else{
                        for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(  sgPrefix + ChatColor.DARK_RED + "" + ChatColor.BOLD +"The grace period is over!");
                            endScoreboardRandomEvent(p);
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
