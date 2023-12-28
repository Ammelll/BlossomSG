package me.ammelsallow.blossomsg.Misc;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Commands.KitMenuCommand;
import me.ammelsallow.blossomsg.Game.Commands.giveCompass;
import me.ammelsallow.blossomsg.Game.Listeners.*;
import me.ammelsallow.blossomsg.Game.Tasks.CheckForWinner;
import me.ammelsallow.blossomsg.Kits.Captain.Listeners.PlayerDamageListener;
import me.ammelsallow.blossomsg.Kits.Enderman.Listeners.BlockPlaceListener;
import me.ammelsallow.blossomsg.Kits.Enderman.Listeners.InteractListener;
import me.ammelsallow.blossomsg.Kits.Enderman.Tasks.UpdateTemporaryBlocks;
import me.ammelsallow.blossomsg.Kits.Frankenstein.Listeners.PlayerInteractListener;
import me.ammelsallow.blossomsg.Kits.Lumberjack.Listeners.BlockBreakListener;
import me.ammelsallow.blossomsg.Kits.Lumberjack.Listeners.EntityTargetPlayerListener;
import me.ammelsallow.blossomsg.Kits.Lumberjack.Listeners.PrepareItemCraftListener;
import me.ammelsallow.blossomsg.Kits.Nethermage.Listeners.EntityDamageListener;
import me.ammelsallow.blossomsg.Kits.Nethermage.Listeners.PotionSplashListener;
import me.ammelsallow.blossomsg.Kits.Shepherd.Listeners.EntityFeedListener;
import me.ammelsallow.blossomsg.Kits.Shepherd.Listeners.PlayerInteractListenerShepherd;
import me.ammelsallow.blossomsg.Kits.Shepherd.Tasks.ShepherdProximityTask;
import org.bukkit.scheduler.BukkitTask;


public class PluginUtil {
    private final BlossomSG plugin;
    public PluginUtil(BlossomSG plugin){
        this.plugin= plugin;
    }
    public void init(){
        //Kits

        plugin.getServer().getPluginManager().registerEvents(new EntityTargetPlayerListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDamageListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new BlockPlaceListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new InteractListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerInteractListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new BlockBreakListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PrepareItemCraftListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new EntityDamageListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PotionSplashListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerInteractListenerShepherd(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new EntityFeedListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new me.ammelsallow.blossomsg.Kits.Robinhood.Listeners.EntityDamageListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new me.ammelsallow.blossomsg.Kits.Robinhood.Listeners.InteractListener(),plugin);

        BukkitTask updateTemporaryBlocks = new UpdateTemporaryBlocks(plugin).runTaskTimer(plugin,1L,1L);

        //Game
        plugin.getCommand("compass").setExecutor(new giveCompass(plugin));
        plugin.getCommand("kit").setExecutor(new KitMenuCommand());
        plugin.getServer().getPluginManager().registerEvents(new ArmorStandInteractListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new BlockDestroyListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChestInteractListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChestMenuGoldListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new ItemMergeListener(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new JoinLeaveListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new KitSelectorMenuListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener(plugin),plugin);
        plugin.getServer().getPluginManager().registerEvents(new SupplyDropInteractEvent(),plugin);
        BukkitTask checkForWinner = new CheckForWinner(plugin).runTaskTimer(plugin,1L,1L);

    }
}
