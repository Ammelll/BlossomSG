package me.ammelsallow.blossomsg.Game.Testing;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public abstract class GameScoreboardHandler {


    public static final void setScoreboard(Player p, int countdown, String title, boolean keep, boolean reset) {
        Scoreboard scoreboard = p.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Score score;
        String timeConverted = convertTimeFormat(countdown);
        score = objective.getScore(ChatColor.AQUA + title + ChatColor.RESET + timeConverted);
        if (reset) {
            scoreboard.resetScores(ChatColor.AQUA + title + ChatColor.RESET + convertTimeFormat(countdown + 1));
        }
        if (keep) {
            score.setScore(6);
        }
    }

    private static final String convertTimeFormat(int totalSeconds) {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds % 3600) / 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}