package me.ammelsallow.blossomsg.Game.Listeners;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;

public class PlayerDeathListener implements Listener {

    private BlossomSG plugin;
    public PlayerDeathListener(BlossomSG plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player p = e.getEntity();

        PlayerConnection connection = ((CraftPlayer) p.getPlayer()).getHandle().playerConnection;
        String titleString = ChatColor.RED + "" + ChatColor.BOLD + "You Died!";
        IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a("{'text': ' " + titleString + " ' }");
        PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, text,1, 80,1);
        connection.sendPacket(packet);
        Player killer = p.getKiller();
        if(killer != null){
            if(plugin.getGame(killer) != null) {
                plugin.getGame(killer).addKill(killer);
                plugin.getGame(killer).addGold(killer,150);
            }
        }
        if(plugin.getGame(p) != null){
            plugin.getGame(p).addDeath(p);
        }
        for(Game g : plugin.getGames()){
            g.getGameQueueHandler().leave(p);
        }

    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        Inventory i = p.getInventory();
        i.clear();
        i.setItem(0, CustomItems.getCompassSelector());
    }
}