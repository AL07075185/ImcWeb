package mx.webapp.imc.controlador;

import mx.webapp.imc.modelo.entidad.Medicion;
import mx.webapp.imc.modelo.entidad.Usuario;
import mx.webapp.imc.modelo.servicio.MedicionService;
import mx.webapp.imc.modelo.servicio.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Cálculo del IMC (CU_0003): GET /calculo muestra la captura de peso y
 * POST /calculo registra la medición y redirige al historial (Post/Redirect/Get).
 */
@Controller
public class CalculoImcController {

    private MedicionService medicionService;

    public CalculoImcController() {
    }

    @GetMapping("/calculo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("clasificaciones", medicionService.obtenerClasificaciones());
        return "calculo";
    }

    @PostMapping("/calculo")
    public String calcular(@RequestParam(name = "peso", defaultValue = "") String peso,
                           @SessionAttribute(AutenticacionInterceptor.USUARIO_SESION) Usuario usuario,
                           Model model, RedirectAttributes atributosRedireccion) {
        try {
            Medicion medicion = medicionService.registrarMedicion(usuario, convertirPeso(peso));
            atributosRedireccion.addFlashAttribute("medicionNueva", medicion);
            return "redirect:/historial";
        } catch (ValidacionException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("peso", peso);
            model.addAttribute("clasificaciones", medicionService.obtenerClasificaciones());
            return "calculo";
        }
    }

    private double convertirPeso(String texto) throws ValidacionException {
        String limpio = texto == null ? "" : texto.trim().replace(',', '.');
        if (limpio.isEmpty()) {
            throw new ValidacionException("peso", "Captura tu peso en kilogramos.");
        }
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException ex) {
            throw new ValidacionException("peso", "Captura un peso válido, por ejemplo 76.50.");
        }
    }

    public MedicionService getMedicionService() {
        return medicionService;
    }

    @Autowired
    public void setMedicionService(MedicionService medicionService) {
        this.medicionService = medicionService;
    }
}
