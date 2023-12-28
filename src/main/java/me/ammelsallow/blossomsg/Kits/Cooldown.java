package me.ammelsallow.blossomsg.Kits;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldown {

    private int cooldown;
    private Map<UUID,Long> map = new HashMap<>();
    public Cooldown(int cooldown){
        this.cooldown = cooldown;
    }
    public void use(Player p){
        map.put(p.getUniqueId(),System.currentTimeMillis());
    }
    public boolean ready(Player p){
        if(!map.containsKey(p.getUniqueId())){
            return true;
        }
        long timeElapsed = System.currentTimeMillis() - map.get(p.getUniqueId());
        return  timeElapsed >= cooldown;
    }
    public double left(Player p){
        long timeElapsed = System.currentTimeMillis() - map.get(p.getUniqueId());
        return  Math.floor(((cooldown - timeElapsed) * 10) / 1000.0) / 10;
    }


}
