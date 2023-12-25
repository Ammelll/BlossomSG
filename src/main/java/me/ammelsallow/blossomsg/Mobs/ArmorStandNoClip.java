package me.ammelsallow.blossomsg.Mobs;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorStandNoClip extends EntityArmorStand {

    public ArmorStandNoClip(World worldServer){
        super(worldServer);
        this.setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "SUPPLY DROP");
        this.setCustomNameVisible(true);

        this.noclip = true;
    }

    public ArmorStand spawn(Location location){
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        final ArmorStandNoClip customEntity = new ArmorStandNoClip(mcWorld);
        customEntity.setLocation(location.getX(),location.getY(),location.getZ(),location.getYaw(),location.getPitch());

        mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (ArmorStand) customEntity.getBukkitEntity();
    }
}