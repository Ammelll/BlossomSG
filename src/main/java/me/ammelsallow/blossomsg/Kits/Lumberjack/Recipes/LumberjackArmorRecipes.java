package me.ammelsallow.blossomsg.Kits.Lumberjack.Recipes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class LumberjackArmorRecipes {
    private static ShapedRecipe shapedHelmet;
    private static ShapedRecipe shapedChestplate;
    private static ShapedRecipe shapedLeggings;
    private static ShapedRecipe shapedBoots;
    private static ShapedRecipe shapedLinks;
    public static void addRecipes(){
        ItemStack chainLinks = new ItemStack(Material.IRON_FENCE,3);
        ItemMeta chainLinksMeta = chainLinks.getItemMeta();
        chainLinksMeta.setDisplayName(ChatColor.RESET + "Chain Links");
        chainLinks.setItemMeta(chainLinksMeta);
        shapedLinks = new ShapedRecipe(chainLinks);
        shapedLinks.shape("SSS","SSS","SSS");
        shapedLinks.setIngredient('S', new MaterialData(Material.STICK));
        Bukkit.addRecipe(shapedLinks);

        ItemStack ljHelmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ljHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        shapedHelmet = new ShapedRecipe(ljHelmet);
        shapedHelmet.shape("SSS","S S");
        shapedHelmet.setIngredient('S', new MaterialData(Material.IRON_FENCE));
        Bukkit.addRecipe(shapedHelmet);

        ItemStack ljChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ljChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        shapedChestplate = new ShapedRecipe(ljChestplate);
        shapedChestplate.shape("S S", "SSS", "SSS");
        shapedChestplate.setIngredient('S', new MaterialData(Material.IRON_FENCE));
        Bukkit.addRecipe(shapedChestplate);

        ItemStack ljLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ljLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        shapedLeggings = new ShapedRecipe(ljLeggings);
        shapedLeggings.shape("SSS","S S", "S S");
        shapedLeggings.setIngredient('S',new MaterialData(Material.IRON_FENCE));
        Bukkit.addRecipe(shapedLeggings);

        ItemStack ljBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ljBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        shapedBoots = new ShapedRecipe(ljBoots);
        shapedBoots.shape("S S","S S");
        shapedBoots.setIngredient('S',new MaterialData(Material.IRON_FENCE));
        Bukkit.addRecipe(shapedBoots);
    }
    public boolean compare(Recipe recipe){
        ShapedRecipe[] recipes = {shapedHelmet,shapedChestplate,shapedLeggings,shapedBoots,shapedLinks};
        boolean contained = false;
        //!! Believed to be not nessecary
        //        if(recipe == null){
        //            return false;
        //        }
        for(ShapedRecipe sr : recipes){
            contained = sr.getResult().equals(recipe.getResult()) || contained;
        }
        return contained;
    }

}
