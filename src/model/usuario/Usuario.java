package model.usuario;

public class Usuario {
    private int id;
    private String email;
    private String password;
    private Rol rol;

    public enum Rol {
        RECEPCION, LABORATORISTA, ADMINISTRADOR
    }

    // TODO: Constructor, getters y setters
}

