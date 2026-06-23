package me.ammelsallow.blossomsg.Recipes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VanillaChangedRecipes {
    private final static List<Material> removedRecipes = Arrays.asList(new Material[]{Material.BUCKET, Material.BOW, Material.FISHING_ROD});
    private static ShapedRecipe shapedBow;
    private static ShapedRecipe shapedRod;
    public static void addRecipes(){
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            if (removedRecipes.contains(recipes.next().getResult().getType())) {
                recipes.remove();
            }
        }
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        rod.setDurability((short) 56);
        shapedRod = new ShapedRecipe(rod);
        shapedRod.shape("001","012","102");
        shapedRod.setIngredient('1', new MaterialData(Material.STICK));
        shapedRod.setIngredient('2', new MaterialData(Material.STRING));

        Bukkit.addRecipe(shapedRod);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.setDurability((short) 373);
        shapedBow = new ShapedRecipe(bow);
        shapedBow.shape("012","102","012");
        shapedBow.setIngredient('1', new MaterialData(Material.STICK));
        shapedBow.setIngredient('2', new MaterialData(Material.STRING));

        Bukkit.addRecipe(shapedBow);
    }
}
