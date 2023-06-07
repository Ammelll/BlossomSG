package me.ammelsallow.blossomsg;

import me.ammelsallow.blossomsg.Commands.KitMenuCommand;
import me.ammelsallow.blossomsg.Commands.giveCompass;
import me.ammelsallow.blossomsg.DB.Database;
import me.ammelsallow.blossomsg.Listeners.*;
import me.ammelsallow.blossomsg.Mobs.ArmorStandNoClip;
import me.ammelsallow.blossomsg.Tasks.CapturePointUpdate;
import me.ammelsallow.blossomsg.Tasks.CheckForWinner;
import me.ammelsallow.blossomsg.Tasks.UpdateTemporaryBlocks;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;
import java.util.ArrayList;

public final class BlossomSG extends JavaPlugin {

    private ArrayList<Game> games = new ArrayList<>();
    private Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        NMSUtil nmsu = new NMSUtil();
        nmsu.registerEntity("ArmorStandNoClip", 30, EntityArmorStand.class, ArmorStandNoClip.class);
        BukkitTask updateTemporaryBlocks = new UpdateTemporaryBlocks(this).runTaskTimer(this, 1L,1L);
        BukkitTask checkForWinner = new CheckForWinner(this).runTaskTimer(this,1L,1L);
        getCommand("compass").setExecutor(new giveCompass(this));
        getCommand("kit").setExecutor(new KitMenuCommand());
        getServer().getPluginManager().registerEvents(new ArmorStandInteractListener(),this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(this),this);
        getServer().getPluginManager().registerEvents(new InteractListener(this),this);
        getServer().getPluginManager().registerEvents(new MenuListener(this),this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(),this);
//        getServer().getPluginManager().registerEvents(new ProjectileHitListener(),this);
        getServer().getPluginManager().registerEvents(new PotionSplashListener(),this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this),this);
        getServer().getPluginManager().registerEvents(new BlockDestroyListener(),this);
        getServer().getPluginManager().registerEvents(new ItemMergeListener(),this);
        try{
            this.database = new Database();
            database.initializeDatabase();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Unable to connect to DB and create tables");
        }
    }
    public void onDisable(){
        this.database.closeConnection();
    }
    public Database getDatabased() {
        return database;
    }

    public ArrayList<Game> getGames(){
        return  games;
    }

    public Game getGame(Player p){
        for(Game game : games){
            if(game.getPlayers().contains(p)){
                return game;
            }
        }
        return null;
    }
    public void addGame(Game game){
        games.add(game);
    }

    public Game getEmptyGame(){
        for(Game g : games){
            if(g.getCapacity() > g.getPlayerAmount()){
                return g;
            }
        }
        return null;
    }
    public void removeGame(Game g){
        games.remove(g);
    }

    public void openMainMenu(Player player){
        Inventory inventory = Bukkit.createInventory(player,36, ChatColor.GOLD + "Game Selector");

        ItemStack join = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta joinMeta = join.getItemMeta();
        joinMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Join Blossom SG Queue");
        join.setItemMeta(joinMeta);

        inventory.setItem(12, join);
        player.openInventory(inventory);
    }
}
