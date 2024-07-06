package model.sucursal;

public class SucursalDTO {

    private int numero;
    private final String direccion;
    private final int responsableTecnico;

    public SucursalDTO(int numero, String direccion, int responsableTecnico) {
        this.numero = numero;
        this.direccion = direccion;
        this.responsableTecnico = responsableTecnico;
    }

    public SucursalDTO(String direccion, int responsableTecnico) {
        this.direccion = direccion;
        this.responsableTecnico = responsableTecnico;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int nuevoNumero) {
        numero = nuevoNumero;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getResponsableTecnico() {
        return responsableTecnico;
    }

    @Override
    public String toString() {
        return direccion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SucursalDTO sucursal = (SucursalDTO) obj;
        return numero == sucursal.numero;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }
}
