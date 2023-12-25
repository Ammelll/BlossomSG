package me.ammelsallow.blossomsg.DB.Model;

import java.util.Date;

public class PlayerStats {


    private String uuid;
    private int deaths;
    private int kills;
    private int gold;
    private int wins;
    private Date last_login;
    private Date last_logout;

    public PlayerStats(String uuid, int deaths, int kills, int gold, int wins, Date last_login, Date last_logout) {
        this.uuid = uuid;
        this.deaths = deaths;
        this.kills = kills;
        this.gold = gold;
        this.wins = wins;
        this.last_login = last_login;
        this.last_logout = last_logout;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public Date getLast_login() {
        return last_login;
    }

    public void setLast_login(Date last_login) {
        this.last_login = last_login;
    }

    public Date getLast_logout() {
        return last_logout;
    }

    public void setLast_logout(Date last_logout) {
        this.last_logout = last_logout;
    }

}