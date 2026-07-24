package com.dayrooms;

import com.dayrooms.commands.DayRoomsCommand;
import com.dayrooms.gui.MenuListener;
import com.dayrooms.listeners.EffectsListener;
import com.dayrooms.listeners.PvPListener;
import com.dayrooms.listeners.SelectionListener;
import com.dayrooms.managers.BarrierManager;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DayRooms extends JavaPlugin {

    private RoomManager roomManager;
    private SelectionManager selectionManager;
    private BarrierManager barrierManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // crea config.yml la primera vez que arranca

        this.roomManager = new RoomManager();
        this.selectionManager = new SelectionManager();
        this.messageManager = new MessageManager(this);
        this.barrierManager = new BarrierManager(this, messageManager);

        getCommand("dayrooms").setExecutor(new DayRoomsCommand(roomManager, selectionManager, barrierManager));
        getServer().getPluginManager().registerEvents(new MenuListener(roomManager, selectionManager), this);
        getServer().getPluginManager().registerEvents(new SelectionListener(selectionManager, roomManager), this);
        getServer().getPluginManager().registerEvents(new EffectsListener(roomManager, selectionManager), this);
        getServer().getPluginManager().registerEvents(new PvPListener(roomManager, barrierManager, messageManager), this);

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

    public BarrierManager getBarrierManager() {
        return barrierManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
