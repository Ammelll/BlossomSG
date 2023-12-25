package me.ammelsallow.blossomsg.Game.Commands;

import me.ammelsallow.blossomsg.Game.Listeners.KitSelectorMenuListener;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitMenuCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        Inventory menu = Bukkit.createInventory(p, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Kit Selector");
        addEndermanKit(menu);
        addRobinhoodKit(menu);
        addNethermageKit(menu);
        addLumberjackKit(menu);
        addFrankensteinKit(menu);
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
        menu.setItem(6,lumberKit);
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
        menu.setItem(7,frankKit);

    }
    public void loadSelectedKit(Player p, Inventory menu){
        if(PlayerKitSelection.selectedKit.containsKey(p.getUniqueId())){
            String kit = PlayerKitSelection.selectedKit.get(p.getUniqueId());
            switch (kit){
                case "ender":
                    menu.setItem(3, KitSelectorMenuListener.addGlow(menu,3));
                    break;
                case "robin":
                    menu.setItem(4,KitSelectorMenuListener.addGlow(menu,4));
                    break;
                case "nether":
                    menu.setItem(5,KitSelectorMenuListener.addGlow(menu,5));
                    break;
                case "lumber":
                    menu.setItem(6,KitSelectorMenuListener.addGlow(menu,6));
                    break;
                case "frank":
                    menu.setItem(7,KitSelectorMenuListener.addGlow(menu,7));
                    break;
            }
        }

    }
}