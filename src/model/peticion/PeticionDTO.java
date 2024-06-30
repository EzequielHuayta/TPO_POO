package model.peticion;

import model.practica.PracticaDTO;

import java.util.Date;
import java.util.List;

public class PeticionDTO {

    private int id;
    private String obraSocial;
    private Date fechaCarga;
    private Date fechaCalculadaEntrega;
    private List<PracticaDTO> listPracticas;

    public PeticionDTO(int id, String obraSocial, Date fechaCarga, Date fechaCalculadaEntrega, List<PracticaDTO> listPracticas) {
        this.id = id;
        this.obraSocial = obraSocial;
        this.fechaCarga = fechaCarga;
        this.fechaCalculadaEntrega = fechaCalculadaEntrega;
        this.listPracticas = listPracticas;
    }


    public int getId() {
        return id;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public Date getFechaCalculadaEntrega() {
        return fechaCalculadaEntrega;
    }

    public List<PracticaDTO> getListPracticas() {
        return listPracticas;
    }

}