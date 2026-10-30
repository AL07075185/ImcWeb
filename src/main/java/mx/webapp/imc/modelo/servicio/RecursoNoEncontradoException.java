package mx.webapp.imc.modelo.servicio;

/**
 * El recurso solicitado (usuario, medición o clasificación) no existe. El
 * servicio REST la convierte en una respuesta 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
