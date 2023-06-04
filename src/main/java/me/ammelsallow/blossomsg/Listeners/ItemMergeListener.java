package me.ammelsallow.blossomsg.Listeners;

import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemMergeListener implements Listener {

    @EventHandler
    public void onItemMerge(ItemMergeEvent event){
        ItemStack itemStack = event.getEntity().getItemStack();

        NBTTagCompound compound = CraftItemStack.asNMSCopy(itemStack).getTag();
        if(compound == null){
            return;
        }
        if(compound.getInt("diamondFromSupplyDrop") == 1){
            event.setCancelled(true);
        }
        if(compound.getInt("ironFromSupplyDrop") == 1){
            event.setCancelled(true);
        }
    }
}
