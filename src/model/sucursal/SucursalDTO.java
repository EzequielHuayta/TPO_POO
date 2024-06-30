package model.sucursal;

public class SucursalDTO {

    private int numero;
    private String direccion;
    private int responsableTecnico;

    public SucursalDTO(int numero, String direccion, int responsableTecnico) {
        this.numero = numero;
        this.direccion = direccion;
        this.responsableTecnico = responsableTecnico;
    }

    public int getNumero() {
        return numero;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getResponsableTecnico() {
        return responsableTecnico;
    }
}
