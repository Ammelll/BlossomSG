package me.ammelsallow.blossomsg.Kits.Enderman.Tasks;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Kits.Enderman.Listeners.BlockPlaceListener;
import me.ammelsallow.blossomsg.Kits.Enderman.Misc.TempBlock;

import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateTemporaryBlocks extends BukkitRunnable {
    BlossomSG plugin;
    public UpdateTemporaryBlocks(BlossomSG plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(int i = 0; i < BlockPlaceListener.getTempBlockArray().size(); i++){
            TempBlock tempBlock = BlockPlaceListener.getTempBlockArray().get(i);
            if(System.currentTimeMillis() - tempBlock.getTime() > 5000){
                BlockPlaceListener.getTempBlockArray().remove(tempBlock);
                i--;
                Player p = tempBlock.getPlayer();
                if(PlayerKitSelection.selectedKit.get(p.getUniqueId()) != null && PlayerKitSelection.selectedKit.get(p.getUniqueId()).equals("ender")) {
                    tempBlock.getBlock().setType(Material.AIR);
                    Inventory inventory = p.getInventory();
                    ItemStack blocks = CustomItems.getEndermanBlocks();
                    blocks.setAmount(1);
                    inventory.addItem(blocks);
                }
            }
        }
    }

}