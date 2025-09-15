package me.ammelsallow.blossomsg.Game.GameHelpers;

import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Misc.PlayerTeam;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import me.ammelsallow.blossomsg.WorldLoading.WorldLoader;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class GameCloseHandler {
    private Game game;
    private int save;
    private PlayerTeam winningTeam;

    public GameCloseHandler(Game game){
        this.game = game;
    }
    public void end(PlayerTeam winningTeam) {
        game.setStarted(false);
        this.winningTeam = winningTeam;
        celebration();
        startLobbyCloseTask();
        try {
            game.updateDatabase();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void startLobbyCloseTask(){
        save = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable() {
            int countDown = 5;
            @Override
            public void run() {
                if(countDown > 0){
                    countDown--;
                    for(Player p : game.getPlayers()){
                        p.sendMessage(Game.sgPrefix + ChatColor.WHITE + "The lobby will close in " + ChatColor.RED + "" + ChatColor.BOLD + countDown + ChatColor.RESET + " seconds!");
                    }
                } else{
                    Bukkit.getScheduler().cancelTask(save);
                    countDown = 5;
                    game.getGameMatchHandler().cancelCapture();
                    try {
                        closeGame();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    endComponents();
                }
            }
        },20L,20L);

//    int countdown = 5;
//    @Override
//    public void run() {
//        if(countdown > 0){
//            for (Player p : game.getPlayers()){
//                p.sendMessage(Game.sgPrefix + ChatColor.WHITE + "The lobby will close in " + ChatColor.RED + "" + ChatColor.BOLD + countdown + ChatColor.RESET + " seconds!");
//            }
//            countdown--;
//        } else{
//            Bukkit.getScheduler().cancelTask(save);
//        }
//    }
//        save = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable() {
//            int countDown = 5;
//
//            @Override
//            public void run() {
//                System.out.println(save);
//                if (countDown > 0) {
//                    for (Player p : Bukkit.getOnlinePlayers()) {
//                        p.sendMessage(Game.sgPrefix + ChatColor.WHITE + "The lobby will close in " + ChatColor.RED + "" + ChatColor.BOLD + countDown + ChatColor.RESET + " seconds!");
//                    }
//                    countDown--;
//                } else {
//                    System.out.println("CANCELLING");
//                    Bukkit.getScheduler().cancelTask(save);
//
//                    game.getGameMatchHandler().cancelCapture();
//                    try {
//                        closeGame();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                    endComponents();
//                }
//
//            }
//        }, 20, 20);
    }


    private void endComponents(){
        game.getGameMatchHandler().cancelVehicle();
        game.getPlayers().clear();
    }
    private void celebration(){
        for(Player winner : winningTeam.getMembers()){
            PlayerConnection connection = ((CraftPlayer) winner.getPlayer()).getHandle().playerConnection;
            String titleString = ChatColor.GOLD + "" + ChatColor.BOLD + "Victory!";
            IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a("{'text': ' " + titleString + " ' }");
            PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, text, 1, 80, 1);
            connection.sendPacket(packet);
            for (int i = 0; i < 5; i++) {
                winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
            }
        }
    }
    public PlayerTeam getWinner(){
        return winningTeam;
    }
    private void closeGame() throws SQLException {
        prepPlayers();
        prepGame();
    }

    private void prepGame() {
        game.getPlugin().removeGame(game);
        game.getPlayerKills().clear();
        game.getPlayers().clear();
        game.getGameQueueHandler().getStartingPlayers().clear();
        WorldLoader.rebuild(game.getMap().getWorld());
        new BukkitRunnable() {

            @Override
            public void run() {
                WorldLoader.unload(game.getMap().getWorld());
            }
        }.runTaskLater(game.getPlugin(), 60L);
        }

    private void prepPlayers(){
        System.out.println(game.getPlayers());
        for(Player p : game.getPlayers()){
            prepAttributes(p);
            prepInventory(p);
            GameScoreboardHandler.resetScoreboard(p);
            sendToLobby(p);
        }
    }

    private void prepAttributes(Player p) {
        p.setTotalExperience(0);
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
    }

    private void sendToLobby(Player p) {
        //14.5 ,108 ,24.5
        p.teleport(new Location(Bukkit.getWorld("world"),-144.5,71,272.5));
        p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "The game has ended and you have been sent back to the lobby!");
    }

    private void prepInventory(Player p) {
        PlayerInventory i = p.getInventory();
        i.setHelmet(null);
        i.setChestplate(null);
        i.setLeggings(null);
        i.setBoots(null);
        i.clear();
        i.setItem(0, CustomItems.getCompassSelector());
        Bukkit.broadcastMessage("PREPPED");
    }
}
