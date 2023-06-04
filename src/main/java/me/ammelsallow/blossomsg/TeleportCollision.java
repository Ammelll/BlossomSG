package me.ammelsallow.blossomsg;

import org.bukkit.entity.Player;

public class TeleportCollision {
    private boolean collision;
    private Player player;
    public TeleportCollision(boolean collision, Player player){
        this.collision = collision;
        this.player = player;
    }
    public TeleportCollision(boolean collision){
        this.collision = collision;
        this.player = null;
    }

    public boolean getCollision() {
        return collision;
    }

    public Player getPlayer() {
        return player;
    }
}
