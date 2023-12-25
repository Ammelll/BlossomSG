package me.ammelsallow.blossomsg.WorldLoading.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ShireMap extends SGMap{
    public ShireMap(){
        center = new Location(Bukkit.getWorld("Shire"),-10.5,70,0.5);
        capacity = 8;
        name = "Shire";
    }
}
