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
    private int numeroSucursal;

    public PeticionDTO(int id, String obraSocial, Date fechaCarga, List<PracticaDTO> listPracticas, List<ResultadoDTO> listResultados, PacienteDTO paciente) {
        this.id = id;
        this.obraSocial = obraSocial;
        this.fechaCarga = fechaCarga;
        this.listPracticas = listPracticas;
        this.listResultados = listResultados;
        this.paciente = paciente;
        this.numeroSucursal = paciente.getSucursalAsignada();
    }

    public PeticionDTO(int id, String obraSocial, List<PracticaDTO> listPracticas, List<ResultadoDTO> listResultados, PacienteDTO paciente) {
        this.id = id;
        this.obraSocial = obraSocial;
        this.listPracticas = listPracticas;
        this.listResultados = listResultados;
        this.paciente = paciente;
        this.numeroSucursal = paciente.getSucursalAsignada();
    }

    public PeticionDTO(String obraSocial, List<PracticaDTO> listPracticas, PacienteDTO paciente) {
        this.obraSocial = obraSocial;
        this.listPracticas = listPracticas;
        this.listResultados = new ArrayList<>();
        this.paciente = paciente;
        this.numeroSucursal = paciente.getSucursalAsignada();
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

    public int getNumeroSucursal() {
        return numeroSucursal;
    }

    public void setNumeroSucursal(int numeroSucursal) {
        this.numeroSucursal = numeroSucursal;
    }

    public void addResultado(ResultadoDTO resultadoDTO) {
        listResultados.add(resultadoDTO);
    }

    public void removeResultado(ResultadoDTO resultadoDTO) {
        listResultados.remove(resultadoDTO);
    }

    public boolean isFinalizada() {
        if (listResultados.isEmpty()) {
            return false;
        } else return listPracticas.size() == listResultados.size();
    }

    @Override
    public String toString() {
        return id + " - " + paciente.getNombre();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PeticionDTO peticionDTO = (PeticionDTO) obj;
        return id == peticionDTO.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}