package mx.webapp.imc.controlador;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.webapp.imc.modelo.entidad.Usuario;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Protege /calculo, /historial y /api/** (se registra en dispatcher-servlet.xml).
 * Sin sesión, las páginas redirigen al inicio de sesión y el servicio REST
 * responde 401 en JSON o XML.
 */
public class AutenticacionInterceptor implements HandlerInterceptor {

    /** Nombre del atributo de sesión que guarda al usuario autenticado. */
    public static final String USUARIO_SESION = "usuarioSesion";

    public AutenticacionInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // OPTIONS solo informa los métodos permitidos (encabezado Allow), no expone datos
        if (tieneSesionActiva(request) || "OPTIONS".equals(request.getMethod())) {
            return true;
        }
        String ruta = request.getRequestURI().substring(request.getContextPath().length());
        if (ruta.startsWith("/api/")) {
            throw new NoAutenticadoException("Inicia sesión para consumir el servicio REST.");
        }
        response.sendRedirect(request.getContextPath() + "/login?sesion=expirada");
        return false;
    }

    private boolean tieneSesionActiva(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        return sesion != null && sesion.getAttribute(USUARIO_SESION) instanceof Usuario;
    }
}
