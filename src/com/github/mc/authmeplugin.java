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
    private static String Url;
    private static String name;
    private static String password;
    private static String Table;
    private static String database;
    private static String CheckName = "SELECT * FROM table WHERE name=";
    private static String cloumnname;
    private static String Msg;
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
        cloumnname = getConfig().getString("mysql.username");
        database = getConfig().getString("mysql.database");
        Msg = getConfig().getString("Msg");
        String sql = CheckName.replace("table",Table).replace("name",cloumnname)+"'gege'";
        System.out.println(sql);
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        super.onEnable();
    }




    @EventHandler
    public void PlayerJoingGame(PlayerJoinEvent event){
        long dateafter = new Date().getTime()/1000;
        long datelater = -1000;
        try{
            Statement stmt = getConnection().createStatement();
            String sql = CheckName.replace("table",Table).replaceAll("name",cloumnname)+"'"+event.getPlayer().getName()+"'";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                datelater = rs.getInt(Time);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println(datelater);
        if (datelater==-1000){
            event.getPlayer().kickPlayer(Msg);
            return;
        }
        long lasttime = dateafter - datelater;
        System.out.println(lasttime);
        if (lasttime>=30){
            event.getPlayer().kickPlayer(Msg);
        }

    }


    private Connection getConnection(){
        Connection conn =null;
        try
        {
            // 1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            // 2.获得数据库的连接
            System.out.println(Url+"{database}?user={Username}&password={password}&useUnicode=true&characterEncoding=utf-8".replace("{database}",database).replace("{Username}",name).replace("{password}",password));
            conn = DriverManager.getConnection(Url+"{database}?user={Username}&password={password}&useUnicode=true&characterEncoding=utf-8".replace("{database}",database).replace("{Username}",name).replace("{password}",password));
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
