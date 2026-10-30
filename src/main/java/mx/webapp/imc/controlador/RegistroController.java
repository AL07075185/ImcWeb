package mx.webapp.imc.controlador;

import mx.webapp.imc.modelo.entidad.Usuario;
import mx.webapp.imc.modelo.servicio.UsuarioService;
import mx.webapp.imc.modelo.servicio.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Registro de usuarios (CU_0001): GET /registro muestra el formulario y
 * POST /registro valida y guarda la cuenta.
 */
@Controller
public class RegistroController {

    private UsuarioService usuarioService;

    public RegistroController() {
    }

    @GetMapping("/registro")
    public String mostrarFormulario(
            @SessionAttribute(name = AutenticacionInterceptor.USUARIO_SESION, required = false) Usuario usuario,
            Model model) {
        if (usuario != null) {
            return "redirect:/calculo";
        }
        model.addAttribute("formularioRegistro", new FormularioRegistro());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute("formularioRegistro") FormularioRegistro formulario, Model model) {
        try {
            usuarioService.registrar(formulario.crearUsuario(), formulario.getContrasena(), formulario.getConfirmacion());
            return "redirect:/login?registro=ok";
        } catch (ValidacionException ex) {
            formulario.limpiarContrasenas();
            model.addAttribute("errores", ex.getErrores());
            return "registro";
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
