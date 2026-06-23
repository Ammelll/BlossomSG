package me.ammelsallow.blossomsg.WorldLoading.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GarrigalMap extends SGMap {
    public GarrigalMap(){
        center = new Location(Bukkit.getWorld("garrigal"),55.5,38,27.5);
        capacity = 4;
        name = "garrigal";
    }
}
