package me.ammelsallow.blossomsg.Maps;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;

public class SGMap {

    private static ArrayList<SGMap> mapPool = new ArrayList<>(Arrays.asList(new ShireMap(), new SG4Map()));

    public Location center;
    public int capacity;


    public static SGMap randomFrommPool(){
        int index = (int) (Math.random() * mapPool.size());
        return mapPool.get(index);
    }

    public Location getCenter(){
        return center;
    }
    public int getCapacity(){
        return capacity;
    }
}
