package org.example.crud;

public class Bicicleta extends Vehiculo {

    public Bicicleta(int id, int personaId, String tipo, String marca, String modelo, int año) {
        super(id, personaId, tipo, marca, modelo, año);
    }

    public void moverse() {
        System.out.println("Moverse sobre 2 ruedas");
    }
}