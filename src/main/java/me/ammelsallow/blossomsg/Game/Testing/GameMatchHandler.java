package me.ammelsallow.blossomsg.Game.Testing;

import me.ammelsallow.blossomsg.BlossomSG;
import me.ammelsallow.blossomsg.Game.Game;
import me.ammelsallow.blossomsg.Game.Tasks.CapturePointUpdate;
import me.ammelsallow.blossomsg.Kits.Captain.Tasks.CheckVehicleTask;
import me.ammelsallow.blossomsg.Kits.Kit;
import me.ammelsallow.blossomsg.Kits.Misc.PlayerKitSelection;
import me.ammelsallow.blossomsg.Kits.Robinhood.Misc.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class GameMatchHandler {
    private Game game;
    private CapturePointUpdate capture;
    private BukkitTask captureUpdate;
    private CheckVehicleTask vehicle;
    private BukkitTask vehicleUpdate;
    private int savePVP;
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
        vehicleUpdate = vehicle.runTaskTimer(game.getPlugin(),0,20);
        initSavePVP();
    }
    private void initSavePVP(){
        savePVP = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable() {
            int countDown = 32;
            @Override
            public void run() {
                if(countDown > 1){
                    countDown--;
                    for(Player p : game.getPlayers()){
                        GameScoreboardHandler.setScoreboard(p,countDown,"PVP Enabled ",true);
                    }
                } else{
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(Game.sgPrefix + ChatColor.DARK_RED + "" + ChatColor.BOLD +"The grace period is over!");
                        GameScoreboardHandler.setScoreboard(p,countDown,"PVP Enabled ",false);
                    }
                    game.getRandomEvent().start();
                    Bukkit.getScheduler().cancelTask(savePVP);
                    countDown = 32;
                }
            }
        },6L,20L);
    }
    private void prepPlayers(){
        for(Player p : game.getPlayers()){
            prepPotionEffects(p);
            prepInventory(p);
            prepAttributes(p);
            capture.setScoreboard(p);
            prepKit(p);
        }
    }
    private void prepPotionEffects(Player p){
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,630,4));
        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,630,4));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,630,4));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1000000,0));
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
        Inventory inventory = p.getInventory();
        for(int i = 0; i < BlossomSG.KITS.length; i++){
            Kit kit = BlossomSG.KITS[i];
            if(stringID.equals(kit.stringID)) {
                for (int j = 0; j < kit.items.length; j++) {
                    inventory.setItem(j, kit.items[j]);
                }
            }
        }
    }
    public void startCapturePoint(){
        captureUpdate = capture.runTaskTimer(game.getPlugin(), 1L, 20L);
    }
    public void cancelVehicle(){
        vehicle.cancel();
    }
    public void cancelCapture(){
        capture.cancel();
    }
}
