package mx.webapp.imc.modelo.entidad;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Registro de la tabla usuario. La estatura se captura una sola vez y se usa en
 * todos los cálculos de IMC del usuario.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreUsuario;
    private String contrasenaHash;
    private int edad;
    private char sexo;
    private double estatura;
    private LocalDateTime fechaRegistro;

    public Usuario() {
    }

    /** Une nombre y apellidos omitiendo el apellido materno cuando no existe. */
    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder();
        for (String parte : new String[] {nombre, apellidoPaterno, apellidoMaterno}) {
            if (parte != null && !parte.isBlank()) {
                if (nombreCompleto.length() > 0) {
                    nombreCompleto.append(' ');
                }
                nombreCompleto.append(parte.trim());
            }
        }
        return nombreCompleto.toString();
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
