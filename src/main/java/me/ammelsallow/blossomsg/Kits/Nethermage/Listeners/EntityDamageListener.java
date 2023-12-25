package me.ammelsallow.blossomsg.Kits.Nethermage.Listeners;

import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            if (PlayerKitSelection.selectedKit.get(attacker.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(attacker.getUniqueId()).equals("nether")) {
                if (attacker.getFireTicks() > 0) {
                    e.setDamage(e.getDamage() * 1.15);
                }

            }
        }
    }
}