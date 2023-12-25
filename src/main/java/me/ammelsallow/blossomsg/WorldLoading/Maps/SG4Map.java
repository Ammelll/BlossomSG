package me.ammelsallow.blossomsg.WorldLoading.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SG4Map extends SGMap{
    public SG4Map(){
        center = new Location(Bukkit.getWorld("sg4"),0.5,70,-2.5);
        capacity = 6;
        name = "sg4";
    }
}
