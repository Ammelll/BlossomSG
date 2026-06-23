package me.ammelsallow.blossomsg.WorldLoading.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class EauDeSourceMap extends SGMap {
    public EauDeSourceMap(){
        center = new Location(Bukkit.getWorld("eaudesource"),0.5,67,0.5);
        capacity = 4;
        name = "eaudesource";
    }
}
