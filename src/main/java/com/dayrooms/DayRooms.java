package com.dayrooms;

import com.dayrooms.commands.DayRoomsCommand;
import com.dayrooms.gui.MenuListener;
import com.dayrooms.managers.RoomManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DayRooms extends JavaPlugin {

    private RoomManager roomManager;

    @Override
    public void onEnable() {
        this.roomManager = new RoomManager();

        getCommand("dayrooms").setExecutor(new DayRoomsCommand(roomManager));
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        getLogger().info("DayRooms habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DayRooms deshabilitado.");
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }
          }
