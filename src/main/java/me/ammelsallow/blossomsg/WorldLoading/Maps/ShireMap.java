package me.ammelsallow.blossomsg.WorldLoading.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ShireMap extends SGMap{
    public ShireMap(){
        center = new Location(Bukkit.getWorld("shire"),-10.5,37,0.5);
        capacity = 4;
        name = "shire";
    }
}
