package me.ammelsallow.blossomsg.Kits.Robinhood.Listeners;

import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class InteractListener implements Listener {
    private final HashMap<UUID, Long> cooldown;


    public InteractListener() {
        this.cooldown = new HashMap<>();
    }


    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();


        if (player.getItemInHand().equals(CustomItems.getRobinhoodBow())) {
            if (PlayerKitSelection.selectedKit.get(player.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(player.getUniqueId()).equals("robin")) {
                Action action = event.getAction();
                Player p = event.getPlayer();
                if (event.getItem() != null && event.getItem().getType().equals(Material.BOW) && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
                    if (!this.cooldown.containsKey(p.getUniqueId())) {
                        this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                        p.setVelocity(p.getLocation().getDirection().multiply(1.4).setY(0.7));
                    } else {
                        long timeElapsed = System.currentTimeMillis() - this.cooldown.get(p.getUniqueId());
                        if (timeElapsed >= 10000) {
                            this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                            p.setVelocity(p.getLocation().getDirection().multiply(1.4).setY(0.7));
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You cannot use that ability for (" + Math.floor(((10000 - timeElapsed) * 10) / 1000.0) / 10 + "s)");

                        }
                    }
                }
            }
        }
    }
}