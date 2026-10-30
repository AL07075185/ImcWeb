package mx.webapp.imc.modelo.servicio;

import java.time.LocalDateTime;
import java.util.List;
import mx.webapp.imc.modelo.entidad.ClasificacionImc;
import mx.webapp.imc.modelo.entidad.Medicion;
import mx.webapp.imc.modelo.entidad.Usuario;
import mx.webapp.imc.modelo.repositorio.ClasificacionImcRepository;
import mx.webapp.imc.modelo.repositorio.MedicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Reglas de negocio de las mediciones: cálculo y clasificación del IMC,
 * registro en la base de datos y consulta del historial.
 */
@Service
public class MedicionService {

    private MedicionRepository medicionRepository;
    private ClasificacionImcRepository clasificacionImcRepository;

    public MedicionService() {
    }

    /**
     * Calcula el IMC con la estatura registrada del usuario, lo clasifica y
     * guarda la medición.
     *
     * @throws ValidacionException si el peso es 0, negativo o está fuera de rango
     */
    public Medicion registrarMedicion(Usuario usuario, double peso) throws ValidacionException {
        CalculadoraImc calculadora = new CalculadoraImc(peso, usuario.getEstatura());
        double imc = calculadora.calcularImc();

        ClasificacionImc clasificacion = clasificacionImcRepository.buscarPorImc(imc);
        if (clasificacion == null) {
            throw new ValidacionException("peso", "No existe una clasificación para el IMC " + imc + ".");
        }

        Medicion medicion = new Medicion();
        medicion.setIdUsuario(usuario.getIdUsuario());
        medicion.setPeso(calculadora.getPeso());
        medicion.setImc(imc);
        medicion.setFechaMedicion(LocalDateTime.now().withNano(0));
        medicion.setClasificacion(clasificacion);
        medicion.setIdMedicion(medicionRepository.insertar(medicion));
        return medicion;
    }

    /** Historial del usuario, de la medición más reciente a la más antigua. */
    public List<Medicion> obtenerHistorial(int idUsuario) {
        return medicionRepository.listarPorUsuario(idUsuario);
    }

    /**
     * Última medición del usuario.
     *
     * @throws RecursoNoEncontradoException si el usuario aún no tiene mediciones
     */
    public Medicion obtenerImcActual(int idUsuario) {
        Medicion medicion = medicionRepository.obtenerUltima(idUsuario);
        if (medicion == null) {
            throw new RecursoNoEncontradoException("El usuario aún no tiene mediciones registradas.");
        }
        return medicion;
    }

    /**
     * Una medición del usuario.
     *
     * @throws RecursoNoEncontradoException si no existe o pertenece a otro usuario
     */
    public Medicion obtenerMedicion(int idUsuario, int idMedicion) {
        Medicion medicion = medicionRepository.buscarPorId(idMedicion, idUsuario);
        if (medicion == null) {
            throw new RecursoNoEncontradoException("No existe la medición " + idMedicion + " para el usuario.");
        }
        return medicion;
    }

    /**
     * Elimina una medición del usuario.
     *
     * @throws RecursoNoEncontradoException si no existe o pertenece a otro usuario
     */
    public void eliminarMedicion(int idUsuario, int idMedicion) {
        if (!medicionRepository.eliminar(idMedicion, idUsuario)) {
            throw new RecursoNoEncontradoException("No existe la medición " + idMedicion + " para el usuario.");
        }
    }

    /** Catálogo de rangos de la OMS ordenado por IMC mínimo. */
    public List<ClasificacionImc> obtenerClasificaciones() {
        return clasificacionImcRepository.listarTodas();
    }

    /**
     * Un rango del catálogo de la OMS.
     *
     * @throws RecursoNoEncontradoException si el id no existe
     */
    public ClasificacionImc obtenerClasificacion(int idClasificacion) {
        ClasificacionImc clasificacion = clasificacionImcRepository.buscarPorId(idClasificacion);
        if (clasificacion == null) {
            throw new RecursoNoEncontradoException("No existe la clasificación con id " + idClasificacion + ".");
        }
        return clasificacion;
    }

    public MedicionRepository getMedicionRepository() {
        return medicionRepository;
    }

    @Autowired
    public void setMedicionRepository(MedicionRepository medicionRepository) {
        this.medicionRepository = medicionRepository;
    }

    public ClasificacionImcRepository getClasificacionImcRepository() {
        return clasificacionImcRepository;
    }

    @Autowired
    public void setClasificacionImcRepository(ClasificacionImcRepository clasificacionImcRepository) {
        this.clasificacionImcRepository = clasificacionImcRepository;
    }
}
