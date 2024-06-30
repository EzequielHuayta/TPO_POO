package model.paciente;

public class PacienteDTO {

    private int dni;
    private String nombre;
    private String apellido;
    private String mail;
    private char sexo;
    private int edad;

    public PacienteDTO(int dni, String nombre, String apellido, String mail, char sexo, int edad) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.sexo = sexo;
        this.edad = edad;
    }

    public int getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getMail() {
        return mail;
    }

    public char getSexo() {
        return sexo;
    }

    public int getEdad() {
        return edad;
    }

}

