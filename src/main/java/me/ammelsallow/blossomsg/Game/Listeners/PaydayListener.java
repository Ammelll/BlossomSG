package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.Kits.Enderman.Misc.ItemLocation;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

public class PaydayListener implements Listener {

    @EventHandler
    public void BlockPlaceListener(BlockPlaceEvent e){
        if(e.getBlockPlaced().getType() != Material.CHEST) return;

        ItemStack item = e.getItemInHand();
        net.minecraft.server.v1_8_R3.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsitem.getTag();

        if(tag == null) return;
        ArrayList<ItemLocation> itemLocations = new ArrayList<>();
        Location loc = e.getBlock().getLocation().add(0,0.75,0);


        ItemStack stone_sword = new ItemStack(Material.STONE_SWORD,1);
        stone_sword.addEnchantment(Enchantment.DAMAGE_ALL,1);
        itemLocations.add(new ItemLocation(loc, stone_sword));

        Potion splashableSpeedPots = new Potion(PotionType.SPEED,1,true);
        ItemStack splashableSpeed = splashableSpeedPots.toItemStack(1);
        PotionMeta potionMeta = (PotionMeta) splashableSpeed.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 160, 1), false);
        splashableSpeed.setItemMeta(potionMeta);
        itemLocations.add(new ItemLocation(loc,splashableSpeed));

        itemLocations.add(payDayItemLocationHelper(loc,Material.EXP_BOTTLE,2));
        itemLocations.add(new ItemLocation(loc, CustomItems.getTickler()));
        itemLocations.add(payDayItemLocationHelper(loc,Material.IRON_INGOT,1));
        itemLocations.add(payDayItemLocationHelper(loc,Material.ARROW,4));
        itemLocations.add(payDayItemLocationHelper(loc,Material.COOKED_BEEF,16));
        itemLocations.add(payDayItemLocationHelper(loc,Material.STRING,3));
        itemLocations.add(new ItemLocation(loc, new ItemStack(Material.DIAMOND_HELMET,1)));
        itemLocations.add(new ItemLocation(loc, new ItemStack(Material.IRON_LEGGINGS,1)));
        itemLocations.add(new ItemLocation(loc, new ItemStack(Material.IRON_CHESTPLATE,1)));
        itemLocations.add(new ItemLocation(loc, new ItemStack(Material.DIAMOND_BOOTS,1)));

        item.setAmount(item.getAmount()-1);
        if(item.getAmount() <= 0){
            e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
        } else{
            e.getPlayer().setItemInHand(item);
        }
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * itemLocations.size());
            loc.getWorld().dropItem(itemLocations.get(random).getLocation(), itemLocations.get(random).getItemStack());
            itemLocations.remove(random);
        }
        e.setCancelled(true);
    }
    public ItemLocation payDayItemLocationHelper(Location location, Material m, int amount){
        return new ItemLocation(location, new ItemStack(m,amount));
    }
}
