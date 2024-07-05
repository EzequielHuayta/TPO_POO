package model.resultado;

import model.practica.PracticaDTO;

public class ResultadoDTO {

    private int id;

    private PracticaDTO tipoPractica;

    private int valor;

    public ResultadoDTO(int id, PracticaDTO tipoPractica, int valor) {
        this.id = id;
        this.tipoPractica = tipoPractica;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public PracticaDTO getTipoPractica() {
        return tipoPractica;
    }

    public String getNombrePractica() {
        return tipoPractica.getNombre();
    }
    public int getValor() {
        return valor;
    }
}
