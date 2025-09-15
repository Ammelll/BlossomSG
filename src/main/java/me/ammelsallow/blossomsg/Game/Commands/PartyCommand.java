package me.ammelsallow.blossomsg.Game.Commands;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Misc.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartyCommand implements CommandExecutor {
    private BlossomSG plugin;

    public PartyCommand(BlossomSG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        Map<Player, Party> parties = plugin.getParties();
        String subject = args[0];
        args = Arrays.stream(args,1,args.length).toArray(String[]::new);
        switch (subject) {
            case "invite":
                List<Player> invitedPlayers = Arrays.stream(args).map((name) -> Bukkit.getPlayer(name).getPlayer()).collect(Collectors.toCollection(ArrayList::new));
                invitedPlayers.add(p);
                if(parties.containsKey(p)){
                    Party party = parties.get(p);
                    party.add(invitedPlayers);
                    parties.put(party.getLeader(),party);
                } else{
                    parties.put(p,new Party(plugin,p,invitedPlayers));
                }
                break;
            case "kick":
                if (parties.containsKey(p)) {
                    List<Player> kickedPlayers = Arrays.stream(args).map((name) -> Bukkit.getPlayer(name).getPlayer()).collect(Collectors.toCollection(ArrayList::new));
                    Party party = parties.get(p);
                    party.kick(kickedPlayers);
                    parties.put(party.getLeader(),party);
                }
                break;
            case "leave":
                for(Player leader : parties.keySet()){
                    Party party = parties.get(leader);
                    if(party.contains(p)){
                        party.leave(p);
                    }
                }
                break;
            case "list":
                for(Player leader : parties.keySet()){
                    Party party = parties.get(leader);
                    if(party.contains(p)){
                        party.list(p);
                    }
                }
                break;
        }
        plugin.updateParties(parties);
        return true;
    }
    private void broadcastJoinToParty(List<Player> playerList){
        playerList.forEach((player -> player.sendMessage(ChatColor.BLUE + player.getName() + " has joined the party")));
    }
    private void broadcastDisband(List<Player> playerList, Player leader){
        playerList.forEach((player -> player.sendMessage(ChatColor.BLUE + leader.getName() + " has disbanded the party")));
    }
    private void broadcastKick(List<Player> playerList, Player kicked){
        playerList.forEach((player -> player.sendMessage(ChatColor.BLUE + kicked.getName() + " has been kicked from the party")));
    }
    private void broadcastLeave(List<Player> playerList,Player left){
        playerList.forEach((player -> player.sendMessage(ChatColor.BLUE + left.getName() + " has left the party")));
    }
}

