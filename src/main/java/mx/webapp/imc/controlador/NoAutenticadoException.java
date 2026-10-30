package mx.webapp.imc.controlador;

/**
 * La petición no tiene una sesión activa. El servicio REST responde 401.
 */
public class NoAutenticadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoAutenticadoException(String mensaje) {
        super(mensaje);
    }
}
