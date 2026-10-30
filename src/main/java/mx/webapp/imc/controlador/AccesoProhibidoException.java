package mx.webapp.imc.controlador;

/**
 * El usuario de la sesión intenta consultar datos de otro usuario. El servicio
 * REST responde 403.
 */
public class AccesoProhibidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccesoProhibidoException(String mensaje) {
        super(mensaje);
    }
}
