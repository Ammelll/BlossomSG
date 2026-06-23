package me.ammelsallow.blossomsg.WorldLoading.Maps;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.WorldLoading.WorldLoader;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SGMap {

    private static final SGMap[] mapPool = {new GarrigalMap(), new EauDeSourceMap(), new LibertasMap(),new ShireMap()};

    public Location center;
    public int capacity;
    public String name;


    public static SGMap randomFrommPool(BlossomSG plugin){
        List<SGMap> maps = plugin.getGames().stream().map(Game::getMap).collect(Collectors.toList());
        List<SGMap> tempPool = Arrays.stream(mapPool).collect(Collectors.toList());
        System.out.println(tempPool);
        tempPool.removeAll(maps);
        tempPool = tempPool.stream().filter(m -> !WorldLoader.isRebuilding(m.getName())).collect(Collectors.toList());
        System.out.println(tempPool);
        if(tempPool.isEmpty()){
            return null;
        }
        int index = (int) (Math.random() * tempPool.size());
        return tempPool.get(index);
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
