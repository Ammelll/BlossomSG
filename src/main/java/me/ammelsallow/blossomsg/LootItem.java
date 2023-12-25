package me.ammelsallow.blossomsg;

import org.bukkit.inventory.ItemStack;

public class LootItem {
    private double percentChance;
    private ItemStack item;

    public LootItem(ItemStack _item, double chance){
        this.percentChance = chance;
        this.item = _item;
    }

    public double getPercentChance(){
        return percentChance;
    }
    public ItemStack getItem(){
        return item;
    }

}
