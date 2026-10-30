package mx.webapp.imc.modelo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

/**
 * Control de hipermedia (HATEOAS): relación, dirección y método HTTP con el que
 * el cliente puede continuar la navegación del API.
 */
@JsonPropertyOrder({"rel", "href", "metodo"})
public class Enlace implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rel;
    private String href;
    private String metodo;

    public Enlace() {
    }

    public Enlace(String rel, String href, String metodo) {
        this.rel = rel;
        this.href = href;
        this.metodo = metodo;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}
