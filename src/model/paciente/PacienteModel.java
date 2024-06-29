package model.paciente;

import persist.Database;

import java.sql.*;

public class PacienteModel {
    private Connection connection;

    public PacienteModel() {
        connection = Database.getInstance().getConnection();
    }

    public void addPaciente(Paciente paciente) {
        String sql = "INSERT INTO pacientes(nombre, apellido) VALUES(?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            pstmt.setString(1, paciente.getNombre());
//            pstmt.setString(2, paciente.getApellido());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Métodos para modificar, borrar y listar pacientes
}

