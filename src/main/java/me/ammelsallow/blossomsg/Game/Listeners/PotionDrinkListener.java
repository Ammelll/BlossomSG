package me.ammelsallow.blossomsg.Game.Listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

public class PotionDrinkListener implements Listener {

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent e){
        if(e.getItem().getType() == Material.POTION){
            e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
        }
    }
}
