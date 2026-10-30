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
 * Recurso "medición": una medición individual con sus enlaces de hipermedia.
 */
@JsonPropertyOrder({"medicion", "enlaces"})
@JacksonXmlRootElement(localName = "respuestaMedicion")
public class RespuestaMedicion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Medicion medicion;

    @JacksonXmlElementWrapper(localName = "enlaces")
    @JacksonXmlProperty(localName = "enlace")
    private List<Enlace> enlaces = new ArrayList<>();

    public RespuestaMedicion() {
    }

    public RespuestaMedicion(Medicion medicion, List<Enlace> enlaces) {
        this.medicion = medicion;
        this.enlaces = enlaces;
    }

    public Medicion getMedicion() {
        return medicion;
    }

    public void setMedicion(Medicion medicion) {
        this.medicion = medicion;
    }

    public List<Enlace> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(List<Enlace> enlaces) {
        this.enlaces = enlaces;
    }
}
