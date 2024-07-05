package model.peticion;

import model.paciente.PacienteDTO;
import model.practica.PracticaDTO;
import model.resultado.ResultadoDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PeticionDTO {

    private int id;
    private final String obraSocial;
    private Date fechaCarga;
    private Date fechaCalculadaEntrega;
    private final List<PracticaDTO> listPracticas;
    private final List<ResultadoDTO> listResultados;
    private final PacienteDTO paciente;

    public PeticionDTO(int id, String obraSocial, Date fechaCarga, List<PracticaDTO> listPracticas, List<ResultadoDTO> listResultados, PacienteDTO paciente) {
        this.id = id;
        this.obraSocial = obraSocial;
        this.fechaCarga = fechaCarga;
        this.listPracticas = listPracticas;
        this.listResultados = listResultados;
        this.paciente = paciente;
    }

    public PeticionDTO(int id, String obraSocial, List<PracticaDTO> listPracticas, List<ResultadoDTO> listResultados, PacienteDTO paciente) {
        this.id = id;
        this.obraSocial = obraSocial;
        this.listPracticas = listPracticas;
        this.listResultados = listResultados;
        this.paciente = paciente;
    }

    public PeticionDTO(String obraSocial, List<PracticaDTO> listPracticas, PacienteDTO paciente) {
        this.obraSocial = obraSocial;
        this.listPracticas = listPracticas;
        this.listResultados = new ArrayList<>();
        this.paciente = paciente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date nuevaFechaCarga) {
        fechaCarga = nuevaFechaCarga;
    }

    public Date getFechaCalculadaEntrega() {
        return fechaCalculadaEntrega;
    }

    public void setFechaCalculadaEntrega(Date nuevaFechaCalculadaEntrega) {
        fechaCalculadaEntrega = nuevaFechaCalculadaEntrega;
    }

    public List<PracticaDTO> getListPracticas() {
        return listPracticas;
    }

    public List<ResultadoDTO> getListResultados() {
        return listResultados;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }
}