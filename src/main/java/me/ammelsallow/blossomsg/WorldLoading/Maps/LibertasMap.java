package me.ammelsallow.blossomsg.WorldLoading.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LibertasMap extends SGMap {
    public LibertasMap(){
        center = new Location(Bukkit.getWorld("libertas"),-0.5, 44, 0.5);
        capacity = 4;
        name = "libertas";
    }
}
