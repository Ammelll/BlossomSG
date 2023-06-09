package me.ammelsallow.blossomsg.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SG4Map extends SGMap{
    public SG4Map(){
        center = new Location(Bukkit.getWorld("sg4"),0,70,0);
        capacity = 6;
    }
}
