package mx.webapp.imc.controlador.rest;

import java.util.ArrayList;
import java.util.List;
import mx.webapp.imc.modelo.dto.Enlace;
import mx.webapp.imc.modelo.dto.RespuestaClasificaciones;
import mx.webapp.imc.modelo.entidad.ClasificacionImc;
import mx.webapp.imc.modelo.servicio.MedicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Recurso REST "clasificaciones": catálogo público de rangos de IMC de la OMS.
 *
 * <pre>
 * GET /api/clasificaciones
 * GET /api/clasificaciones/{idClasificacion}
 * </pre>
 */
@RestController
@RequestMapping(value = "/api/clasificaciones",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class ClasificacionRestController {

    private MedicionService medicionService;

    public ClasificacionRestController() {
    }

    @GetMapping
    public ResponseEntity<RespuestaClasificaciones> obtenerClasificaciones() {
        List<ClasificacionImc> clasificaciones = medicionService.obtenerClasificaciones();
        List<Enlace> enlaces = new ArrayList<>();
        enlaces.add(Hipermedia.enlace("self", "GET", "/api/clasificaciones"));
        for (ClasificacionImc clasificacion : clasificaciones) {
            enlaces.add(Hipermedia.enlace("clasificacion", "GET", "/api/clasificaciones/{idClasificacion}",
                    clasificacion.getIdClasificacion()));
        }
        return ResponseEntity.ok(new RespuestaClasificaciones(clasificaciones, enlaces));
    }

    @GetMapping("/{idClasificacion}")
    public ResponseEntity<RespuestaClasificaciones> obtenerClasificacion(
            @PathVariable("idClasificacion") int idClasificacion) {
        ClasificacionImc clasificacion = medicionService.obtenerClasificacion(idClasificacion);
        List<Enlace> enlaces = new ArrayList<>();
        enlaces.add(Hipermedia.enlace("self", "GET", "/api/clasificaciones/{idClasificacion}", idClasificacion));
        enlaces.add(Hipermedia.enlace("catalogo", "GET", "/api/clasificaciones"));
        return ResponseEntity.ok(new RespuestaClasificaciones(List.of(clasificacion), enlaces));
    }

    public MedicionService getMedicionService() {
        return medicionService;
    }

    @Autowired
    public void setMedicionService(MedicionService medicionService) {
        this.medicionService = medicionService;
    }
}
