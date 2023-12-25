package me.ammelsallow.blossomsg.WorldLoading;

import me.ammelsallow.blossomsg.BlossomSG;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class WorldLoader {

    private static BlossomSG plugin;

    public WorldLoader(BlossomSG plugin){
        this.plugin = plugin;
    }
    public static void rebuild(World world) {
        String worldName = world.getName();
        delete(world);
        new BukkitRunnable() {

            @Override
            public void run() {
                load(worldName);
            }
        }.runTaskLater(plugin, 60L);
    }

    public static void load(String worldName) {
        System.out.println("LOADING WORLDS");
        File rootDir = new File("").getAbsoluteFile();
        File unloadedWorldsDir = new File(rootDir, "WorldFiles");
        File from = new File(unloadedWorldsDir, worldName);
        File to = new File(rootDir, worldName);
        try {
            to.mkdir();

            FileUtils.copyDirectory(from, to);

            Bukkit.createWorld(new WorldCreator(worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deletePath(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deletePath(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static void delete(World world) {
        File path = world.getWorldFolder();
        // Bukkit.getWorlds().remove(world);
        unload(world);
        deletePath(path);
    }

    public static void unload(World world) {
//        String Name = world.getName();
        if (!world.equals(null)) {
            for (Player p : world.getPlayers()) {
                p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
            Bukkit.getServer().unloadWorld(world, true);
        }
    }
}