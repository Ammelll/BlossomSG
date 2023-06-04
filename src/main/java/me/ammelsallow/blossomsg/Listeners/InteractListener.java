package me.ammelsallow.blossomsg.Listeners;

import me.ammelsallow.blossomsg.*;
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

public class InteractListener implements Listener {
    private final HashMap<UUID, Long> cooldown;

    private BlossomSG plugin;
    public InteractListener(BlossomSG _plugin){
        this.plugin = _plugin;
        this.cooldown = new HashMap<>();
    }
    private ArrayList<Chest> openedChests= new ArrayList<>();

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getClickedBlock() != null) {
            if(event.getClickedBlock().getType() == Material.ENDER_CHEST){
                Block clickedBlock = event.getClickedBlock();
                Location blockLocation = clickedBlock.getLocation();
                World blockWorld = clickedBlock.getWorld();
                clickedBlock.setType(Material.AIR);
                //Items
                //5 XP BOTTLES
                // Iron Chestplate prot 1
                // Iron Leggings prot 1
                // 3 Unstacked diamonds (diff metadata)?
                //5 unstacked iron ingots
                //Drinkable speed 2 (20 seconds)
                //Splashable Instant Health 1 Not added f potions
                //Fishing rod 8 uses
                // Bow 12 uses Not added lazy
                // FNS (10 Uses) Not added lazy short stuff
                ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
                ItemStack potion = new Potion(PotionType.SPEED).toItemStack(1);
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED,400,0),true);
                potion.setItemMeta(potionMeta);
                leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
                chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);

                ItemStack diamond = new ItemStack(Material.DIAMOND,1);
                net.minecraft.server.v1_8_R3.ItemStack NMSDiamond = CraftItemStack.asNMSCopy(diamond);
                NBTTagCompound diamondCompound = (NMSDiamond.hasTag()) ? NMSDiamond.getTag() : new NBTTagCompound();
                diamondCompound.set("diamondFromSupplyDrop",new NBTTagInt(1));
                NMSDiamond.setTag(diamondCompound);
                diamond = CraftItemStack.asBukkitCopy(NMSDiamond);

                ItemStack iron = new ItemStack(Material.IRON_INGOT,1);
                net.minecraft.server.v1_8_R3.ItemStack NMSIron = CraftItemStack.asNMSCopy(iron);
                NBTTagCompound ironCompound = (NMSIron.hasTag()) ? NMSIron.getTag() : new NBTTagCompound();
                ironCompound.set("ironFromSupplyDrop",new NBTTagInt(1));
                NMSIron.setTag(ironCompound);
                iron = CraftItemStack.asBukkitCopy(NMSIron);
                double x = blockLocation.getX();
                double y = blockLocation.getY();
                double z = blockLocation.getZ();
                ArrayList<ItemLocation> itemLocations = new ArrayList<>();
                itemLocations.add(new ItemLocation(new Location(blockLocation.getWorld(),x+2,y+2,z+2), new ItemStack(Material.EXP_BOTTLE,5)));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x+2,y+2,z+1)), chestplate));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x+2,y+2,z)),new ItemStack(Material.FISHING_ROD,1, (short) 58)));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x+2,y+2,z-1.5)), diamond));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x+1,y+1.5,z-1.5)), diamond));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x+1,y+1.75,z-1.25)), diamond));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x+0.76,y+2.25,z-2)), leggings));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x,y+2,z)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x-1.5,y,z+1.5)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x+1.6,y+2,z)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x-1,y+2,z+3)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x-2,y+2,z-1)), iron));
                itemLocations.add(new ItemLocation((new Location(blockLocation.getWorld(),x,y,z)), potion));
                AtomicInteger playerCount = new AtomicInteger();
                blockWorld.getNearbyEntities(blockLocation, 10, 5, 10).forEach(entity -> {
                  if(entity instanceof Player){
                      playerCount.getAndIncrement();
                  }
                });
                for(int i = 0; i <  playerCount.get() *3; i++){
                    int  random = (int) (Math.random()*itemLocations.size());
                    blockWorld.dropItem(itemLocations.get(random).getLocation(),itemLocations.get(random).getItemStack());
                }


                event.setCancelled(true);
            }
            if (event.getClickedBlock().getType() == Material.CHEST && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Chest chest = (Chest) event.getClickedBlock().getState();
                if (firstopen(chest.getMetadata("opened"))) {
                    randomizeLoot(chest);
                    openedChests.add(chest);
                } else {
                    player.sendMessage("not first open!");
                }

                chest.setMetadata("opened", new FixedMetadataValue(plugin, true));

            }
        }

        if (player.getItemInHand().equals(CustomItems.getCompassSelector())) {
            plugin.openMainMenu(player);
            for(Chest chest : openedChests){
                chest.removeMetadata("opened",plugin);
            }
        } else if (player.getItemInHand().equals(CustomItems.getEndermanEnderheart())) {
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
        } else if(player.getItemInHand().equals(CustomItems.getRobinhoodBow())){
            if (PlayerKitSelection.selectedKit.get(player.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(player.getUniqueId()).equals("robin")) {
                Action action = event.getAction();
                Player p = event.getPlayer();
                if (event.getItem() != null && event.getItem().getType().equals(Material.BOW) && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
                    if (!this.cooldown.containsKey(p.getUniqueId())) {
                        this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                        p.setVelocity(p.getLocation().getDirection().multiply(1.4).setY(0.7));
                    } else {
                        long timeElapsed = System.currentTimeMillis() - this.cooldown.get(p.getUniqueId());
                        if (timeElapsed >= 10000) {
                            this.cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                            p.setVelocity(p.getLocation().getDirection().multiply(1.4).setY(0.7));
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You cannot use that ability for (" + Math.floor(((10000 - timeElapsed) * 10) / 1000.0) / 10 + "s)");

                        }
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
    private boolean firstopen(List<MetadataValue> values){
        boolean fo = true;
        for(MetadataValue value : values){
            if(value.asBoolean()){
                return false;
            }
        }
        return true;
    }
    public void randomizeLoot(Chest unr){
        Short s = 62;
        LootItem[] lootTable = {new LootItem(new ItemStack(Material.GOLDEN_APPLE),0.2), new LootItem(new ItemStack(Material.DIAMOND),0.3), new LootItem(new ItemStack(Material.IRON_INGOT),0.4),new LootItem(new ItemStack(Material.IRON_LEGGINGS),0.5),new LootItem(new ItemStack(Material.IRON_CHESTPLATE),0.5), new LootItem(new ItemStack(Material.EXP_BOTTLE,3),0.5), new LootItem(new ItemStack(Material.FISHING_ROD,1,s),0.75), new LootItem(new ItemStack(Material.IRON_BOOTS),0.6), new LootItem(new ItemStack(Material.STONE_SWORD),1), new LootItem(new ItemStack(Material.BOW),1.2), new LootItem(new ItemStack(Material.IRON_HELMET),1.2),new LootItem(new ItemStack(Material.COOKED_BEEF,3),1.2), new LootItem(new ItemStack(Material.CHAINMAIL_LEGGINGS),1.3), new LootItem(new ItemStack(Material.CHAINMAIL_HELMET),1.4), new LootItem(new ItemStack(Material.CHAINMAIL_BOOTS),1.6), new LootItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE),1.6),new LootItem(new ItemStack(Material.GOLD_BOOTS),2), new LootItem(new ItemStack(Material.GOLD_LEGGINGS),2), new LootItem(new ItemStack(Material.GOLD_CHESTPLATE),2),new LootItem(new ItemStack(Material.GOLD_HELMET),2),new LootItem(new ItemStack(Material.STICK),2), new LootItem(new ItemStack(Material.FEATHER),2), new LootItem(new ItemStack(Material.FLINT),2), new LootItem(new ItemStack(Material.WOOD_SWORD),2.5), new LootItem(new ItemStack(Material.STONE_AXE),3), new LootItem(new ItemStack(Material.LEATHER_CHESTPLATE),4)};
        Inventory inv = unr.getBlockInventory();
        for(int chestIndex = 0; chestIndex < 27; chestIndex++){

           for(LootItem li : lootTable){

               double random = Math.random() * 300;

               if(random < li.getPercentChance()){
                   inv.setItem(chestIndex,li.getItem());
                   break;
               }
           }
       }
    }
}
