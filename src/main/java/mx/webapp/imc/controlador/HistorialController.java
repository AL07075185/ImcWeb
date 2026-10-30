package mx.webapp.imc.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Historial (CU_0004): GET /historial muestra la vista; la tabla se llena en el
 * navegador consumiendo el servicio REST GET /api/usuarios/{idUsuario}/mediciones.
 */
@Controller
public class HistorialController {

    public HistorialController() {
    }

    @GetMapping("/historial")
    public String mostrarHistorial() {
        return "historial";
    }
}
