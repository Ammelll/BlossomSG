package me.ammelsallow.blossomsg.DB;

import me.ammelsallow.blossomsg.DB.Model.PlayerStats;

import java.sql.*;

public class Database {
    private Connection connection;
    public Connection getConnection() throws SQLException {
        if(connection != null){
            return connection;
        }
        String url = "jdbc:mysql://localhost/stat_tracker?characterEncoding=latin1";
        String user = "root";
        String password = "";

        this.connection = DriverManager.getConnection(url,user,password);

        System.out.println("Connected to the stat tracker database");
        return this.connection;
    }

    public void initializeDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS player_stats(uuid varchar(36) primary key, deaths int, kills int, gold int, wins int, last_login DATE, last_logout DATE)";
        statement.execute(sql);

        statement.close();
        System.out.println("created tabless");
    }
    public PlayerStats findPlayerStatsByUUID(String uuid) throws SQLException {

        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1,uuid);
        ResultSet results = statement.executeQuery();

        if(results.next()){

            int deaths = results.getInt("deaths");
            int kills = results.getInt("kills");
            int gold = results.getInt("gold");
            int wins = results.getInt("wins");
            Date lastLogin = results.getDate("last_login");
            Date lastLogout = results.getDate("last_logout");


            PlayerStats playerStats = new PlayerStats(uuid,deaths,kills,gold,wins,lastLogin,lastLogout);
            statement.close();
            return playerStats;
        }
        statement.close();
        return null;
    }
    public void createPlayerStats(PlayerStats playerStats) throws SQLException{

        PreparedStatement statement = getConnection()
                .prepareStatement( "INSERT INTO player_stats(uuid,deaths,kills,gold, wins, last_login,last_logout) VALUES (? ,?,?,?,?,?,?)");
        statement.setString(1, playerStats.getUuid());
        statement.setInt(2,playerStats.getDeaths());
        statement.setInt(3,playerStats.getKills());
        statement.setInt(4,playerStats.getGold());
        statement.setInt(5,playerStats.getWins());
        statement.setDate(6,new Date(playerStats.getLast_login().getTime()));
        statement.setDate(7, new Date(playerStats.getLast_logout().getTime()));

        statement.executeUpdate();
        statement.close();
    }
    public void updatePlayerStats(PlayerStats playerStats) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement( "UPDATE player_stats SET deaths = ?,kills = ?, gold = ?, wins = ?, last_login = ?, last_logout = ? WHERE uuid = ?");
        statement.setInt(1,playerStats.getDeaths());
        statement.setInt(2,playerStats.getKills());
        statement.setInt(3,playerStats.getGold());
        statement.setInt(4,playerStats.getWins());
        statement.setDate(5,new Date(playerStats.getLast_login().getTime()));
        statement.setDate(6, new Date(playerStats.getLast_logout().getTime()));
        statement.setString(7, playerStats.getUuid());

        statement.executeUpdate();
        statement.close();
    }
    public void deletePlayerStats(String uuid) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("DELETE FROM player_stats WHERE uuid = ?");
        statement.setString(1,uuid);

        statement.executeUpdate();
        statement.close();
    }
    public void closeConnection(){
        try{
            if(this.connection != null){
                this.connection.close();
            }
        } catch (SQLException e){

        }
    }
}package me.ammelsallow.blossomsg.DB;

import me.ammelsallow.blossomsg.Model.PlayerStats;

import java.sql.*;
import java.util.UUID;

public class Database {
    private Connection connection;
    public Connection getConnection() throws SQLException {
        if(connection != null){
            return connection;
        }
        String url = "jdbc:mysql://localhost/stat_tracker?characterEncoding=latin1";
        String user = "root";
        String password = "";

        this.connection = DriverManager.getConnection(url,user,password);

        System.out.println("Connected to the stat tracker database");
        return this.connection;
    }

    public void initializeDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS player_stats(uuid varchar(36) primary key, deaths int, kills int, gold int, wins int, last_login DATE, last_logout DATE)";
        statement.execute(sql);

        statement.close();
        System.out.println("created tabless");
    }
    public PlayerStats findPlayerStatsByUUID(String uuid) throws SQLException {

        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1,uuid);
        ResultSet results = statement.executeQuery();

        if(results.next()){

            int deaths = results.getInt("deaths");
            int kills = results.getInt("kills");
            int gold = results.getInt("gold");
            int wins = results.getInt("wins");
            Date lastLogin = results.getDate("last_login");
            Date lastLogout = results.getDate("last_logout");


            PlayerStats playerStats = new PlayerStats(uuid,deaths,kills,gold,wins,lastLogin,lastLogout);
            statement.close();
            return playerStats;
        }
        statement.close();
        return null;
    }
    public void createPlayerStats(PlayerStats playerStats) throws SQLException{

        PreparedStatement statement = getConnection()
                .prepareStatement( "INSERT INTO player_stats(uuid,deaths,kills,gold, wins, last_login,last_logout) VALUES (? ,?,?,?,?,?,?)");
        statement.setString(1, playerStats.getUuid());
        statement.setInt(2,playerStats.getDeaths());
        statement.setInt(3,playerStats.getKills());
        statement.setInt(4,playerStats.getGold());
        statement.setInt(5,playerStats.getWins());
        statement.setDate(6,new Date(playerStats.getLast_login().getTime()));
        statement.setDate(7, new Date(playerStats.getLast_logout().getTime()));

        statement.executeUpdate();
        statement.close();
    }
    public void updatePlayerStats(PlayerStats playerStats) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement( "UPDATE player_stats SET deaths = ?,kills = ?, gold = ?, wins = ?, last_login = ?, last_logout = ? WHERE uuid = ?");
        statement.setInt(1,playerStats.getDeaths());
        statement.setInt(2,playerStats.getKills());
        statement.setInt(3,playerStats.getGold());
        statement.setInt(4,playerStats.getWins());
        statement.setDate(5,new Date(playerStats.getLast_login().getTime()));
        statement.setDate(6, new Date(playerStats.getLast_logout().getTime()));
        statement.setString(7, playerStats.getUuid());

        statement.executeUpdate();
        statement.close();
    }
    public void deletePlayerStats(String uuid) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("DELETE FROM player_stats WHERE uuid = ?");
        statement.setString(1,uuid);

        statement.executeUpdate();
        statement.close();
    }
    public void closeConnection(){
        try{
            if(this.connection != null){
                this.connection.close();
            }
        } catch (SQLException e){

        }
    }
}
