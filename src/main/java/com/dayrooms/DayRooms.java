package com.dayrooms.model;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Room {

    private final String name;
    private UUID owner;
    private final Set<UUID> invitados = new HashSet<>();

    private boolean keepInventory = false;
    private boolean utilidadesHabilitadas = true;
    private boolean comandosHabilitados = true;

    private Location esquina1;
    private Location esquina2;
    private boolean esquinasDefinidas = false;

    private Location barreraEsquina1;
    private Location barreraEsquina2;
    private boolean barreraDefinida = false;

    private Location teleportLocation;
    private boolean teleportZoneDefinida = false;

    private final Map<String, EffectData> efectos = new LinkedHashMap<>();

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

    public boolean isComandosHabilitados() {
        return comandosHabilitados;
    }

    public void setComandosHabilitados(boolean comandosHabilitados) {
        this.comandosHabilitados = comandosHabilitados;
    }

    public Location getEsquina1() {
        return esquina1;
    }

    public void setEsquina1(Location esquina1) {
        this.esquina1 = esquina1;
    }

    public Location getEsquina2() {
        return esquina2;
    }

    public void setEsquina2(Location esquina2) {
        this.esquina2 = esquina2;
    }

    public boolean isEsquinasDefinidas() {
        return esquinasDefinidas;
    }

    public void setEsquinasDefinidas(boolean esquinasDefinidas) {
        this.esquinasDefinidas = esquinasDefinidas;
    }

    public Location getBarreraEsquina1() {
        return barreraEsquina1;
    }

    public void setBarreraEsquina1(Location barreraEsquina1) {
        this.barreraEsquina1 = barreraEsquina1;
    }

    public Location getBarreraEsquina2() {
        return barreraEsquina2;
    }

    public void setBarreraEsquina2(Location barreraEsquina2) {
        this.barreraEsquina2 = barreraEsquina2;
    }

    public boolean isBarreraDefinida() {
        return barreraDefinida;
    }

    public void setBarreraDefinida(boolean barreraDefinida) {
        this.barreraDefinida = barreraDefinida;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
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

    public Map<String, EffectData> getEfectos() {
        return efectos;
    }

    public EffectData obtenerOCrearEfecto(String key) {
        return efectos.computeIfAbsent(key, k -> new EffectData(0, 0));
    }
    }
