package me.ammelsallow.blossomsg.Game.GameHelpers;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Tasks.CapturePointUpdate;
import me.ammelsallow.blossomsg.Kits.Captain.Tasks.CheckVehicleTask;
import me.ammelsallow.blossomsg.Kits.Kit;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.Kits.Shepherd.Tasks.ShepherdProximityTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.stream.Collectors;

public class GameMatchHandler {
    private Game game;
    private CapturePointUpdate capture;
    private BukkitTask captureUpdate;
    private CheckVehicleTask vehicle;
    private BukkitTask vehicleUpdate;
    private ShepherdProximityTask sheep;
    private BukkitTask sheepUpdate;
    private int savePVP;
    private int speedID;
    public GameMatchHandler(Game game){
        this.game = game;
    }
    public void startGame(){
        initTasks();
        prepPlayers();
    }
    private void initTasks(){
        capture = new CapturePointUpdate(game);
        vehicle = new CheckVehicleTask(game);
        sheep = new ShepherdProximityTask(game);
        vehicleUpdate = vehicle.runTaskTimer(game.getPlugin(),0,20);
        sheepUpdate = sheep.runTaskTimer(game.getPlugin(),0,80);
        initSavePVP();
    }
    private void initSavePVP(){
        savePVP = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable() {
            int countDown = 30;
            @Override
            public void run() {

                if(countDown > 0){
                    for(Player p : game.getPlayers()){
                        GameScoreboardHandler.setScoreboard(p,countDown,"PVP Enabled ",true,6);
                    }
                    countDown--;
                } else{
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(Game.sgPrefix + ChatColor.DARK_RED + "" + ChatColor.BOLD +"The grace period is over!");
                        GameScoreboardHandler.setScoreboard(p,countDown,"PVP Enabled ",false,6);
                    }
                    game.getRandomEvent().start();
                    Bukkit.getScheduler().cancelTask(savePVP);
                    countDown = 30;
                }
            }
        },6L,20L);
    }
    private void prepPlayers(){
        speedRunnable();
        for(Player p : game.getPlayers()){
            prepPotionEffects(p);
            prepInventory(p);
            prepAttributes(p);
            capture.setScoreboard(p);
            prepKit(p);
        }
    }
    private void speedRunnable(){
        speedID = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(),new Runnable(){
            @Override
            public void run(){
                for(Player p : game.getPlayers()) {
                    if (p.getActivePotionEffects().stream().noneMatch(pe -> pe.getType() == PotionEffectType.SPEED)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 0));
                    }
                }
            }
        },0,20L);
    }

    private void prepPotionEffects(Player p){
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,630,4));
        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,630,4));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,630,4));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,630,1));
    }
    private void prepInventory(Player p){
        Inventory inventory = p.getInventory();
        inventory.clear();
        p.getInventory().setArmorContents(null);
    }
    private void prepAttributes(Player p){
        p.setHealth(20.0);
        p.setTotalExperience(0);
        p.setSaturation(20);
        p.setGameMode(GameMode.SURVIVAL);
    }
    private void prepKit(Player p){
        Inventory inventory = p.getInventory();
        if(PlayerKitSelection.selectedKit.containsKey(p.getUniqueId())){
            String stringID = PlayerKitSelection.selectedKit.get(p.getUniqueId());
            selectKit(p, stringID);
        }
        else{
            p.sendMessage(Game.sgPrefix + ChatColor.RED + "No kit selecting, defaulting to robinhood");
            PlayerKitSelection.selectedKit.put(p.getUniqueId(),"robin");
            selectKit(p,"robin");
        }
    }
    private void selectKit(Player p, String stringID){
        PlayerInventory inventory = p.getInventory();
        for(int i = 0; i < BlossomSG.KITS.size(); i++){
            Kit kit = BlossomSG.KITS.get(i);
            if(stringID.equals(kit.stringID)) {
                inventory.setContents(kit.items);
                inventory.setArmorContents(kit.armor);
            }
        }
    }
    public void startCapturePoint(){
        captureUpdate = capture.runTaskTimer(game.getPlugin(), 1L, 20L);
    }
    public void cancelVehicle(){
        vehicle.cancel();
        sheep.cancel();
    }
    public void cancelCapture(){
        if(captureUpdate != null) {
            capture.cancel();
        }
    }
    public int getSpeedID() {
        return speedID;
    }

    public int getSavePVP(){
        return savePVP;
    }
}
