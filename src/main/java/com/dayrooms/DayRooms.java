package com.dayrooms;

import com.dayrooms.commands.DayRoomsCommand;
import com.dayrooms.gui.MenuListener;
import com.dayrooms.listeners.SelectionListener;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DayRooms extends JavaPlugin {

    private RoomManager roomManager;
    private SelectionManager selectionManager;

    @Override
    public void onEnable() {
        this.roomManager = new RoomManager();
        this.selectionManager = new SelectionManager();

        getCommand("dayrooms").setExecutor(new DayRoomsCommand(roomManager, selectionManager));
        getServer().getPluginManager().registerEvents(new MenuListener(roomManager, selectionManager), this);
        getServer().getPluginManager().registerEvents(new SelectionListener(selectionManager, roomManager), this);

        getLogger().info("DayRooms habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DayRooms deshabilitado.");
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }
}
