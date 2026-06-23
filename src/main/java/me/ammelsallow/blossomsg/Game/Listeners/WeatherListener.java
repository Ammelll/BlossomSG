package me.ammelsallow.blossomsg.Game.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        if(event.toWeatherState()) event.setCancelled(true);

    }
    @EventHandler
    public void onThunderChange(ThunderChangeEvent event){
        if(event.toThunderState()) event.setCancelled(true);
    }
}
