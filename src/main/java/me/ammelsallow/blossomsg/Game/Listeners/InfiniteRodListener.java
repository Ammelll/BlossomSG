package me.ammelsallow.blossomsg.Game.Listeners;

import net.minecraft.server.v1_8_R3.EntityFishingHook;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class InfiniteRodListener implements Listener {

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e){
        if(e.getDamager().getType() != EntityType.FISHING_HOOK) return;
        Projectile shot = (Projectile) e.getDamager();
        if (!(shot.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) shot.getShooter();
        if(shooter.getItemInHand().getDurability() > 64){
            shooter.setItemInHand(new ItemStack(Material.AIR));
            return;
        }
        shooter.getItemInHand().setDurability((short) (shooter.getItemInHand().getDurability()+1));
    }
}
