package mx.webapp.imc.modelo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mx.webapp.imc.modelo.entidad.Usuario;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Recurso "usuario": datos del perfil sin la contraseña, con enlaces a sus
 * mediciones.
 */
@JsonPropertyOrder({"idUsuario", "nombreUsuario", "nombreCompleto", "edad", "sexo", "estatura",
    "fechaRegistro", "enlaces"})
@JacksonXmlRootElement(localName = "respuestaUsuario")
public class RespuestaUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private String nombreUsuario;
    private String nombreCompleto;
    private int edad;
    private String sexo;
    private double estatura;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRegistro;

    @JacksonXmlElementWrapper(localName = "enlaces")
    @JacksonXmlProperty(localName = "enlace")
    private List<Enlace> enlaces = new ArrayList<>();

    public RespuestaUsuario() {
    }

    /** Copia los datos públicos del usuario; nunca incluye contrasenaHash. */
    public RespuestaUsuario(Usuario usuario, List<Enlace> enlaces) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.edad = usuario.getEdad();
        this.sexo = String.valueOf(usuario.getSexo());
        this.estatura = usuario.getEstatura();
        this.fechaRegistro = usuario.getFechaRegistro();
        this.enlaces = enlaces;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
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

    public List<Enlace> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(List<Enlace> enlaces) {
        this.enlaces = enlaces;
    }
}
