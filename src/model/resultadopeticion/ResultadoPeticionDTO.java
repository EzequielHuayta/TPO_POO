package model.resultadopeticion;

import model.practica.PracticaDTO;

public class ResultadoPeticionDTO {

    private int id;

    private PracticaDTO tipoPractica;

    private int valor;

    public ResultadoPeticionDTO(int id, PracticaDTO tipoPractica, int valor) {
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
        return tipoPractica.getNombrePractica();
    }
    public int getValor() {
        return valor;
    }
}
