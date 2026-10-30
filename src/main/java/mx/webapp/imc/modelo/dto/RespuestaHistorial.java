package mx.webapp.imc.modelo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.webapp.imc.modelo.entidad.Medicion;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Recurso "historial de mediciones" de un usuario: IMC actual, lista de
 * mediciones (de la más reciente a la más antigua) y enlaces de hipermedia.
 */
@JsonPropertyOrder({"idUsuario", "nombreCompleto", "totalMediciones", "imcActual", "mediciones", "enlaces"})
@JacksonXmlRootElement(localName = "respuestaHistorial")
public class RespuestaHistorial implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private String nombreCompleto;
    private int totalMediciones;
    private Medicion imcActual;

    @JacksonXmlElementWrapper(localName = "mediciones")
    @JacksonXmlProperty(localName = "medicion")
    private List<Medicion> mediciones = new ArrayList<>();

    @JacksonXmlElementWrapper(localName = "enlaces")
    @JacksonXmlProperty(localName = "enlace")
    private List<Enlace> enlaces = new ArrayList<>();

    public RespuestaHistorial() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getTotalMediciones() {
        return totalMediciones;
    }

    public void setTotalMediciones(int totalMediciones) {
        this.totalMediciones = totalMediciones;
    }

    public Medicion getImcActual() {
        return imcActual;
    }

    public void setImcActual(Medicion imcActual) {
        this.imcActual = imcActual;
    }

    public List<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    public List<Enlace> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(List<Enlace> enlaces) {
        this.enlaces = enlaces;
    }
}
