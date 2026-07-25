package com.dayrooms.model;

public class EffectData {

    private int nivel;
    private long duracionSegundos;

    public EffectData(int nivel, long duracionSegundos) {
        this.nivel = nivel;
        this.duracionSegundos = duracionSegundos;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public long getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(long duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public boolean estaActivo() {
        return nivel > 0;
    }
}
