package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Misc.LootItem;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ChestInteractListener implements Listener {

    private BlossomSG plugin;
    public ChestInteractListener(BlossomSG _plugin){
        this.plugin = _plugin;
        initLootTable();
    }
    private ArrayList<Chest> openedChests= new ArrayList<>();
    private final Random random = new Random();
    private final List<LootItem> lootTable = new ArrayList<>();

    public void initLootTable() {
        //i know the percentages are wrong but i added a 4 times vibes multiplier because i think the documentation is wrong
        double tier0Chance = 1;
        double tier1Chance = 2.8;
        double tier2Chance = 0.8;
        double tier3Chance = 0.36;
        double tier4Chance = 0.04;

        lootTable.add(getGoldLootItem(1, tier0Chance));
        lootTable.add(getGoldLootItem(2, tier1Chance));
        lootTable.add(getGoldLootItem(4, tier2Chance));
        lootTable.add(getGoldLootItem(8, tier3Chance));
        lootTable.add(getGoldLootItem(16, tier4Chance));

        List<ItemStack> tier1Items = new ArrayList<>();

        tier1Items.add(new ItemStack(Material.WOOD_SWORD));
        tier1Items.add(new ItemStack(Material.STONE_PICKAXE));
        tier1Items.add(new ItemStack(Material.STONE_AXE));
        tier1Items.add(new ItemStack(Material.COOKED_BEEF,2));
        tier1Items.add(new ItemStack(Material.CHAINMAIL_HELMET));
        tier1Items.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        tier1Items.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        tier1Items.add(new ItemStack(Material.IRON_BOOTS));
        tier1Items.add(new ItemStack(Material.GOLD_HELMET));
        tier1Items.add(new ItemStack(Material.GOLD_CHESTPLATE));
        tier1Items.add(new ItemStack(Material.IRON_HELMET));

        lootTable.addAll(tier1Items.stream().map(item -> new LootItem(item,tier1Chance/tier1Items.size())).collect(Collectors.toList()));

        List<ItemStack> tier2Items = new ArrayList<>();

        tier2Items.add(new ItemStack(Material.CHAINMAIL_HELMET));
        tier2Items.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        tier2Items.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        tier2Items.add(new ItemStack(Material.IRON_BOOTS));
        tier2Items.add(new ItemStack(Material.GOLD_HELMET));
        tier2Items.add(new ItemStack(Material.GOLD_CHESTPLATE));
        tier2Items.add(new ItemStack(Material.IRON_HELMET));
        tier2Items.forEach(item -> item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1));
        tier2Items.add(new ItemStack(Material.STONE_SWORD));
        tier2Items.add(new ItemStack(Material.IRON_AXE));
        tier2Items.add(new ItemStack(Material.STICK));
        tier2Items.add(new ItemStack(Material.COOKED_BEEF,4));

        lootTable.addAll(tier2Items.stream().map(item -> new LootItem(item,tier2Chance/tier2Items.size())).collect(Collectors.toList()));

        List<ItemStack> tier3Items = new ArrayList<>();

        tier3Items.add(new ItemStack(Material.FISHING_ROD, 1, (short) 62));
        tier3Items.add(new ItemStack(Material.BOW, 1, (short) 380));
        tier3Items.add(new ItemStack(Material.FLINT_AND_STEEL, 1, (short) 60));
        tier3Items.add(new ItemStack(Material.IRON_PICKAXE));
        tier3Items.add(new ItemStack(Material.COOKED_BEEF,8));
        tier3Items.add(CustomItems.getSplashableSpeed2());
        tier3Items.add(new ItemStack(Material.INK_SACK, 4, (short) 4));
        tier3Items.add(new ItemStack(Material.ARROW,2));
        tier3Items.add(new ItemStack(Material.DIAMOND_HELMET));
        tier3Items.add(new ItemStack(Material.IRON_CHESTPLATE));
        tier3Items.add(new ItemStack(Material.IRON_LEGGINGS));
        tier3Items.add(new ItemStack(Material.DIAMOND_BOOTS));

        lootTable.addAll(tier3Items.stream().map(item -> new LootItem(item,tier3Chance/tier3Items.size())).collect(Collectors.toList()));

        List<ItemStack> tier4Items = new ArrayList<>();

        tier4Items.add(new ItemStack(Material.ARROW,6));
        tier4Items.add(new ItemStack(Material.EXP_BOTTLE,4));
        tier4Items.add(new ItemStack(Material.DIAMOND_AXE));
        tier4Items.add(new ItemStack(Material.STRING,3));
        tier4Items.add(CustomItems.getRegenerationPotion());

        lootTable.addAll(tier4Items.stream().map(item -> new LootItem(item,tier4Chance/tier4Items.size())).collect(Collectors.toList()));
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null) {

            if (event.getClickedBlock().getType() == Material.CHEST && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Chest chest = (Chest) event.getClickedBlock().getState();
                if (firstopen(chest.getMetadata("opened"))) {
                    randomizeLoot(chest);
                    openedChests.add(chest);
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

        List<Integer> indexes = new ArrayList<>();

        for(int i = 0; i < 27; i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        for(int i = 0; i < selectedItems.size(); i++){
            ItemStack item = selectedItems.get(i);
            inv.setItem(indexes.get(i),item);
        }

        unr.update();
    }
    private List<ItemStack> selectItems(){
        List<ItemStack> selected = new ArrayList<>();
        for(LootItem li : lootTable){
            double val = random.nextDouble();
            if(val <= li.getChance()){
                selected.add(li.getItem());
            }
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
