package org.example.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {
    public static void insertarVehiculo(int personaId, String tipo, String marca, String modelo, int año) {
        String sql = "INSERT INTO vehiculos (persona_id, tipo, marca, modelo, año) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personaId);
            stmt.setString(2, tipo);
            stmt.setString(3, marca);
            stmt.setString(4, modelo);
            stmt.setInt(5, año);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarVehiculo(int idVehiculo) {
        String sql = "DELETE FROM vehiculos WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVehiculo);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Vehiculo> obtenerVehiculosPorPersona(int personaId) {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos WHERE persona_id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehiculo v = new Vehiculo(
                        rs.getInt("id"),
                        rs.getInt("persona_id"),
                        rs.getString("tipo"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("año")
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
