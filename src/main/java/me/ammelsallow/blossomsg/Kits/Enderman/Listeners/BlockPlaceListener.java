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
        } else if (block.getType() == Material.CHEST) {
            if(e.getItemInHand().getType() == Material.CHEST){
                ItemStack item = e.getItemInHand();
                net.minecraft.server.v1_8_R3.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = nmsitem.getTag();
                ArrayList<ItemLocation> itemLocations = new ArrayList<>();
                Location loc = e.getBlock().getLocation().add(0,0.75,0);
                ItemStack stone_sword = new ItemStack(Material.STONE_SWORD,1);
                stone_sword.addEnchantment(Enchantment.DAMAGE_ALL,1);
                itemLocations.add(new ItemLocation(loc, new ItemStack(Material.EXP_BOTTLE,2)));
                itemLocations.add(new ItemLocation(loc, stone_sword));
                itemLocations.add(new ItemLocation(loc, new ItemStack(Material.IRON_INGOT,1)));
                itemLocations.add(new ItemLocation(loc, new ItemStack(Material.ARROW,4)));
                itemLocations.add(new ItemLocation(loc, new ItemStack(Material.DIAMOND_HELMET,1)));
                itemLocations.add(new ItemLocation(loc, new ItemStack(Material.IRON_LEGGINGS,1)));
                itemLocations.add(new ItemLocation(loc, new ItemStack(Material.IRON_CHESTPLATE,1)));
                itemLocations.add(new ItemLocation(loc, new ItemStack(Material.DIAMOND_BOOTS,1)));

                if(tag != null){
                    e.setCancelled(true);
                    ItemStack itemRemoved = e.getItemInHand();
                    itemRemoved.setAmount(1);
                    e.getPlayer().getInventory().removeItem(itemRemoved);
                    for(int i = 0; i < 5; i++){
                        int random = (int) (Math.random() * itemLocations.size());
                        loc.getWorld().dropItem(itemLocations.get(random).getLocation(),itemLocations.get(random).getItemStack());
                        itemLocations.remove(random);
                    }
                }
            }
        }
    }
    public static ArrayList<TempBlock> getTempBlockArray(){
        return tempBlockArray;
    }
}
