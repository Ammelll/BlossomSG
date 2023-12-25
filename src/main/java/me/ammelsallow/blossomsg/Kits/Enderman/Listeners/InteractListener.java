package me.ammelsallow.blossomsg.Kits.Enderman.Listeners;

import me.ammelsallow.blossomsg.Kits.Enderman.Misc.TeleportCollision;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class InteractListener implements Listener {
    private final HashMap<UUID, Long> cooldown;

    public InteractListener(){
        this.cooldown = new HashMap<>();
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().equals(CustomItems.getEndermanEnderheart())) {
            Location l = player.getLocation();
            World w = player.getWorld();
            double yaw = l.getYaw();
            double pitch = l.getPitch();
            double fraction = 1 - Math.abs(Math.sin(Math.toRadians(pitch)));
            double yOffset = -Math.sin(Math.toRadians(pitch)) * 10;
            double xOffset = -Math.sin(Math.toRadians(yaw)) * 10 * fraction;
            double zOffset = Math.cos(Math.toRadians(yaw)) * 10 * fraction;
            TeleportCollision tp;
            Player collidedPlayer = player;
            if (PlayerKitSelection.selectedKit.get(player.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(player.getUniqueId()).equals("ender")) {
                if (!this.cooldown.containsKey(player.getUniqueId())) {
                    player.playSound(l, Sound.ENDERMAN_SCREAM,1,1);
                    this.cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    for (int i = 11; i > 0; i--) {
                        Location desiredLocationY = new Location(w, l.getX() + (xOffset / i), l.getY() + (yOffset / i), l.getZ() + (zOffset / i), l.getYaw(), l.getPitch());
                        Location desiredLocationNoY = new Location(w, l.getX() + (xOffset / i), l.getY(), l.getZ() + (zOffset / i), l.getYaw(), l.getPitch());
                        tp = tryTeleport(desiredLocationNoY, player);
                        if (tp.getCollision()) {
                            collidedPlayer = tp.getPlayer();
                        }
                        tp = tryTeleport(desiredLocationY, player);
                        if (tp.getCollision()) {
                            collidedPlayer = tp.getPlayer();
                        }
                    }
                    collidedPlayer.damage(3);
                } else {
                    long timeElapsed = System.currentTimeMillis() - this.cooldown.get(player.getUniqueId());

                    if (timeElapsed >= 10000) {
                        player.playSound(l,Sound.ENDERMAN_SCREAM,1,1);
                        this.cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                        for (int i = 11; i > 0; i--) {
                            Location desiredLocationY = new Location(w, l.getX() + (xOffset / i), l.getY() + (yOffset / i), l.getZ() + (zOffset / i), l.getYaw(), l.getPitch());
                            Location desiredLocationNoY = new Location(w, l.getX() + (xOffset / i), l.getY(), l.getZ() + (zOffset / i), l.getYaw(), l.getPitch());
                            tp = tryTeleport(desiredLocationNoY, player);
                            if (tp.getCollision()) {
                                collidedPlayer = tp.getPlayer();
                            }
                            tp = tryTeleport(desiredLocationY, player);
                            if (tp.getCollision()) {
                                collidedPlayer = tp.getPlayer();
                            }
                        }
                        collidedPlayer.damage(3);
                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You cannot teleport yet! (" + Math.floor(((10000 - timeElapsed) * 10) / 1000.0) / 10 + "s)");
                    }
                }
            }
        }
    }
    private TeleportCollision tryTeleport(Location desiredLocation, Player p){
        Location l = p.getLocation();
        World w = p.getWorld();
        double yaw = l.getYaw();
        double pitch = l.getPitch();
        double fraction = 1 - Math.abs(Math.sin(Math.toRadians(pitch)));
        double xOffset = -Math.sin(Math.toRadians(yaw)) * 10 * fraction;
        double zOffset = Math.cos(Math.toRadians(yaw)) * 10 * fraction;
        if(desiredLocation.getBlock().getType().equals(Material.AIR) || desiredLocation.getBlock().getType().equals(Material.WATER)) {
            for(int j = 0; j < 10; j++){

                w.playEffect(new Location(w,l.getX()+(xOffset/j),l.getY()+1,l.getZ()+(zOffset/j),l.getYaw(),l.getPitch()), Effect.WITCH_MAGIC,0);
            }
            p.teleport(desiredLocation);
        }
        return collisionCheck(desiredLocation, p);
    }
    private TeleportCollision collisionCheck(Location l, Player player){
        World w = l.getWorld();
        for(Entity e : w.getNearbyEntities(l,1.5,1.5,1.5)){
            if(e instanceof Player){
                if(!((Player) e).getDisplayName().equals(player.getDisplayName())){
                    return new TeleportCollision(true,(Player) e);
                }
            }
        }
        return new TeleportCollision(false);
    }

}