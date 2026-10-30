package mx.webapp.imc.controlador.rest;

import mx.webapp.imc.controlador.AccesoProhibidoException;
import mx.webapp.imc.controlador.NoAutenticadoException;
import mx.webapp.imc.modelo.dto.Enlace;
import mx.webapp.imc.modelo.entidad.Usuario;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Utilidades compartidas por los controladores REST: creación de enlaces de
 * hipermedia con URL absolutas y validación del propietario del recurso.
 */
public final class Hipermedia {

    private Hipermedia() {
    }

    /**
     * Crea un enlace a partir de una plantilla relativa al contexto, por ejemplo
     * enlace("self", "GET", "/api/usuarios/{idUsuario}", 1).
     */
    public static Enlace enlace(String rel, String metodo, String plantilla, Object... valores) {
        String href = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(plantilla)
                .buildAndExpand(valores)
                .toUriString();
        return new Enlace(rel, href, metodo);
    }

    /**
     * Comprueba que el usuario de la sesión sea el dueño del recurso.
     *
     * @throws NoAutenticadoException si no hay sesión (401)
     * @throws AccesoProhibidoException si el recurso es de otro usuario (403)
     */
    public static Usuario validarPropietario(Usuario usuarioSesion, int idUsuario) {
        if (usuarioSesion == null) {
            throw new NoAutenticadoException("Inicia sesión para consumir el servicio REST.");
        }
        if (usuarioSesion.getIdUsuario() != idUsuario) {
            throw new AccesoProhibidoException("Solo puedes consultar tus propios datos.");
        }
        return usuarioSesion;
    }
}
