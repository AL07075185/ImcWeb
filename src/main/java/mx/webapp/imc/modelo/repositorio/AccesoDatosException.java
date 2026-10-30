package mx.webapp.imc.modelo.repositorio;

/**
 * Error al consultar o modificar la base de datos. Envuelve la SQLException para
 * que los controladores respondan con el código 500.
 */
public class AccesoDatosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
