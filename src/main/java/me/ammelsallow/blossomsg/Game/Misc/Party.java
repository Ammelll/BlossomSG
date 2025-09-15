package me.ammelsallow.blossomsg.Game.Misc;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.GameHelpers.GameQueueHandler;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Party {
    private Player leader;
    private List<Player> members = new ArrayList<>();
    private BlossomSG plugin;
    public Party(BlossomSG plugin, Player leader, List<Player> members){
        this.plugin = plugin;
        this.leader = leader;
        add(members);
    }
    public void add(List<Player> pot){
        List<Player> joined = pot.stream().distinct().collect(Collectors.toList());
        this.members.addAll(joined);
        broadcast(joined," has joined");
    }
    public void kick(List<Player> pot){
        List<Player> kicks = pot.stream().distinct().collect(Collectors.toList());
        broadcast(kicks, " has been kicked");
        this.members.removeAll(kicks);
    }
    public void leave(Player player){
        List<Player> singleton = Collections.singletonList(player);
        if(player.equals(leader)){
            Map<Player,Party> parties = plugin.getParties();
            parties.remove(leader);
            plugin.updateParties(parties);
            broadcast(singleton, " has disbanded");
        } else{
            broadcast(singleton," has left");
            members.remove(player);
        }
    }
    public void list(Player p){
        StringBuilder stringBuilder = new StringBuilder();
        for(Player player : members){
            stringBuilder.append(player.getName()).append(" ");
        }
        p.sendMessage(stringBuilder.toString());
    }
    public void join(GameQueueHandler gameQueueHandler){
        gameQueueHandler.join(new PlayerTeam(this));
    }
    public void broadcast(List<Player> players, String msg){
        for(Player member : members){
            for(Player player : players){
                member.sendMessage(player.getName() + msg);
            }
        }
    }
    public Player getLeader(){
        return leader;
    }
    public List<Player> getMembers(){
        return members;
    }
    public boolean contains(Player player){
        return members.contains(player);
    }
}
