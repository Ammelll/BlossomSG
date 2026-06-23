package me.ammelsallow.blossomsg;

import me.ammelsallow.blossomsg.DB.Database;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Misc.Party;
import me.ammelsallow.blossomsg.Game.Mobs.ArmorStandNoClip;
import me.ammelsallow.blossomsg.Game.Mobs.Frankenstein;
import me.ammelsallow.blossomsg.Kits.Kit;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import me.ammelsallow.blossomsg.Misc.NMSUtil;
import me.ammelsallow.blossomsg.Misc.PluginUtil;
import me.ammelsallow.blossomsg.Misc.Util;
import me.ammelsallow.blossomsg.Recipes.RecipeManager;
import me.ammelsallow.blossomsg.WorldLoading.Maps.SGMap;
import me.ammelsallow.blossomsg.WorldLoading.WorldLoader;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Want a new Kit?
//Make a new Kit in KITS
//add a function for it in KitMenuCommand
//add associated Listeners/Tasks/Ect in package named after kit under *Kits*
public final class BlossomSG extends JavaPlugin {
    Map<Player, Party> parties = new HashMap<>();
    public static List<Kit> KITS = new ArrayList<>();
    private final List<Game> games = new ArrayList<>();
    private Database database;

    public void initKits(){
        KITS.add(new Kit(Material.ENDER_PEARL,"ender",new ItemStack[]{CustomItems.getEndermanEnderheart(),CustomItems.getEndermanBlocks()}));
        KITS.add(new Kit(Material.BOW,"robin",new ItemStack[]{CustomItems.getRobinhoodBow(),new ItemStack(Material.ARROW,5)},new ItemStack[]{new ItemStack(Material.AIR),Util.getEnchantedItemStack(new ItemStack(Material.CHAINMAIL_LEGGINGS),Enchantment.PROTECTION_PROJECTILE),Util.getEnchantedItemStack(new ItemStack(Material.GOLD_CHESTPLATE), Enchantment.PROTECTION_PROJECTILE),new ItemStack(Material.AIR)}));
        KITS.add(new Kit(Material.FIREBALL,"nether", new ItemStack[]{CustomItems.getNethermageAxe(), CustomItems.getNethermagePotions()}));
        KITS.add(new Kit(Material.DIAMOND_AXE, "lumber", new ItemStack[]{CustomItems.getLumberjackAxe()}));
        KITS.add(new Kit(Material.RED_ROSE,"frank", new ItemStack[]{CustomItems.getFrankensteinEgg(),CustomItems.getFrankensteinRoses()}));
        KITS.add(new Kit(Material.BOAT,"cap", new ItemStack[]{CustomItems.getCaptainRod(),CustomItems.getCaptainBoats()}, new ItemStack[]{new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR),Util.getEnchantedItemStack(new ItemStack(Material.DIAMOND_HELMET),Enchantment.OXYGEN,2)}));
        KITS.add(new Kit(Material.WHEAT,"shep",new ItemStack[]{CustomItems.getShepherdCrook(),CustomItems.getShepherdEggs(),CustomItems.getShepherdWheat()}));
    }

    @Override
    public void onEnable(){
        initKits();
        PluginUtil pluginUtil = new PluginUtil(this);
        pluginUtil.init();
        WorldLoader.setPlugin(this);
        for(SGMap sgMap : SGMap.getMapPool()){
            System.out.println("MAP: " + sgMap.getCenter());
            System.out.println(sgMap.getWorld());
            if(sgMap.getWorld() == null){
                sgMap.getWorld().setWeatherDuration(0);
                WorldLoader.rebuild(sgMap.getWorld());
            }
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
    public List<Game> getGames(){
        return  games;
    }
    public Map<Player,Party> getParties(){
        return parties;
    }
    public void updateParties(Map<Player,Party> parties){
        this.parties = parties;
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
            if(g.getCapacity() > g.getPlayers().size() && !g.getStarted()){
                return g;
            }
        }
        return null;
    }
    public void removeGame(Game g){
        games.remove(g);
    }

}
