package me.ammelsallow.blossomsg.Game.Misc;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerTeam {
    List<Player> members = new ArrayList<>();
    List<Player> alive = new ArrayList<>();
    UUID uuid;
    public PlayerTeam(List<Player> players){
        members.addAll(players);
        alive.addAll(players);
        this.uuid = UUID.randomUUID();
    }
    public PlayerTeam(Player player){
        this(Collections.singletonList(player));
    }
    public PlayerTeam(Party party){
       this(party.getMembers());
    }

    public List<Player> getMembers(){
        return members;
    }
    public List<Player> getAlive(){
        return alive;
    }

    public UUID getUniqueId(){
        return  uuid;
    }
    public boolean contains(Player p){
        return members.contains(p);
    }
    public void kill(Player killed){
        alive.remove(killed);
    }
}
