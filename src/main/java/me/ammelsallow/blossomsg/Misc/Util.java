package me.ammelsallow.blossomsg.Misc;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Util {
    public static final void addHealth(Damageable e, int amount){
        e.setHealth(Math.min(e.getMaxHealth(),e.getHealth()+amount));
    }
}
