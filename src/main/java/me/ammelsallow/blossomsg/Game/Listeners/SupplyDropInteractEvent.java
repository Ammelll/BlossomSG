package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Kits.Enderman.Misc.ItemLocation;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SupplyDropInteractEvent implements Listener {

    private BlossomSG plugin;


    private ArrayList<Chest> openedChests = new ArrayList<>();

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                Block clickedBlock = event.getClickedBlock();
                Location blockLocation = clickedBlock.getLocation();
                World blockWorld = clickedBlock.getWorld();
                clickedBlock.setType(Material.AIR);

                ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
                ItemStack potion = new Potion(PotionType.SPEED,2).toItemStack(1);
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 400, 0), true);
                potion.setItemMeta(potionMeta);
                leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

                ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
                net.minecraft.server.v1_8_R3.ItemStack NMSDiamond = CraftItemStack.asNMSCopy(diamond);
                NBTTagCompound diamondCompound = (NMSDiamond.hasTag()) ? NMSDiamond.getTag() : new NBTTagCompound();
                diamondCompound.set("diamondFromSupplyDrop", new NBTTagInt(1));
                NMSDiamond.setTag(diamondCompound);
                diamond = CraftItemStack.asBukkitCopy(NMSDiamond);

                ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);
                net.minecraft.server.v1_8_R3.ItemStack NMSIron = CraftItemStack.asNMSCopy(iron);
                NBTTagCompound ironCompound = (NMSIron.hasTag()) ? NMSIron.getTag() : new NBTTagCompound();
                ironCompound.set("ironFromSupplyDrop", new NBTTagInt(1));
                NMSIron.setTag(ironCompound);
                iron = CraftItemStack.asBukkitCopy(NMSIron);
                double x = blockLocation.getX();
                double y = blockLocation.getY();
                double z = blockLocation.getZ();
                ArrayList<ItemLocation> itemLocations = new ArrayList<>();
                itemLocations.add(new ItemLocation(new Location(blockLocation.getWorld(), x + 2, y + 2, z + 2), new ItemStack(Material.EXP_BOTTLE, 5)));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x + 2, y + 2, z + 1)), chestplate));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x + 2, y + 2, z)), new ItemStack(Material.FISHING_ROD, 1, (short) 58)));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x + 2, y + 2, z - 1.5)), diamond));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x + 1, y + 1.5, z - 1.5)), diamond));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x + 1, y + 1.75, z - 1.25)), diamond));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x + 0.76, y + 2.25, z - 2)), leggings));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x, y + 2, z)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x - 1.5, y, z + 1.5)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x + 1.6, y + 2, z)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x - 1, y + 2, z + 3)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x - 2, y + 2, z - 1)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(), x, y, z)), potion));
                AtomicInteger playerCount = new AtomicInteger();
                blockWorld.getNearbyEntities(blockLocation, 10, 5, 10).forEach(entity -> {
                    if (entity instanceof Player) {
                        playerCount.getAndIncrement();
                    }
                });
                for (int i = 0; i < playerCount.get() * 3; i++) {
                    int random = (int) (Math.random() * itemLocations.size());
                    blockWorld.dropItem(itemLocations.get(random).getLocation(), itemLocations.get(random).getItemStack());
                }


                event.setCancelled(true);
            }
        }
    }
}




