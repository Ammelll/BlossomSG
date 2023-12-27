package me.ammelsallow.blossomsg.Game.Commands;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Mobs.ArmorStandNoClip;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class giveCompass implements CommandExecutor {
    int save;

    BlossomSG plugin;
    ArmorStand drop;
    public giveCompass(BlossomSG plu){
        this.plugin = plu;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player p = (Player) commandSender;
        Inventory i = p.getInventory();
        i.setItem(0, CustomItems.getEndermanEnderheart());
        i.setItem(1, CustomItems.getEndermanBlocks());
        i.setItem(2, CustomItems.getNethermageAxe());
        i.setItem(3, CustomItems.getNethermagePotions());
        i.setItem(4, CustomItems.getRobinhoodBow());
        i.setItem(5, CustomItems.getCompassSelector());
        i.setItem(6,CustomItems.getFrankensteinEgg());
        i.setItem(7,CustomItems.getFrankensteinRoses());
        p.setGameMode(GameMode.CREATIVE);
        resetScoreboard(p);
//        p.setWalkSpeed(0.2f);
//        p.getInventory().addItem(getPayDay());
//        summonSupplyDrop();

        return true;
    }
    private ItemStack getPayDay(){
        ItemStack payday = new ItemStack(Material.CHEST);
        ItemMeta paydayMeta = payday.getItemMeta();
        paydayMeta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "PAYDAY");
        net.minecraft.server.v1_8_R3.ItemStack NMSpayday = CraftItemStack.asNMSCopy(payday);
        NBTTagCompound paydayCompound = (NMSpayday.hasTag()) ? NMSpayday.getTag() : new NBTTagCompound();
        paydayCompound.setInt("isPayDay",2);
        NMSpayday.setTag(paydayCompound);
        payday = CraftItemStack.asBukkitCopy(NMSpayday);
        payday.setItemMeta(paydayMeta);
        return payday;
    }
    private void summonSupplyDrop() {
        Location location = new Location(Bukkit.getWorld("sg4"),0.5,50,-2.5);
        ArmorStandNoClip asnc = new ArmorStandNoClip(((CraftWorld)location.getWorld()).getHandle());
        drop = asnc.spawn(location);
        drop.setHelmet(new ItemStack(Material.ENDER_CHEST));
        drop.setVisible(false);
        drop.setHeadPose(new EulerAngle(0f,Math.toRadians(180f),0f));

        floatRunnable();

    }

    private void floatRunnable() {
        save = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                drop.setVelocity((new Vector(0,-0.1,0)));
                if((int)(drop.getLocation().getY()) == drop.getWorld().getHighestBlockYAt(drop.getLocation())-2){
                    Bukkit.getScheduler().cancelTask(save);
                    Block b = drop.getWorld().getBlockAt(drop.getLocation().add(0,2,0));
                    b.setType(Material.ENDER_CHEST);
                    drop.teleport(new Location(drop.getWorld(),0,-100,0));
                }
            }
        },5,1);
    }
    public void resetScoreboard(Player p){
        Scoreboard scoreboard = p.getScoreboard();
        scoreboard.getEntries().forEach(entry -> scoreboard.resetScores(entry));
    }
}
