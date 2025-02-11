package org.example.crud;

public class Vehiculo {
    private int id;
    private int personaId;  // Relación con la persona
    private String tipo;
    private String marca;
    private String modelo;
    private int año;

    public Vehiculo(int id, int personaId, String tipo, String marca, String modelo, int año) {
        this.id = id;
        this.personaId = personaId;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
    }

    // Getters y Setters
    public int getId() { return id; }
    public int getPersonaId() { return personaId; }
    public String getTipo() { return tipo; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAño() { return año; }

    public void setPersonaId(int personaId) { this.personaId = personaId; }
}


