package model.resultado;

import model.practica.PracticaDTO;

public class ResultadoDTO {

    private int resultadoID;
    private final PracticaDTO tipoPractica;
    private final int valor;
    private final int peticionAsociada;

    public ResultadoDTO(int resultadoID, PracticaDTO tipoPractica, int valor, int peticionAsociada) {
        this.resultadoID = resultadoID;
        this.tipoPractica = tipoPractica;
        this.valor = valor;
        this.peticionAsociada = peticionAsociada;
    }

    public ResultadoDTO(PracticaDTO tipoPractica, int valor, int peticionAsociada) {
        this.tipoPractica = tipoPractica;
        this.valor = valor;
        this.peticionAsociada = peticionAsociada;
    }

    public int getResultadoID() {
        return resultadoID;
    }

    public void setResultadoID(int nuevoID) {
        resultadoID = nuevoID;
    }

    public PracticaDTO getTipoPractica() {
        return tipoPractica;
    }

    public int getPeticionAsociada() {
        return peticionAsociada;
    }

    public int getValor() {
        return valor;
    }

    public boolean esCritico() {
        return valor >= tipoPractica.getValoresCriticos();
    }

    public boolean esReservado() {
        return valor >= tipoPractica.getValoresReservados();
    }

    @Override
    public String toString() {
        return Integer.toString(resultadoID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ResultadoDTO resultadoDTO = (ResultadoDTO) obj;
        return resultadoID == resultadoDTO.resultadoID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(resultadoID);
    }
}
