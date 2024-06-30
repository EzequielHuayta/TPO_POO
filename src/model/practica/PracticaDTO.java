package model.practica;

public class PracticaDTO {

    private int codigoPractica;
    private String nombrePractica;
    private int grupo;
    private int valoresCriticos;
    private int valoresReservados;
    private int cantidadHorasResultados;
    private boolean habilitada;

    public PracticaDTO(int codigoPractica, String nombrePractica, int grupo, int valoresCriticos, int valoresReservados, int cantidadHorasResultados, boolean habilitada) {
        this.codigoPractica = codigoPractica;
        this.nombrePractica = nombrePractica;
        this.grupo = grupo;
        this.valoresCriticos = valoresCriticos;
        this.valoresReservados = valoresReservados;
        this.cantidadHorasResultados = cantidadHorasResultados;
        this.habilitada = habilitada;
    }

    public int getCodigoPractica() {
        return codigoPractica;
    }

    public String getNombrePractica() {
        return nombrePractica;
    }

    public int getGrupo() {
        return grupo;
    }

    public int getValoresCriticos() {
        return valoresCriticos;
    }

    public int getValoresReservados() {
        return valoresReservados;
    }

    public int getCantidadHorasResultados() {
        return cantidadHorasResultados;
    }

    public boolean isHabilitada() {
        return habilitada;
    }

}
