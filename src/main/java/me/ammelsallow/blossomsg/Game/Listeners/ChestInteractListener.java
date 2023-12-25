package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Misc.LootItem;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestInteractListener implements Listener {

    private BlossomSG plugin;
    public ChestInteractListener(BlossomSG _plugin){
        this.plugin = _plugin;
    }
    private ArrayList<Chest> openedChests= new ArrayList<>();
    private final Random random = new Random();
    private final LootItem[] lootTable = {getGoldLootItem(10,0.8),getGoldLootItem(1,0.2),getGoldLootItem(5,0.45),new LootItem(new ItemStack(Material.GOLDEN_APPLE), 0.98), new LootItem(new ItemStack(Material.DIAMOND), 0.95), new LootItem(new ItemStack(Material.IRON_INGOT), 0.85), new LootItem(new ItemStack(Material.IRON_LEGGINGS), 0.9), new LootItem(new ItemStack(Material.IRON_CHESTPLATE), 0.9), new LootItem(new ItemStack(Material.EXP_BOTTLE, 3), 0.8), new LootItem(new ItemStack(Material.FISHING_ROD, 1, (short) 62), 0.8), new LootItem(new ItemStack(Material.IRON_BOOTS), 0.94), new LootItem(new ItemStack(Material.STONE_SWORD), 0.9), new LootItem(new ItemStack(Material.BOW), 0.9), new LootItem(new ItemStack(Material.IRON_HELMET), 0.9), new LootItem(new ItemStack(Material.COOKED_BEEF, 3), 0.25), new LootItem(new ItemStack(Material.CHAINMAIL_LEGGINGS), 0.9), new LootItem(new ItemStack(Material.CHAINMAIL_HELMET), 0.8), new LootItem(new ItemStack(Material.CHAINMAIL_BOOTS), 0.8), new LootItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE), 0.8), new LootItem(new ItemStack(Material.GOLD_BOOTS), 0.6), new LootItem(new ItemStack(Material.GOLD_LEGGINGS), 0.6), new LootItem(new ItemStack(Material.GOLD_CHESTPLATE), 0.6), new LootItem(new ItemStack(Material.GOLD_HELMET), 0.6), new LootItem(new ItemStack(Material.STICK), 0.5), new LootItem(new ItemStack(Material.FEATHER), 0.5), new LootItem(new ItemStack(Material.FLINT), 0.5), new LootItem(new ItemStack(Material.WOOD_SWORD), 0.5), new LootItem(new ItemStack(Material.STONE_AXE), 0.5), new LootItem(new ItemStack(Material.LEATHER_CHESTPLATE), 0.4),new LootItem(new ItemStack(Material.IRON_PICKAXE), 0.93),new LootItem(new ItemStack(Material.STONE_PICKAXE), 0.6)};

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null) {

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
            openMainMenu(player);
            for (Chest chest : openedChests) {
                chest.removeMetadata("opened", plugin);
            }

        }
    }
    private LootItem getGoldLootItem(int quantity, double chance){
        ItemStack itemStack = new ItemStack(Material.GOLD_INGOT,quantity);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" +  quantity + " Gold");
        itemStack.setItemMeta(meta);
        return new LootItem(itemStack,chance);
    }
    private boolean firstopen (List <MetadataValue> values) {
        boolean fo = true;
        for (MetadataValue value : values) {
            if (value.asBoolean()) {
                return false;
            }
        }
        return true;
    }

    public void randomizeLoot (Chest unr){
        List<ItemStack> selectedItems = selectItems();
        Inventory inv = unr.getBlockInventory();
        for(int i = 0; i < selectedItems.size() ; i++) {
            ItemStack item = selectedItems.get(random.nextInt(selectedItems.size()));
            selectedItems.remove(item);
            inv.setItem(random.nextInt(27), item);
        }
        unr.update();
    }
    private List<ItemStack> selectItems(){
        List<ItemStack> selected = new ArrayList<>();
        for(LootItem li : lootTable){
            double val = random.nextDouble()+0.01;
            double quantity = ((val/li.getChance()*1.25));
            for(double i = 0.999; i < quantity; i++){selected.add(li.getItem());}
        }
        return selected;
    }
    public void openMainMenu(Player player){
        Inventory inventory = Bukkit.createInventory(player,36, ChatColor.GOLD + "Game Selector");

        ItemStack join = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta joinMeta = join.getItemMeta();
        joinMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Join Blossom SG Queue");
        join.setItemMeta(joinMeta);

        inventory.setItem(12, join);
        player.openInventory(inventory);
    }
}
