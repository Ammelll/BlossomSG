package me.ammelsallow.blossomsg;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ItemLocation {
    private Location location;
    private ItemStack itemStack;

    public ItemLocation(Location l, ItemStack is){
        location = l;
        itemStack = is;
    }
    public Location getLocation(){
        return  location;
    }
    public ItemStack getItemStack(){
        return itemStack;
    }
}
