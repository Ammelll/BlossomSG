package me.ammelsallow.blossomsg.Game.Testing;

import me.ammelsallow.blossomsg.DB.Model.PlayerStats;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
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

import java.sql.SQLException;

public class GameCloseHandler {
    private Game game;
    private int save;
    private Player winner;

    public GameCloseHandler(Game game){
        this.game = game;
    }
    public void end(Player winner) {
        this.winner = winner;
        endComponents();
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
                if (countDown > 0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(Game.sgPrefix + ChatColor.WHITE + "The lobby will close in " + ChatColor.RED + "" + ChatColor.BOLD + countDown + ChatColor.RESET + " seconds!");
                    }
                    countDown--;
                } else {
                    game.getGameMatchHandler().cancelCapture();
                    try {
                        closeGame();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Bukkit.getScheduler().cancelTask(save);
                }

            }
        }, 20, 20);
    }


    private void endComponents(){
        game.getGameMatchHandler().cancelVehicle();
        game.getPlayers().clear();
        game.setStarted(false);
    }
    private void celebration(){
        PlayerConnection connection = ((CraftPlayer) winner.getPlayer()).getHandle().playerConnection;
        String titleString = ChatColor.GOLD + "" + ChatColor.BOLD + "Victory!";
        IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a("{'text': ' " + titleString + " ' }");
        PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, text, 1, 80, 1);
        connection.sendPacket(packet);
        for (int i = 0; i < 5; i++) {
            winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
        }
    }
    public Player getWinner(){
        return winner;
    }
    private void closeGame() throws SQLException {
        prepGame();
        prepPlayers();
    }

    private void prepGame() {
        game.getPlugin().getWorldLoader().rebuild(game.getWorld());
        game.getPlugin().removeGame(game);
        game.getPlayerKills().clear();
        game.getPlayers().clear();
    }

    private void prepPlayers(){
        for(Player p : game.getPlayers()){
            prepAttributes(p);
            prepInventory(p);
            sendToLobby(p);
        }
    }

    private void prepAttributes(Player p) {
        p.setTotalExperience(0);
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
    }

    private void sendToLobby(Player p) {
        p.teleport(new Location(Bukkit.getWorld("world"),0,150,0));
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
    }
}
