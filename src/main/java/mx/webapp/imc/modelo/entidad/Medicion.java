package mx.webapp.imc.modelo.entidad;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.time.LocalDateTime;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Registro de la tabla medicion: peso capturado, IMC calculado y su clasificación.
 */
@JsonPropertyOrder({"idMedicion", "idUsuario", "fechaMedicion", "peso", "imc", "clasificacion"})
@JacksonXmlRootElement(localName = "medicion")
public class Medicion implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idMedicion;
    private int idUsuario;
    private double peso;
    private double imc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaMedicion;
    private ClasificacionImc clasificacion;

    public Medicion() {
    }

    public int getIdMedicion() {
        return idMedicion;
    }

    public void setIdMedicion(int idMedicion) {
        this.idMedicion = idMedicion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public LocalDateTime getFechaMedicion() {
        return fechaMedicion;
    }

    public void setFechaMedicion(LocalDateTime fechaMedicion) {
        this.fechaMedicion = fechaMedicion;
    }

    public ClasificacionImc getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(ClasificacionImc clasificacion) {
        this.clasificacion = clasificacion;
    }
}
