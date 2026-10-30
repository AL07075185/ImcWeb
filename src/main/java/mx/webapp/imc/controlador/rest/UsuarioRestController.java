package mx.webapp.imc.controlador.rest;

import java.util.ArrayList;
import java.util.List;
import mx.webapp.imc.controlador.AutenticacionInterceptor;
import mx.webapp.imc.modelo.dto.Enlace;
import mx.webapp.imc.modelo.dto.RespuestaUsuario;
import mx.webapp.imc.modelo.entidad.Usuario;
import mx.webapp.imc.modelo.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Recurso REST "usuarios": perfil del usuario de la sesión, sin la contraseña,
 * con enlaces a sus mediciones.
 *
 * <pre>
 * GET /api/usuarios/{idUsuario}
 * </pre>
 */
@RestController
@RequestMapping(value = "/api/usuarios",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class UsuarioRestController {

    private UsuarioService usuarioService;

    public UsuarioRestController() {
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<RespuestaUsuario> obtenerUsuario(
            @PathVariable("idUsuario") int idUsuario,
            @SessionAttribute(name = AutenticacionInterceptor.USUARIO_SESION, required = false) Usuario usuarioSesion) {
        Hipermedia.validarPropietario(usuarioSesion, idUsuario);
        Usuario usuario = usuarioService.obtenerUsuario(idUsuario);

        List<Enlace> enlaces = new ArrayList<>();
        enlaces.add(Hipermedia.enlace("self", "GET", "/api/usuarios/{idUsuario}", idUsuario));
        enlaces.add(Hipermedia.enlace("mediciones", "GET", "/api/usuarios/{idUsuario}/mediciones", idUsuario));
        enlaces.add(Hipermedia.enlace("actual", "GET", "/api/usuarios/{idUsuario}/mediciones/actual", idUsuario));
        enlaces.add(Hipermedia.enlace("registrarMedicion", "POST", "/api/usuarios/{idUsuario}/mediciones", idUsuario));
        return ResponseEntity.ok(new RespuestaUsuario(usuario, enlaces));
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    @Autowired
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}
