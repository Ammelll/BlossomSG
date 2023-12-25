package me.ammelsallow.blossomsg.Kits.Robinhood.Listeners;

import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {
            Projectile shot = (Projectile) e.getDamager();
            if (shot instanceof Projectile) {
                Player shooter = (Player) shot.getShooter();
                if (e.getEntity() instanceof Player) {
                    if (PlayerKitSelection.selectedKit.get(shooter.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(shooter.getUniqueId()).equals("robin")) {
                        shooter.getInventory().addItem(new ItemStack(Material.ARROW));
                    }
                }
            }
        }
    }
}