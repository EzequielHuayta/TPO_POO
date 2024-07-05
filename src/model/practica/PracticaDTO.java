package model.practica;

public class PracticaDTO {

    private int codigo;
    private final String nombre;
    private final int grupo;
    private final int valoresCriticos;
    private final int valoresReservados;
    private final int cantidadHorasResultados;
    private final boolean habilitada;

    public PracticaDTO(int codigo, String nombre, int grupo, int valoresCriticos, int valoresReservados, int cantidadHorasResultados, boolean habilitada) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.grupo = grupo;
        this.valoresCriticos = valoresCriticos;
        this.valoresReservados = valoresReservados;
        this.cantidadHorasResultados = cantidadHorasResultados;
        this.habilitada = habilitada;
    }

    public PracticaDTO(String nombre, int grupo, int valoresCriticos, int valoresReservados, int cantidadHorasResultados, boolean habilitada) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.valoresCriticos = valoresCriticos;
        this.valoresReservados = valoresReservados;
        this.cantidadHorasResultados = cantidadHorasResultados;
        this.habilitada = habilitada;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int nuevoCodigo) {
        codigo = nuevoCodigo;
    }

    public String getNombre() {
        return nombre;
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
