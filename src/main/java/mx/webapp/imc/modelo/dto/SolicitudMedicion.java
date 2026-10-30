package mx.webapp.imc.modelo.dto;

import java.io.Serializable;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Cuerpo de la petición POST /api/usuarios/{idUsuario}/mediciones, en JSON
 * ({"peso": 76.5}) o en XML (&lt;solicitudMedicion&gt;&lt;peso&gt;76.5&lt;/peso&gt;&lt;/solicitudMedicion&gt;).
 */
@JacksonXmlRootElement(localName = "solicitudMedicion")
public class SolicitudMedicion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double peso;

    public SolicitudMedicion() {
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }
}
