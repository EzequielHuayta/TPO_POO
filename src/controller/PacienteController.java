package controller;

import model.paciente.Paciente;
import model.paciente.PacienteModel;

public class PacienteController {
    private static PacienteController instance;
    private PacienteModel model;

    private PacienteController() {
        model = new PacienteModel();
    }

    public static PacienteController getInstance() {
        if (instance == null) {
            instance = new PacienteController();
        }
        return instance;
    }

    public void addPaciente(Paciente paciente) {
        model.addPaciente(paciente);
    }

    // Métodos para modificar, borrar y listar pacientes
}

