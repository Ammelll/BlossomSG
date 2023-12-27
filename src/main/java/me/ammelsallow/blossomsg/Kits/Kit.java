package me.ammelsallow.blossomsg.Kits;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Kit {

    public Material material;
    public String stringID;
    public ItemStack[] items;
    public Kit(Material material, String stringID, ItemStack[] items){
        this.material = material;
        this.stringID = stringID;
        this.items = items;
    }
}
