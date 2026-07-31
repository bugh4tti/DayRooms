package com.dayrooms;

import com.dayrooms.commands.DayRoomsCommand;
import com.dayrooms.commands.DayRoomsTabCompleter;
import com.dayrooms.gui.MenuListener;
import com.dayrooms.listeners.BarrierBreakListener;
import com.dayrooms.listeners.CommandBlockListener;
import com.dayrooms.listeners.EffectsListener;
import com.dayrooms.listeners.PvPListener;
import com.dayrooms.listeners.RoomEntryListener;
import com.dayrooms.listeners.RoomListListener;
import com.dayrooms.listeners.SelectionListener;
import com.dayrooms.listeners.UtilitiesListener;
import com.dayrooms.managers.BarrierManager;
import com.dayrooms.managers.ManualBarrierManager;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.RoomPersistenceManager;
import com.dayrooms.managers.SelectionManager;
import com.dayrooms.managers.StatsManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DayRooms extends JavaPlugin {

    private RoomManager roomManager;
    private SelectionManager selectionManager;
    private BarrierManager barrierManager;
    private MessageManager messageManager;
    private RoomPersistenceManager persistenceManager;
    private ManualBarrierManager manualBarrierManager;
    private StatsManager statsManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.roomManager = new RoomManager();
        this.selectionManager = new SelectionManager();
        this.messageManager = new MessageManager(this);
        this.barrierManager = new BarrierManager(this, messageManager);
        this.persistenceManager = new RoomPersistenceManager(this);
        this.manualBarrierManager = new ManualBarrierManager(this, barrierManager, messageManager);
        this.statsManager = new StatsManager(this);

        persistenceManager.cargarTodas(roomManager);

        getCommand("dayrooms").setExecutor(new DayRoomsCommand(roomManager, selectionManager, barrierManager, messageManager, persistenceManager, statsManager));
        getCommand("dayrooms").setTabCompleter(new DayRoomsTabCompleter(roomManager));

        getServer().getPluginManager().registerEvents(new MenuListener(roomManager, selectionManager), this);
        getServer().getPluginManager().registerEvents(new SelectionListener(selectionManager, roomManager), this);
        getServer().getPluginManager().registerEvents(new EffectsListener(roomManager, selectionManager), this);
        getServer().getPluginManager().registerEvents(new PvPListener(roomManager, barrierManager, messageManager, statsManager), this);
        getServer().getPluginManager().registerEvents(new UtilitiesListener(roomManager, messageManager), this);
        getServer().getPluginManager().registerEvents(new RoomEntryListener(roomManager, barrierManager, messageManager, manualBarrierManager), this);
        getServer().getPluginManager().registerEvents(new RoomListListener(roomManager, selectionManager), this);
        getServer().getPluginManager().registerEvents(new CommandBlockListener(roomManager, messageManager), this);
        getServer().getPluginManager().registerEvents(new BarrierBreakListener(roomManager, manualBarrierManager), this);

        getLogger().info("DayRooms habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        if (persistenceManager != null && roomManager != null) {
            persistenceManager.guardarTodas(roomManager);
        }
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

    public RoomPersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
    }
