package mx.webapp.imc.modelo.servicio;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excepción de negocio: uno o más datos no cumplen las reglas. Conserva el
 * mensaje de cada campo para marcarlo en el formulario o en la respuesta REST.
 */
public class ValidacionException extends Exception {

    private static final long serialVersionUID = 1L;

    private String campo;
    private Map<String, String> errores;

    /** Error de un solo campo. */
    public ValidacionException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
        this.errores = new LinkedHashMap<>();
        this.errores.put(campo, mensaje);
    }

    /** Errores de varios campos; el primero se usa como mensaje principal. */
    public ValidacionException(Map<String, String> errores) {
        super(errores.values().iterator().next());
        this.campo = errores.keySet().iterator().next();
        this.errores = new LinkedHashMap<>(errores);
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public Map<String, String> getErrores() {
        return Collections.unmodifiableMap(errores);
    }

    public void setErrores(Map<String, String> errores) {
        this.errores = new LinkedHashMap<>(errores);
    }
}
