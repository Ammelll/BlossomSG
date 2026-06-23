package me.ammelsallow.blossomsg.Kits.Robinhood.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Kits.Cooldown;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class EntityDamageListener implements Listener {
    BlossomSG plugin;


    Cooldown playerFallDamage = new Cooldown(1600);
    public EntityDamageListener(BlossomSG plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile)) {
            return;
        }
        Projectile shot = (Projectile) e.getDamager();
        if (shot == null) {
            return;
        }
        if (!(shot.getShooter() instanceof Player)) {
            return;
        }
        Player shooter = (Player) shot.getShooter();
        if (!(e.getEntity() instanceof Player)) return;

        if (PlayerKitSelection.selectedKit.get(shooter.getUniqueId()) == null || !PlayerKitSelection.selectedKit.get(shooter.getUniqueId()).equals("robin")) return;

        if(!shooter.getItemInHand().equals(CustomItems.getRobinhoodBow())) return;

        shooter.getInventory().addItem(new ItemStack(Material.ARROW));

        e.setDamage(e.getDamage() * 0.65);

        if(Math.random() < 0.2) {
            if(plugin.getGame(shooter) != null) plugin.getGame(shooter).addGold(shooter, 20);
        }
    }

    public void use(Player p){
        playerFallDamage.use(p);
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();

        if(e.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        if (PlayerKitSelection.selectedKit.get(p.getUniqueId()) == null || !PlayerKitSelection.selectedKit.get(p.getUniqueId()).equals("robin")) return;

        if((playerFallDamage.found(p) &&  playerFallDamage.ready(p))) return;

        e.setCancelled(true);


    }
}