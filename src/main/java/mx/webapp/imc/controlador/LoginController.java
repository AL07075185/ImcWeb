package mx.webapp.imc.controlador;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mx.webapp.imc.modelo.entidad.Usuario;
import mx.webapp.imc.modelo.servicio.UsuarioService;
import mx.webapp.imc.modelo.servicio.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Inicio de sesión (CU_0002): GET /login muestra el formulario y POST /login
 * autentica al usuario y lo guarda en la sesión.
 */
@Controller
public class LoginController {

    private UsuarioService usuarioService;

    public LoginController() {
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarFormulario(
            @SessionAttribute(name = AutenticacionInterceptor.USUARIO_SESION, required = false) Usuario usuario) {
        return usuario != null ? "redirect:/calculo" : "login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@RequestParam(name = "nombreUsuario", defaultValue = "") String nombreUsuario,
                                @RequestParam(name = "contrasena", defaultValue = "") String contrasena,
                                HttpServletRequest request, Model model) {
        try {
            Usuario usuario = usuarioService.autenticar(nombreUsuario, contrasena);
            HttpSession sesion = request.getSession(true);
            request.changeSessionId();
            sesion.setAttribute(AutenticacionInterceptor.USUARIO_SESION, usuario);
            return "redirect:/calculo";
        } catch (ValidacionException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("nombreUsuario", nombreUsuario);
            return "login";
        }
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    @Autowired
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}
