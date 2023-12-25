package me.ammelsallow.blossomsg;

import me.ammelsallow.blossomsg.DB.Database;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Mobs.ArmorStandNoClip;
import me.ammelsallow.blossomsg.Game.Mobs.Frankenstein;
import me.ammelsallow.blossomsg.Misc.NMSUtil;
import me.ammelsallow.blossomsg.Misc.PluginUtil;
import me.ammelsallow.blossomsg.Recipes.RecipeManager;
import me.ammelsallow.blossomsg.WorldLoading.Maps.SGMap;
import me.ammelsallow.blossomsg.WorldLoading.WorldLoader;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;

public final class BlossomSG extends JavaPlugin {

    private ArrayList<Game> games = new ArrayList<>();
    private Database database;
    private WorldLoader worldLoader;
    @Override
    public void onEnable(){
        PluginUtil pluginUtil = new PluginUtil(this);
        pluginUtil.init();

        worldLoader = new WorldLoader(this);
        for(SGMap sgMap : SGMap.getMapPool()){
            System.out.println("MAP: " + sgMap.getCenter());
            System.out.println(Bukkit.getWorld("Shire"));
            worldLoader.delete(sgMap.getWorld());
            worldLoader.load(sgMap.getName());
        }
        RecipeManager.addAllRecipes();

        NMSUtil nmsu = new NMSUtil();
        nmsu.registerEntity("ArmorStandNoClip", 30, EntityArmorStand.class, ArmorStandNoClip.class);
        nmsu.registerEntity("Frankenstein", 99, EntityIronGolem.class, Frankenstein.class);
        try{
            this.database = new Database();
            database.initializeDatabase();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Unable to connect to DB and create tables");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
    public WorldLoader getWorldLoader(){
        return worldLoader;
    }
    public void removeGame(Game g){
        games.remove(g);
    }

}
