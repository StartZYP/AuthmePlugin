package com.github.mc;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.Date;


public class authmeplugin extends JavaPlugin implements Listener {
    private String Url;
    private String name;
    private String password;
    private String Table;
    private String CheckName = "SELECT * FROM {table} WHERE {name}=";
    private String Time;
    @Override
    public void onEnable(){
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(),"config.yml");
        if (!(file.exists())){
            saveDefaultConfig();
        }
        Url = getConfig().getString("mysql.url");
        name = getConfig().getString("mysql.user");
        password = getConfig().getString("mysql.password");
        Table = getConfig().getString("mysql.table");
        Time = getConfig().getString("mysql.time");
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        super.onEnable();
    }




    @EventHandler
    public void PlayerJoingGame(PlayerJoinEvent event){
        long dateafter = new Date().getTime()-1000;
        long datelater = -1000;
        try{
            Statement stmt = getConnection().createStatement();

            ResultSet rs = stmt.executeQuery(CheckName.replace("{table}",Table).replace("{name}",name)+event.getPlayer().getName());
            while (rs.next()){
                datelater = rs.getLong(Time);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        if (datelater==-1000){
            event.getPlayer().kickPlayer("请你先登录登录");
            return;
        }
        long lasttime = dateafter - datelater;
        if (lasttime>=30){
            event.getPlayer().kickPlayer("请你先登录登录");
        }

    }


    private Connection getConnection(){
        Connection conn =null;
        try
        {
            // 1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            // 2.获得数据库的连接
            conn = DriverManager.getConnection(Url+Table, name, password);
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }
}
