package mx.webapp.imc.modelo.entidad;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Rango de IMC de la Organización Mundial de la Salud (tabla clasificacionImc).
 */
@JsonPropertyOrder({"idClasificacion", "nombre", "imcMinimo", "imcMaximo"})
@JacksonXmlRootElement(localName = "clasificacionImc")
public class ClasificacionImc implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idClasificacion;
    private String nombre;
    private double imcMinimo;
    private double imcMaximo;

    public ClasificacionImc() {
    }

    /** Indica si el IMC cae dentro del rango, con ambos límites incluidos. */
    public boolean contiene(double imc) {
        return imc >= imcMinimo && imc <= imcMaximo;
    }

    public int getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(int idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getImcMinimo() {
        return imcMinimo;
    }

    public void setImcMinimo(double imcMinimo) {
        this.imcMinimo = imcMinimo;
    }

    public double getImcMaximo() {
        return imcMaximo;
    }

    public void setImcMaximo(double imcMaximo) {
        this.imcMaximo = imcMaximo;
    }
}
