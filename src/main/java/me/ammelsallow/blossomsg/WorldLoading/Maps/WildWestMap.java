package me.ammelsallow.blossomsg.WorldLoading.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WildWestMap extends SGMap{
    public WildWestMap(){
        center = new Location(Bukkit.getWorld("WildWest"),-542.5,70,602.5);
        capacity = 6;
        name = "WildWest";
    }
}
