package org.example.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/baseDeDatosCRUD?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "Hg.11";

    public static void insertarPersona(String nombre, String direccion, List<String> telefonos) {
        Connection conexion = ConexionDB.conectar();
        if (conexion == null) return;

        try {
            String sql = "INSERT INTO personas (nombre, direccion) VALUES (?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.executeUpdate();

            // Obtener el ID de la persona insertada
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int personaId = generatedKeys.getInt(1);
                for (String telefono : telefonos) {
                    insertarTelefono(personaId, telefono, conexion);
                }
            }

            stmt.close();
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertarTelefono(int personaId, String numero, Connection conexion) throws SQLException {
        String sql = "INSERT INTO telefonos (persona_id, numero) VALUES (?, ?)";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setInt(1, personaId);
        stmt.setString(2, numero);
        stmt.executeUpdate();
        stmt.close();
    }

    public static List<Persona> obtenerPersonas() {
        List<Persona> lista = new ArrayList<>();
        Connection conexion = ConexionDB.conectar();
        if (conexion == null) return lista;

        try {
            String sql = "SELECT p.id, p.nombre, p.direccion, t.numero FROM personas p LEFT JOIN telefonos t ON p.id = t.persona_id";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("numero");

                boolean personaExistente = false;
                for (Persona persona : lista) {
                    if (persona.getId() == id) {
                        persona.getTelefonos().add(telefono);
                        personaExistente = true;
                        break;
                    }
                }
                if (!personaExistente) {
                    List<String> telefonos = new ArrayList<>();
                    if (telefono != null) telefonos.add(telefono);
                    lista.add(new Persona(id, nombre, direccion, telefonos));
                }
            }

            rs.close();
            stmt.close();
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
        public static void actualizarPersona(Persona persona) {
            try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA)) {
                // Actualizar la tabla personas
                String sqlPersona = "UPDATE personas SET nombre = ?, direccion = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlPersona)) {
                    stmt.setString(1, persona.getNombre());
                    stmt.setString(2, persona.getDireccion());
                    stmt.setInt(3, persona.getId());
                    stmt.executeUpdate();
                }

                // Eliminar teléfonos actuales y agregar los nuevos
                String sqlBorrarTelefonos = "DELETE FROM telefonos WHERE persona_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlBorrarTelefonos)) {
                    stmt.setInt(1, persona.getId());
                    stmt.executeUpdate();
                }

                String sqlInsertarTelefono = "INSERT INTO telefonos (persona_id, numero) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlInsertarTelefono)) {
                    for (String telefono : persona.getTelefonos()) {
                        stmt.setInt(1, persona.getId());
                        stmt.setString(2, telefono);
                        stmt.executeUpdate();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    public static void eliminarPersona(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA)) {
            String sql = "DELETE FROM personas WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}