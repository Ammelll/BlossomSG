package me.ammelsallow.blossomsg;

import me.ammelsallow.blossomsg.DB.Database;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Mobs.ArmorStandNoClip;
import me.ammelsallow.blossomsg.Game.Mobs.Frankenstein;
import me.ammelsallow.blossomsg.Kits.Kit;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import me.ammelsallow.blossomsg.Misc.NMSUtil;
import me.ammelsallow.blossomsg.Misc.PluginUtil;
import me.ammelsallow.blossomsg.Recipes.RecipeManager;
import me.ammelsallow.blossomsg.WorldLoading.Maps.SGMap;
import me.ammelsallow.blossomsg.WorldLoading.WorldLoader;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
//Want a new Kit?
//Make a new Kit in KITS
//add a function for it in KitMenuCommand
//add associated Listeners/Tasks/Ect in package named after kit under *Kits*
public final class BlossomSG extends JavaPlugin {
    public static final Kit[] KITS = {new Kit(Material.ENDER_PEARL,"ender",new ItemStack[]{CustomItems.getEndermanEnderheart(),CustomItems.getEndermanBlocks()}),new Kit(Material.BOW,"robin",new ItemStack[]{CustomItems.getRobinhoodBow(),new ItemStack(Material.ARROW,5)}),new Kit(Material.FIREBALL,"nether", new ItemStack[]{CustomItems.getNethermageAxe(), CustomItems.getNethermagePotions()}), new Kit(Material.DIAMOND_AXE, "lumber", new ItemStack[]{CustomItems.getLumberjackAxe()}), new Kit(Material.RED_ROSE,"frank", new ItemStack[]{CustomItems.getFrankensteinEgg(),CustomItems.getFrankensteinRoses()}), new Kit(Material.BOAT,"cap", new ItemStack[]{CustomItems.getCaptainRod(),CustomItems.getCaptainBoats()})};
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
