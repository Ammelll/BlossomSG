package me.ammelsallow.blossomsg.Game.Commands;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Listeners.KitSelectorMenuListener;
import me.ammelsallow.blossomsg.Kits.Kit;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitMenuCommand implements CommandExecutor {



    //!! This class is not very DRY
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        Inventory menu = Bukkit.createInventory(p, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Kit Selector");
        addEndermanKit(menu);
        addRobinhoodKit(menu);
        addNethermageKit(menu);
        addLumberjackKit(menu);
        addFrankensteinKit(menu);
        addCaptainKit(menu);
        addShepherdKit(menu);
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
        menu.setItem(0,emanKit);
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
        menu.setItem(1,robinKit);
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
        menu.setItem(2,netherKit);
    }
    public void addLumberjackKit(Inventory menu){
        ItemStack lumberKit = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta lumberMeta = lumberKit.getItemMeta();
        ArrayList<String> lumberLore = new ArrayList<>();
        lumberLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Lumberjack's Axe");
        lumberMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Lumberjack");
        lumberMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lumberMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        lumberMeta.setLore(lumberLore);
        lumberKit.setItemMeta(lumberMeta);
        menu.setItem(3,lumberKit);
    }
    public void addFrankensteinKit(Inventory menu){
        ItemStack frankKit = new ItemStack(Material.RED_ROSE);
        ItemMeta frankMeta = frankKit.getItemMeta();
        List<String> frankLore = new ArrayList<>();
        frankLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Monster Spawn Egg");
        frankLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Poppy x3");
        frankMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Frankenstein");
        frankMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        frankMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        frankMeta.setLore(frankLore);
        frankKit.setItemMeta(frankMeta);
        menu.setItem(4,frankKit);
    }
    public void addCaptainKit(Inventory menu){
        ItemStack captKit = new ItemStack(Material.BOAT);
        ItemMeta capMeta = captKit.getItemMeta();
        List<String> capLore = new ArrayList<>();
        capLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Fishing Rod : 6 Uses");
        capLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Boat 5x");
        capMeta.setLore(capLore);
        capMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Captain");
        capMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        capMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        captKit.setItemMeta(capMeta);
        menu.setItem(5,captKit);
    }
    public void addShepherdKit(Inventory menu){
        ItemStack shepKit = new ItemStack(Material.WHEAT);
        ItemMeta shepMeta = shepKit.getItemMeta();
        List<String> shepLore = new ArrayList<>();
        shepLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Crook");
        shepLore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "Sheep Spawn Egg x3");
        shepLore.add(ChatColor.WHITE + "" +ChatColor.ITALIC  + "Wheat x5");
        shepMeta.setLore(shepLore);
        shepMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        shepMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        shepMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Shepherd");
        shepKit.setItemMeta(shepMeta);
        menu.setItem(6,shepKit);
    }
    public void loadSelectedKit(Player p, Inventory menu){
        if(PlayerKitSelection.selectedKit.containsKey(p.getUniqueId())){
            String stringID = PlayerKitSelection.selectedKit.get(p.getUniqueId());
            for(int i = 0; i < BlossomSG.KITS.length; i++){
                Kit kit = BlossomSG.KITS[i];
                if(stringID.equals(kit.stringID)){
                    menu.setItem(i,KitSelectorMenuListener.addGlow(menu,i));
                }
            }
        }

    }
}