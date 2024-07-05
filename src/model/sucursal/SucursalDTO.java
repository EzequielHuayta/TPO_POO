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

    public void setNumero(int nuevoNumero){
        numero = nuevoNumero;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getResponsableTecnico() {
        return responsableTecnico;
    }
}
