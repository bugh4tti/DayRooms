package com.dayrooms.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Room {

    private final String name;
    private UUID owner;
    private final Set<UUID> invitados = new HashSet<>();

    private boolean keepInventory = false;
    private boolean utilidadesHabilitadas = true;

    private boolean esquinasDefinidas = false;
    private boolean barreraDefinida = false;
    private boolean teleportZoneDefinida = false;

    public Room(String name, UUID owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Set<UUID> getInvitados() {
        return invitados;
    }

    public boolean isKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public boolean isUtilidadesHabilitadas() {
        return utilidadesHabilitadas;
    }

    public void setUtilidadesHabilitadas(boolean utilidadesHabilitadas) {
        this.utilidadesHabilitadas = utilidadesHabilitadas;
    }

    public boolean isEsquinasDefinidas() {
        return esquinasDefinidas;
    }

    public void setEsquinasDefinidas(boolean esquinasDefinidas) {
        this.esquinasDefinidas = esquinasDefinidas;
    }

    public boolean isBarreraDefinida() {
        return barreraDefinida;
    }

    public void setBarreraDefinida(boolean barreraDefinida) {
        this.barreraDefinida = barreraDefinida;
    }

    public boolean isTeleportZoneDefinida() {
        return teleportZoneDefinida;
    }

    public void setTeleportZoneDefinida(boolean teleportZoneDefinida) {
        this.teleportZoneDefinida = teleportZoneDefinida;
    }

    public boolean isCompleta() {
        return esquinasDefinidas && barreraDefinida;
    }
    }
