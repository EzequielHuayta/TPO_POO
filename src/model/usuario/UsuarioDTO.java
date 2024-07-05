package model.usuario;

import java.util.Date;

public class UsuarioDTO {
    private int id;
    private final String email;
    private final String password;
    private final String nombre;
    private final String domicilio;
    private final int dni;
    private final Date fechaNacimiento;
    private final Rol rol;

    public UsuarioDTO(int id, String email, String password, String nombre, String domicilio, int dni, Date fechaNacimiento, Rol rol) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.rol = rol;
    }

    public UsuarioDTO(String email, String password, String nombre, String domicilio, int dni, Date fechaNacimiento, Rol rol) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int newID) {
        id = newID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Rol getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public int getDni() {
        return dni;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
