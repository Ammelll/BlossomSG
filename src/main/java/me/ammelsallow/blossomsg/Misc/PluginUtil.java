package me.ammelsallow.blossomsg.Misc;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Commands.KitMenuCommand;
import me.ammelsallow.blossomsg.Game.Commands.PartyCommand;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;


public class PluginUtil {
    private final BlossomSG plugin;
    public PluginUtil(BlossomSG plugin){
        this.plugin= plugin;
    }
    public void init(){
        //Kits
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new EntityTargetPlayerListener(plugin),plugin);
        pm.registerEvents(new PlayerDamageListener(),plugin);
        pm.registerEvents(new BlockPlaceListener(),plugin);
        pm.registerEvents(new InteractListener(),plugin);
        pm.registerEvents(new PlayerInteractListener(plugin),plugin);
        pm.registerEvents(new BlockBreakListener(),plugin);
        pm.registerEvents(new PrepareItemCraftListener(),plugin);
        pm.registerEvents(new EntityDamageListener(),plugin);
        pm.registerEvents(new PotionSplashListener(),plugin);
        pm.registerEvents(new PlayerInteractListenerShepherd(plugin),plugin);
        pm.registerEvents(new EntityFeedListener(plugin),plugin);
        me.ammelsallow.blossomsg.Kits.Robinhood.Listeners.EntityDamageListener damageListener = new me.ammelsallow.blossomsg.Kits.Robinhood.Listeners.EntityDamageListener(plugin);
        pm.registerEvents(damageListener,plugin);
        pm.registerEvents(new me.ammelsallow.blossomsg.Kits.Robinhood.Listeners.InteractListener(damageListener),plugin);

        BukkitTask updateTemporaryBlocks = new UpdateTemporaryBlocks(plugin).runTaskTimer(plugin,1L,1L);

        //Game

        plugin.getCommand("compass").setExecutor(new giveCompass(plugin));
        plugin.getCommand("kit").setExecutor(new KitMenuCommand());
        plugin.getCommand("party").setExecutor(new PartyCommand(plugin));
        pm.registerEvents(new ArmorStandInteractListener(),plugin);
        pm.registerEvents(new BlockDestroyListener(plugin),plugin);
        pm.registerEvents(new ChestInteractListener(plugin),plugin);
        pm.registerEvents(new ChestMenuGoldListener(plugin),plugin);
        pm.registerEvents(new ItemMergeListener(),plugin);
        pm.registerEvents(new JoinLeaveListener(plugin),plugin);
        pm.registerEvents(new KitSelectorMenuListener(plugin),plugin);
        pm.registerEvents(new PlayerDeathListener(plugin),plugin);
        pm.registerEvents(new WeatherListener(),plugin);
        pm.registerEvents(new SupplyDropInteractEvent(),plugin);
        pm.registerEvents(new InfiniteRodListener(),plugin);
        pm.registerEvents(new PaydayListener(),plugin);
        pm.registerEvents(new PotionDrinkListener(),plugin);
        BukkitTask checkForWinner = new CheckForWinner(plugin).runTaskTimer(plugin,1L,1L);

    }
}
