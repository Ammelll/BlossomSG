package me.ammelsallow.blossomsg;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TempBlock {
    private Block block;
    private long time;
    private Player player;
    public TempBlock(Block block, long time, Player player){
        this.block = block;
        this.time = time;
        this.player = player;
    }
    public Block getBlock(){
        return this.block;
    }
    public long getTime(){
        return this.time;
    }
    public Player getPlayer(){return this.player;}
}
