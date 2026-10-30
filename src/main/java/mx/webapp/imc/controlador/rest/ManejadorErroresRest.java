package mx.webapp.imc.controlador.rest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.webapp.imc.controlador.AccesoProhibidoException;
import mx.webapp.imc.controlador.NoAutenticadoException;
import mx.webapp.imc.modelo.dto.ErrorRespuesta;
import mx.webapp.imc.modelo.repositorio.AccesoDatosException;
import mx.webapp.imc.modelo.servicio.RecursoNoEncontradoException;
import mx.webapp.imc.modelo.servicio.ValidacionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Convierte las excepciones de los controladores REST en un ErrorRespuesta con
 * el código HTTP correspondiente. La representación (JSON o XML) se elige con
 * el mismo encabezado Accept de la petición.
 */
@RestControllerAdvice(basePackages = "mx.webapp.imc.controlador.rest")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ManejadorErroresRest {

    private static final Logger LOG = Logger.getLogger(ManejadorErroresRest.class.getName());

    public ManejadorErroresRest() {
    }

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<ErrorRespuesta> validacion(ValidacionException ex, HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getCampo(), request);
    }

    @ExceptionHandler(NoAutenticadoException.class)
    public ResponseEntity<ErrorRespuesta> noAutenticado(NoAutenticadoException ex, HttpServletRequest request) {
        return responder(HttpStatus.UNAUTHORIZED, ex.getMessage(), null, request);
    }

    @ExceptionHandler(AccesoProhibidoException.class)
    public ResponseEntity<ErrorRespuesta> accesoProhibido(AccesoProhibidoException ex, HttpServletRequest request) {
        return responder(HttpStatus.FORBIDDEN, ex.getMessage(), null, request);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest request) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage(), null, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> tipoInvalido(MethodArgumentTypeMismatchException ex,
                                                       HttpServletRequest request) {
        String mensaje = "El valor '" + ex.getValue() + "' no es válido para " + ex.getName() + "; debe ser numérico.";
        return responder(HttpStatus.BAD_REQUEST, mensaje, ex.getName(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> cuerpoInvalido(HttpMessageNotReadableException ex,
                                                         HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON o XML válido, por ejemplo {\"peso\": 76.5}.", "peso", request);
    }

    @ExceptionHandler(AccesoDatosException.class)
    public ResponseEntity<ErrorRespuesta> accesoDatos(AccesoDatosException ex, HttpServletRequest request) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR,
                "No fue posible acceder a la base de datos. Intenta más tarde.", null, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> general(Exception ex, HttpServletRequest request) {
        LOG.log(Level.SEVERE, "Error no controlado en " + request.getRequestURI(), ex);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor.", null, request);
    }

    private ResponseEntity<ErrorRespuesta> responder(HttpStatus estado, String mensaje, String campo,
                                                     HttpServletRequest request) {
        ErrorRespuesta error = new ErrorRespuesta(estado.value(), estado.getReasonPhrase(), mensaje, campo,
                request.getRequestURI());
        return ResponseEntity.status(estado).body(error);
    }
}
