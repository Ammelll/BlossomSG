package me.ammelsallow.blossomsg.Commands;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.CustomItems;
import me.ammelsallow.blossomsg.Listeners.MenuListener;
import me.ammelsallow.blossomsg.Mobs.ArmorStandNoClip;
import me.ammelsallow.blossomsg.PlayerKitSelection;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

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
    public static class KitMenuCommand implements CommandExecutor {


        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            Player p = (Player) commandSender;
            Inventory menu = Bukkit.createInventory(p, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Kit Selector");
            addEndermanKit(menu);
            addRobinhoodKit(menu);
            addNethermageKit(menu);
            loadSelectedKit(p, menu);
            p.openInventory(menu);
            return true;
        }
        public void addEndermanKit(Inventory menu){
            ItemStack emanKit = new ItemStack(Material.ENDER_PEARL);
            ItemMeta emanMeta = emanKit.getItemMeta();
            ArrayList<String> emanLore = new ArrayList<>();
            emanLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Enderheart");
            emanLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Enderman Block x7");
            emanMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Enderman");
            emanMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            emanMeta.setLore(emanLore);
            emanKit.setItemMeta(emanMeta);
            menu.setItem(3,emanKit);
        }
        public void addRobinhoodKit(Inventory menu){
            ItemStack robinKit = new ItemStack(Material.BOW);
            ItemMeta robinMeta = robinKit.getItemMeta();
            ArrayList<String> robinLore = new ArrayList<>();
            robinLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Robinhood's Bow");
            robinLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Arrow x7");
            robinMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Robinhood");
            robinMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            robinMeta.setLore(robinLore);
            robinKit.setItemMeta(robinMeta);
            menu.setItem(4,robinKit);
        }
        public void addNethermageKit(Inventory menu){
            ItemStack netherKit = new ItemStack(Material.FIREBALL);
            ItemMeta netherMeta = netherKit.getItemMeta();
            ArrayList<String> netherLore = new ArrayList<>();
            netherLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Nethermage's Axe");
            netherLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Potion of Fire x3");
            netherMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Nethermage");
            netherMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            netherMeta.setLore(netherLore);
            netherKit.setItemMeta(netherMeta);
            menu.setItem(5,netherKit);
        }
        public void loadSelectedKit(Player p, Inventory menu){
            if(PlayerKitSelection.selectedKit.containsKey(p.getUniqueId())){
                String kit = PlayerKitSelection.selectedKit.get(p.getUniqueId());
                switch (kit){
                    case "ender":
                        menu.setItem(3, MenuListener.addGlow(menu,3));
                        break;
                    case "robin":
                        menu.setItem(4,MenuListener.addGlow(menu,4));
                        break;
                    case "nether":
                        menu.setItem(5,MenuListener.addGlow(menu,5));
                        break;
                }
            }

        }
    }
}
