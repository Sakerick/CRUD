package org.example.crud;

public class VehiculoMaritimo extends Vehiculo {
    private String tipo;


    public VehiculoMaritimo(int id, int personaId, String tipo, String marca, String modelo, int año) {
        super(id, personaId, tipo, marca, modelo, año);
    }
}
