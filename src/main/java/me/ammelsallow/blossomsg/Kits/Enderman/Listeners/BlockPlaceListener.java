package me.ammelsallow.blossomsg.Kits.Enderman.Listeners;

import me.ammelsallow.blossomsg.Kits.Enderman.Misc.ItemLocation;
import me.ammelsallow.blossomsg.Kits.Enderman.Misc.TempBlock;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BlockPlaceListener implements Listener {
    private static ArrayList<TempBlock> tempBlockArray = new ArrayList<>();
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Block block = e.getBlockPlaced();
        Player p = e.getPlayer();
        if(block.getType() == Material.GRASS) {
            long time = System.currentTimeMillis();
            tempBlockArray.add(new TempBlock(block, time,p));
        }
    }
    public static ArrayList<TempBlock> getTempBlockArray(){
        return tempBlockArray;
    }
}
