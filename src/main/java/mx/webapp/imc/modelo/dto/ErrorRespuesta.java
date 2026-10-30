package mx.webapp.imc.modelo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.time.LocalDateTime;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Representación de un error del servicio REST, en JSON o en XML según el
 * encabezado Accept de la petición.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"codigo", "error", "mensaje", "campo", "ruta", "fecha"})
@JacksonXmlRootElement(localName = "errorRespuesta")
public class ErrorRespuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    private int codigo;
    private String error;
    private String mensaje;
    private String campo;
    private String ruta;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;

    public ErrorRespuesta() {
        this.fecha = LocalDateTime.now().withNano(0);
    }

    public ErrorRespuesta(int codigo, String error, String mensaje, String campo, String ruta) {
        this();
        this.codigo = codigo;
        this.error = error;
        this.mensaje = mensaje;
        this.campo = campo;
        this.ruta = ruta;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
