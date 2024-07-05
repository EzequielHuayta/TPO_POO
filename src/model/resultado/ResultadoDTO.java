package model.resultado;

import model.peticion.PeticionDTO;
import model.practica.PracticaDTO;

public class ResultadoDTO {

    private int id;
    private final PracticaDTO tipoPractica;
    private final int valor;
    private final PeticionDTO peticionAsociada;

    public ResultadoDTO(int id, PracticaDTO tipoPractica, int valor, PeticionDTO peticionAsociada) {
        this.id = id;
        this.tipoPractica = tipoPractica;
        this.valor = valor;
        this.peticionAsociada = peticionAsociada;
    }

    public ResultadoDTO(PracticaDTO tipoPractica, int valor, PeticionDTO peticionAsociada) {
        this.tipoPractica = tipoPractica;
        this.valor = valor;
        this.peticionAsociada = peticionAsociada;
    }

    public int getId() {
        return id;
    }

    public PracticaDTO getTipoPractica() {
        return tipoPractica;
    }

    public PeticionDTO getPeticionAsociada() {
        return peticionAsociada;
    }

    public String getNombrePractica() {
        return tipoPractica.getNombre();
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
}
