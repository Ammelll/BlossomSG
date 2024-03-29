package me.ammelsallow.blossomsg.Game.Mobs;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class Frankenstein extends EntityIronGolem {
    private Player player;
    private BlossomSG plugin;
    private Frankenstein frankenstein;
    private World world;
    private Location currentLocation;
    public Frankenstein(Location location, Player player, BlossomSG plugin) {
        super(((CraftWorld)location.getWorld()).getHandle());
        this.setPosition(location.getX(),location.getY()+1,location.getZ());
        this.setCustomName(ChatColor.GRAY + "" + ChatColor.BOLD + player.getName() + "'s Frankenstein");
        this.setCustomNameVisible(true);
        this.player = player;
        this.plugin = plugin;
        this.world = player.getWorld();
        this.frankenstein = this;
    }
    public void spawn(){
        WorldServer ws = ((CraftWorld) world).getHandle();
        ws.addEntity(this);
        spawnIronInvoke();
    }
    public void spawnIronInvoke(){
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                currentLocation = new Location(world,frankenstein.locX,frankenstein.locY,frankenstein.locZ);
                frankenstein.setHealth(Math.min(100,frankenstein.getHealth()+9));
                world.dropItem(currentLocation, new ItemStack(Material.IRON_INGOT));
                evaluateTarget();
            }
        },0,80);
    }
    public void evaluateTarget(){
        Game g = plugin.getGame(player);
        if(g != null){

            if(world.getNearbyEntities(currentLocation,10,10,10).stream().filter(e -> e instanceof Player).count() == 0){
                return;
            }
            List<Entity> nearByPlayers = world.getNearbyEntities(currentLocation,10,10,10).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
            System.out.println(g.getMobMap() + " MOB MAP");
            for(Entity e : nearByPlayers){
                Player targetPlayer = (Player) e;
//                if(g.getMobMap().get(targetPlayer.getUniqueId()).get(0) == frankenstein.getUniqueID()){
//
//                }
                System.out.println("NEW GOAL");
                System.out.println(targetPlayer.getUniqueId());
                System.out.println(frankenstein.getUniqueID() + " FRANKENSTEIN ID");
                System.out.println(targetPlayer.getName() + " " + targetPlayer.getUniqueId() + " NAME AND ID");
                frankenstein.setGoalTarget((EntityLiving) ((CraftPlayer) targetPlayer).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            }

        }
    }
}

