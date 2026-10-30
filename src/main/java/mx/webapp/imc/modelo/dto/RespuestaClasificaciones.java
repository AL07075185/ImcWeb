package mx.webapp.imc.modelo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.webapp.imc.modelo.entidad.ClasificacionImc;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Recurso "clasificaciones": catálogo de rangos de IMC de la OMS con sus
 * enlaces de hipermedia.
 */
@JsonPropertyOrder({"clasificaciones", "enlaces"})
@JacksonXmlRootElement(localName = "respuestaClasificaciones")
public class RespuestaClasificaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @JacksonXmlElementWrapper(localName = "clasificaciones")
    @JacksonXmlProperty(localName = "clasificacionImc")
    private List<ClasificacionImc> clasificaciones = new ArrayList<>();

    @JacksonXmlElementWrapper(localName = "enlaces")
    @JacksonXmlProperty(localName = "enlace")
    private List<Enlace> enlaces = new ArrayList<>();

    public RespuestaClasificaciones() {
    }

    public RespuestaClasificaciones(List<ClasificacionImc> clasificaciones, List<Enlace> enlaces) {
        this.clasificaciones = clasificaciones;
        this.enlaces = enlaces;
    }

    public List<ClasificacionImc> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(List<ClasificacionImc> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public List<Enlace> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(List<Enlace> enlaces) {
        this.enlaces = enlaces;
    }
}
