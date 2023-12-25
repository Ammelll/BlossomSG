package me.ammelsallow.blossomsg.Game.Misc;

import org.bukkit.inventory.ItemStack;

public class LootItem {
    private ItemStack item;
    private double chance;

    public LootItem(ItemStack _item, double chance){
        this.chance = chance;
        this.item = _item;
    }

    public ItemStack getItem(){
        return item;
    }
    public double getChance(){return chance;}

}
