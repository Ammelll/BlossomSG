package me.ammelsallow.blossomsg.Misc;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {
    public static final void addHealth(Damageable e, int amount){
        e.setHealth(Math.min(e.getMaxHealth(),e.getHealth()+amount));
    }
    public static final double getDistance(Location l1, Location l2){
        return Math.sqrt((l1.getX()-l2.getX()) * (l1.getX()-l2.getX()) + (l1.getY()-l2.getY()) * (l1.getY()-l2.getY()) + (l1.getZ()-l2.getZ())*(l1.getZ()-l2.getZ()));
    }
    public static ItemStack getEnchantedItemStack(ItemStack i,Enchantment e){
        return getEnchantedItemStack(i,e,1);
    }
    public static ItemStack getEnchantedItemStack(ItemStack i, Enchantment e, int level){
        i.addEnchantment(e,level);
        return i;
    }
}
