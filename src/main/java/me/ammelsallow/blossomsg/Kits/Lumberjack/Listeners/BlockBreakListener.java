package me.ammelsallow.blossomsg.Kits.Lumberjack.Listeners;

import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block brokenBlock = e.getBlock();
        Material type = brokenBlock.getType();
        Location location = brokenBlock.getLocation();
        World world = location.getWorld();
        if(type == Material.LOG){
            if(PlayerKitSelection.selectedKit.get(p.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(p.getUniqueId()).equals("lumber")){
                brokenBlock.setType(Material.AIR);
                world.dropItemNaturally(location.add(0,0.5,0), new ItemStack(Material.STICK,4));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,0),true);
            }
        }
    }
}
