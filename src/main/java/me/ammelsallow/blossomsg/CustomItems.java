package me.ammelsallow.blossomsg;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

public class CustomItems {

    public static ItemStack getCompassSelector(){
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        ArrayList<String> compassLore = new ArrayList<>();
        compassLore.add(ChatColor.YELLOW + "Right Click to open Game Selector");
        compassMeta.setLore(compassLore);
        compassMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Game Selector");
        compass.setItemMeta(compassMeta);
        return compass;
    }
    public static ItemStack getEndermanBlocks(){
        ItemStack blocks = new ItemStack(Material.GRASS,7);
        ItemMeta blocksMeta = blocks.getItemMeta();
        blocksMeta.setDisplayName(ChatColor.DARK_GREEN+ "" + ChatColor.BOLD + "Enderman Blocks");
        ArrayList<String> blocksLore = new ArrayList<>();
        blocksLore.add(ChatColor.GREEN+ "Disappears after 5 seconds");
        blocksMeta.setLore(blocksLore);
        blocksMeta.addEnchant(Enchantment.SILK_TOUCH,1,false);
        blocksMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        blocks.setItemMeta(blocksMeta);
        return blocks;
    }
    public static ItemStack getEndermanEnderheart(){
        ItemStack heart = new ItemStack(Material.INK_SACK);
        Short s = 5;
        heart.setDurability(s);
        ItemMeta heartMeta = heart.getItemMeta();
        heartMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Enderheart");
        ArrayList<String> heartLore = new ArrayList<>();
        heartLore.add(ChatColor.LIGHT_PURPLE + "Click to teleport");
        heartMeta.setLore(heartLore);
        heart.setItemMeta(heartMeta);
        return heart;
    }
    public static ItemStack getRobinhoodBow(){
        ItemStack bow = new ItemStack(Material.BOW,1);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Robinhood's Bow");
        ArrayList<String> bowLore = new ArrayList<>();
        bowLore.add(ChatColor.RED+ "Retains arrows on hit");
        bowMeta.setLore(bowLore);
        bowMeta.spigot().setUnbreakable(true);
        bow.setItemMeta(bowMeta);
        return bow;
    }
    public static ItemStack getNethermageAxe(){
        ItemStack axe = new ItemStack(Material.GOLD_AXE,1);
        ItemMeta axeMeta = axe.getItemMeta();
        axeMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Nethermage's Axe");
        axeMeta.addEnchant(Enchantment.DAMAGE_ALL,3,false);
        ArrayList<String> axeLore = new ArrayList<>();
        axeMeta.setLore(axeLore);
        axeMeta.spigot().setUnbreakable(true);
        axe.setItemMeta(axeMeta);
        return axe;
    }
    public static ItemStack getNethermagePotions(){

        Potion pot = new Potion(PotionType.FIRE_RESISTANCE, 1, true);
        ItemStack potion = pot.toItemStack(3);

        //CHECK BOOKMARKS FOR 1.8 SUPPORT
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Nethermage Potion of Fire");
        ArrayList<String> potionLore = new ArrayList<>();
        potionLore.add(ChatColor.GOLD + "Light enemies ablaze!");
        potionMeta.setLore(potionLore);
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        potion.setItemMeta(potionMeta);
        return potion;
    }
}
