package mx.webapp.imc.controlador;

import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.webapp.imc.modelo.dto.ErrorRespuesta;
import mx.webapp.imc.modelo.repositorio.AccesoDatosException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Manejo global de errores. Para las páginas muestra error.jsp; para rutas
 * /api/** que no llegaron a un controlador REST (ruta o método inexistente)
 * responde con ErrorRespuesta en JSON o XML.
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ManejadorErroresWeb {

    private static final Logger LOG = Logger.getLogger(ManejadorErroresWeb.class.getName());

    public ManejadorErroresWeb() {
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public Object noEncontrado(Exception ex, HttpServletRequest request) {
        return responder(request, HttpStatus.NOT_FOUND, "Página no encontrada",
                "La página que buscas no existe o fue movida. Regresa al inicio para continuar.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object metodoNoPermitido(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return responder(request, HttpStatus.METHOD_NOT_ALLOWED, "Método no permitido",
                "La ruta no admite el método " + ex.getMethod() + ".");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Object tipoNoSoportado(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return responder(request, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Formato no soportado",
                "Envía el cuerpo como application/json o application/xml.");
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public Object tipoNoAceptable(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request) {
        return responder(request, HttpStatus.NOT_ACCEPTABLE, "Formato no disponible",
                "Solicita la representación con Accept: application/json o application/xml.");
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public Object peticionIncompleta(ServletRequestBindingException ex, HttpServletRequest request) {
        return responder(request, HttpStatus.BAD_REQUEST, "Petición incompleta",
                "Faltan datos en la petición. Regresa al inicio e inténtalo de nuevo.");
    }

    @ExceptionHandler(AccesoDatosException.class)
    public Object accesoDatos(AccesoDatosException ex, HttpServletRequest request) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);
        return responder(request, HttpStatus.INTERNAL_SERVER_ERROR, "Base de datos no disponible",
                "No fue posible conectar con la base de datos. Intenta más tarde.");
    }

    @ExceptionHandler(Exception.class)
    public Object general(Exception ex, HttpServletRequest request) {
        LOG.log(Level.SEVERE, "Error no controlado en " + request.getRequestURI(), ex);
        return responder(request, HttpStatus.INTERNAL_SERVER_ERROR, "Error del servidor",
                "Ocurrió un error inesperado. Intenta de nuevo en unos minutos.");
    }

    /** Regresa ResponseEntity para rutas del API y ModelAndView para páginas. */
    private Object responder(HttpServletRequest request, HttpStatus estado, String titulo, String mensaje) {
        String ruta = request.getRequestURI().substring(request.getContextPath().length());
        if (ruta.startsWith("/api/")) {
            ErrorRespuesta error = new ErrorRespuesta(estado.value(), estado.getReasonPhrase(), mensaje, null,
                    request.getRequestURI());
            return ResponseEntity.status(estado).body(error);
        }
        ModelAndView vista = new ModelAndView("error");
        vista.setStatus(estado);
        vista.addObject("codigo", estado.value());
        vista.addObject("titulo", titulo);
        vista.addObject("mensaje", mensaje);
        return vista;
    }
}
