package me.ammelsallow.blossomsg.WorldLoading.Maps;

import org.bukkit.Location;
import org.bukkit.World;

public class SGMap {

    private static final SGMap[] mapPool = {new ShireMap(), new SG4Map(), new WildWestMap()};

    public Location center;
    public int capacity;
    public String name;


    public static SGMap randomFrommPool(){
        int index = (int) (Math.random() * mapPool.length);
        return mapPool[index];
    }

    public Location getCenter(){
        return center;
    }
    public World getWorld(){
        return getCenter().getWorld();
    }
    public int getCapacity(){
        return capacity;
    }

    public static SGMap[] getMapPool(){
        return mapPool;
    }
    public String getName(){
        return name;
    }
}
