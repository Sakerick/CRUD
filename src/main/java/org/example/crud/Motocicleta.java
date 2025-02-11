package org.example.crud;

public class Motocicleta extends Vehiculo {
    private int nDeCilindros;

    public Motocicleta(int id, int personaId, String tipo, String marca, String modelo, int año, int nDeCilindros) {
        super(id, personaId, tipo, marca, modelo, año);
        this.nDeCilindros = nDeCilindros;
    }

    public int getnDeCilindros() {
        return nDeCilindros;
    }

    public void setnDeCilindros(int nDeCilindros) {
        this.nDeCilindros = nDeCilindros;
    }

    public void moverse() {
        System.out.println("Moverse sobre 2 ruedas");
    }
}
