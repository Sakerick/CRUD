package org.example.crud;

public class Automovil extends Vehiculo {
    private int numPuertas;

    public Automovil(int id, int personaId, String tipo, String marca,
                     String modelo, int año, int numPuertas) {
        super(id, personaId, tipo, marca, modelo, año);
        this.numPuertas = numPuertas;
    }

    public int getNumDePuertas() {
        return numPuertas;
    }

    public void setnDePuertas(int nDePuertas) {
        this.numPuertas = nDePuertas;
    }

    public void moverse(){
        System.out.println("Moverse sobre 4 ruedas");
    }

}
