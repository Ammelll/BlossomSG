package me.ammelsallow.blossomsg.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ArmorStandInteractListener implements Listener {
    @EventHandler
    public void OnInteractAtEntity(PlayerInteractAtEntityEvent e) {
        e.setCancelled(true);
    }
}
