package org.example.crud;

public class CamionDeCarga extends Vehiculo {
    private int capacidadDeCarga;

    public CamionDeCarga(int id, int personaId, String tipo, String marca, String modelo, int año, int capacidadDeCarga) {
        super(id, personaId, tipo, marca, modelo, año);
        this.capacidadDeCarga = capacidadDeCarga;
    }

    public int getCapacidadDeCarga() {
        return capacidadDeCarga;
    }

    public void setCapacidadDeCarga(int capacidadDeCarga) {
        this.capacidadDeCarga = capacidadDeCarga;
    }
}