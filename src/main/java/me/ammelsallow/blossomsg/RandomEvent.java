package me.ammelsallow.blossomsg;

import me.ammelsallow.blossomsg.Mobs.ArmorStandNoClip;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.FallingBlock;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class RandomEvent {

    String eventType;
    int eventInt;
    Game game;
    int save;
    ArmorStand drop;
    int taskID;

    public RandomEvent(int i, Game game) {
        eventInt = i;
        this.game = game;
        switch (eventInt) {
            case 0:
                eventType = "SupplyDrop";
                break;
            case 1:
                eventType = "PayDay";
                break;
            case 2:
                eventType = "Juggernaut";
                break;
            default:
                eventType = "noEvent";
                break;
        }
    }

    public void trigger() {
        switch (eventType) {
            case "SupplyDrop":
                Bukkit.broadcastMessage("SUPPLYDROP");
                summonSupplyDrop();
                break;
            case "PayDay":
                givePayDay();
                Bukkit.broadcastMessage("PAYDAY");
                break;
            case "Juggernaut":
                juggernautInitiate();
                Bukkit.broadcastMessage("JUGGERNAUT");
                break;
        }
    }

    private void juggernautInitiate(){
        for(Player p : game.getPlayers()){
            PotionEffect potionEffectHealthBoost = new PotionEffect(PotionEffectType.HEALTH_BOOST,1000000,2);
            PotionEffect potionEffectRegeneration= new PotionEffect(PotionEffectType.REGENERATION,1000000,0);
            p.addPotionEffects(Arrays.asList(new PotionEffect[]{potionEffectRegeneration, potionEffectHealthBoost}));
        }
    }
    private void givePayDay(){
        for(Player p : game.getPlayers()){
            p.getInventory().addItem(getPayDay());
            Inventory i = p.getInventory();

        }
    }

    private void summonSupplyDrop() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable() {
            int countDown = 16;

            @Override
            public void run() {
                if(countDown > 1){
                    countDown--;
                    for(Player p : game.getPlayers()){
                        p.sendMessage(Game.getSGPrefix() + ChatColor.RESET +"A supply drop is dropping in " +ChatColor.RED + "" +ChatColor.BOLD + countDown + ChatColor.RESET + " seconds");
                    }

                } else{
                    for(Player p : game.getPlayers()){
                        p.sendMessage(Game.getSGPrefix() + ChatColor.RESET + "A supply drop has spawned over the"  + ChatColor.AQUA + "" + ChatColor.BOLD +" Capture Point");
                    }
                    countDown = 16;
                    Bukkit.getScheduler().cancelTask(taskID);
                    Location location = game.getMap().getCenter().add(0,20,0);
                    ArmorStandNoClip asnc = new ArmorStandNoClip(((CraftWorld) location.getWorld()).getHandle());
                    drop = asnc.spawn(location);
                    drop.setHelmet(new ItemStack(Material.ENDER_CHEST));
                    drop.setVisible(false);
                    drop.setHeadPose(new EulerAngle(0f, Math.toRadians(180f), 0f));

                    floatRunnable();
                }
            }
        },0L,20L);


    }

    private void floatRunnable() {
        save = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable() {
            @Override
            public void run() {
                drop.setVelocity((new Vector(0, -0.1, 0)));
                if ((int) (drop.getLocation().getY()) == drop.getWorld().getHighestBlockYAt(drop.getLocation()) - 2) {
                    Bukkit.getScheduler().cancelTask(save);
                    Block b = drop.getWorld().getBlockAt(drop.getLocation().add(0, 2, 0));
                    b.setType(Material.ENDER_CHEST);
                    drop.teleport(new Location(drop.getWorld(), 0, -100, 0));
                }
            }
        }, 5, 1);
    }
    private ItemStack getPayDay(){
        ItemStack payday = new ItemStack(Material.CHEST);
        ItemMeta paydayMeta = payday.getItemMeta();
        paydayMeta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "PAYDAY");
        net.minecraft.server.v1_8_R3.ItemStack NMSpayday = CraftItemStack.asNMSCopy(payday);
        NBTTagCompound paydayCompound = (NMSpayday.hasTag()) ? NMSpayday.getTag() : new NBTTagCompound();
        paydayCompound.setInt("isPayDay",1);
        NMSpayday.setTag(paydayCompound);
        payday = CraftItemStack.asBukkitCopy(NMSpayday);
        payday.setItemMeta(paydayMeta);
        return payday;
    }
}
