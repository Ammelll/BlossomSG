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
import java.util.HashSet;
import java.util.Set;

public class WorldLoader {

    private static BlossomSG plugin;

    public static void setPlugin(BlossomSG plugin) {
        WorldLoader.plugin = plugin;
    }

    private static final Set<String> rebuildingWorlds = new HashSet<>();

    public static boolean isRebuilding(String world) {
        return rebuildingWorlds.contains(world);
    }

    public static void rebuild(World world) {
        String worldName = world.getName();

        if (rebuildingWorlds.contains(worldName)) return;
        rebuildingWorlds.add(worldName);

        unload(world);

        new BukkitRunnable() {
            @Override
            public void run() {
                delete(world);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        World loaded = load(worldName);

                        if (loaded != null) {
                            loaded.getSpawnLocation();
                        }

                        rebuildingWorlds.remove(worldName);

                    }
                }.runTask(plugin);

            }
        }.runTaskLater(plugin, 20L); // give server time to settle
    }

    public static World load(String worldName) {
        System.out.println("LOADING WORLDS");
        File rootDir = new File("").getAbsoluteFile();
        File unloadedWorldsDir = new File(rootDir, "WorldFiles");
        File from = new File(unloadedWorldsDir, worldName);
        File to = new File(rootDir, worldName);
        try {
            FileUtils.copyDirectory(from, to);

            return Bukkit.createWorld(new WorldCreator(worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        deletePath(world.getWorldFolder());
    }

    public static void unload(World world) {
//        String Name = world.getName();
        if (world != null) {
            for (Player p : world.getPlayers()) {
                p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
            Bukkit.getServer().unloadWorld(world, false);
        }
    }
}