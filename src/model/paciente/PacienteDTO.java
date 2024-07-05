package model.paciente;

public class PacienteDTO {
    private int id;
    private final int dni;
    private final String nombre;
    private final String email;
    private final String domicilio;
    private final char sexo;
    private final int edad;
    private final int sucursalAsignada;

    public PacienteDTO(int id, int dni, String nombre, String email, String domicilio, char sexo, int edad, int sucursalAsignada) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.email = email;
        this.sexo = sexo;
        this.edad = edad;
        this.sucursalAsignada = sucursalAsignada;
    }

    public PacienteDTO(int dni, String nombre, String email, String domicilio, char sexo, int edad, int sucursalAsignada) {
        this.dni = dni;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.email = email;
        this.sexo = sexo;
        this.edad = edad;
        this.sucursalAsignada = sucursalAsignada;
    }

    public int getId() {
        return id;
    }

    public void setId(int nuevoId){
        id = nuevoId;
    }

    public int getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getEmail() {
        return email;
    }

    public char getSexo() {
        return sexo;
    }

    public int getEdad() {
        return edad;
    }

    public int getSucursalAsignada() {
        return sucursalAsignada;
    }
}
