package me.ammelsallow.blossomsg.Kits.Lumberjack.Listeners;

import me.ammelsallow.blossomsg.Kits.Lumberjack.Recipes.LumberjackArmorRecipes;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class PrepareItemCraftListener implements Listener {
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent e){
       // e.getRecipe();
        if(e.getViewers().size() == 1){
            Player p = (Player) e.getViewers().get(0);
            String kit = PlayerKitSelection.selectedKit.get(p.getUniqueId());
            LumberjackArmorRecipes ljr = new LumberjackArmorRecipes();
//            p.sendMessage("" + ljr.compare(e.getRecipe()));
            if((kit != null && kit.equals("lumber")) || !ljr.compare(e.getRecipe())){
                return;
            }
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
}
