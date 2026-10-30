package mx.webapp.imc.controlador.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import mx.webapp.imc.controlador.AutenticacionInterceptor;
import mx.webapp.imc.modelo.dto.Enlace;
import mx.webapp.imc.modelo.dto.RespuestaHistorial;
import mx.webapp.imc.modelo.dto.RespuestaMedicion;
import mx.webapp.imc.modelo.dto.SolicitudMedicion;
import mx.webapp.imc.modelo.entidad.Medicion;
import mx.webapp.imc.modelo.entidad.Usuario;
import mx.webapp.imc.modelo.servicio.MedicionService;
import mx.webapp.imc.modelo.servicio.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Recurso REST "mediciones" del usuario (CU_0006). Responde JSON o XML según el
 * encabezado Accept o el parámetro ?formato=json|xml.
 *
 * <pre>
 * GET    /api/usuarios/{idUsuario}/mediciones               historial completo
 * GET    /api/usuarios/{idUsuario}/mediciones/actual        IMC actual
 * GET    /api/usuarios/{idUsuario}/mediciones/{idMedicion}  una medición
 * POST   /api/usuarios/{idUsuario}/mediciones               registra una medición (201)
 * DELETE /api/usuarios/{idUsuario}/mediciones/{idMedicion}  elimina una medición (204)
 * </pre>
 */
@RestController
@RequestMapping(value = "/api/usuarios/{idUsuario}/mediciones",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class HistorialRestController {

    private static final String RUTA_MEDICIONES = "/api/usuarios/{idUsuario}/mediciones";
    private static final String RUTA_MEDICION = RUTA_MEDICIONES + "/{idMedicion}";

    private MedicionService medicionService;

    public HistorialRestController() {
    }

    @GetMapping
    public ResponseEntity<RespuestaHistorial> obtenerHistorial(
            @PathVariable("idUsuario") int idUsuario,
            @SessionAttribute(name = AutenticacionInterceptor.USUARIO_SESION, required = false) Usuario usuarioSesion) {
        Usuario usuario = Hipermedia.validarPropietario(usuarioSesion, idUsuario);
        List<Medicion> mediciones = medicionService.obtenerHistorial(idUsuario);

        RespuestaHistorial respuesta = new RespuestaHistorial();
        respuesta.setIdUsuario(idUsuario);
        respuesta.setNombreCompleto(usuario.getNombreCompleto());
        respuesta.setTotalMediciones(mediciones.size());
        respuesta.setImcActual(mediciones.isEmpty() ? null : mediciones.get(0));
        respuesta.setMediciones(mediciones);
        respuesta.setEnlaces(crearEnlacesHistorial(idUsuario));
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/actual")
    public ResponseEntity<RespuestaMedicion> obtenerImcActual(
            @PathVariable("idUsuario") int idUsuario,
            @SessionAttribute(name = AutenticacionInterceptor.USUARIO_SESION, required = false) Usuario usuarioSesion) {
        Hipermedia.validarPropietario(usuarioSesion, idUsuario);
        Medicion medicion = medicionService.obtenerImcActual(idUsuario);
        return ResponseEntity.ok(new RespuestaMedicion(medicion, crearEnlacesMedicion(medicion)));
    }

    @GetMapping("/{idMedicion}")
    public ResponseEntity<RespuestaMedicion> obtenerMedicion(
            @PathVariable("idUsuario") int idUsuario,
            @PathVariable("idMedicion") int idMedicion,
            @SessionAttribute(name = AutenticacionInterceptor.USUARIO_SESION, required = false) Usuario usuarioSesion) {
        Hipermedia.validarPropietario(usuarioSesion, idUsuario);
        Medicion medicion = medicionService.obtenerMedicion(idUsuario, idMedicion);
        return ResponseEntity.ok(new RespuestaMedicion(medicion, crearEnlacesMedicion(medicion)));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<RespuestaMedicion> registrarMedicion(
            @PathVariable("idUsuario") int idUsuario,
            @RequestBody SolicitudMedicion solicitud,
            @SessionAttribute(name = AutenticacionInterceptor.USUARIO_SESION, required = false) Usuario usuarioSesion)
            throws ValidacionException {
        Usuario usuario = Hipermedia.validarPropietario(usuarioSesion, idUsuario);
        if (solicitud == null || solicitud.getPeso() == null) {
            throw new ValidacionException("peso", "El peso es obligatorio.");
        }
        Medicion medicion = medicionService.registrarMedicion(usuario, solicitud.getPeso());
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RUTA_MEDICION)
                .buildAndExpand(idUsuario, medicion.getIdMedicion())
                .toUri();
        return ResponseEntity.created(ubicacion).body(new RespuestaMedicion(medicion, crearEnlacesMedicion(medicion)));
    }

    @DeleteMapping("/{idMedicion}")
    public ResponseEntity<Void> eliminarMedicion(
            @PathVariable("idUsuario") int idUsuario,
            @PathVariable("idMedicion") int idMedicion,
            @SessionAttribute(name = AutenticacionInterceptor.USUARIO_SESION, required = false) Usuario usuarioSesion) {
        Hipermedia.validarPropietario(usuarioSesion, idUsuario);
        medicionService.eliminarMedicion(idUsuario, idMedicion);
        return ResponseEntity.noContent().build();
    }

    private List<Enlace> crearEnlacesHistorial(int idUsuario) {
        List<Enlace> enlaces = new ArrayList<>();
        enlaces.add(Hipermedia.enlace("self", "GET", RUTA_MEDICIONES, idUsuario));
        enlaces.add(Hipermedia.enlace("actual", "GET", RUTA_MEDICIONES + "/actual", idUsuario));
        enlaces.add(Hipermedia.enlace("registrar", "POST", RUTA_MEDICIONES, idUsuario));
        enlaces.add(Hipermedia.enlace("usuario", "GET", "/api/usuarios/{idUsuario}", idUsuario));
        enlaces.add(Hipermedia.enlace("clasificaciones", "GET", "/api/clasificaciones"));
        enlaces.add(Hipermedia.enlace("nuevoCalculo", "POST", "/calculo"));
        return enlaces;
    }

    private List<Enlace> crearEnlacesMedicion(Medicion medicion) {
        int idUsuario = medicion.getIdUsuario();
        int idMedicion = medicion.getIdMedicion();
        List<Enlace> enlaces = new ArrayList<>();
        enlaces.add(Hipermedia.enlace("self", "GET", RUTA_MEDICION, idUsuario, idMedicion));
        enlaces.add(Hipermedia.enlace("historial", "GET", RUTA_MEDICIONES, idUsuario));
        enlaces.add(Hipermedia.enlace("eliminar", "DELETE", RUTA_MEDICION, idUsuario, idMedicion));
        enlaces.add(Hipermedia.enlace("clasificacion", "GET", "/api/clasificaciones/{idClasificacion}",
                medicion.getClasificacion().getIdClasificacion()));
        return enlaces;
    }

    public MedicionService getMedicionService() {
        return medicionService;
    }

    @Autowired
    public void setMedicionService(MedicionService medicionService) {
        this.medicionService = medicionService;
    }
}
