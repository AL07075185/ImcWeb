package mx.webapp.imc.modelo.repositorio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mx.webapp.imc.modelo.entidad.ClasificacionImc;
import mx.webapp.imc.modelo.entidad.Medicion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Acceso a la tabla medicion. Las consultas unen cada medición con su
 * clasificación mediante INNER JOIN por rango de IMC.
 */
@Repository
public class MedicionRepository {

    private static final String SQL_INSERTAR =
            "INSERT INTO medicion (idUsuario, peso, imc, fechaMedicion) VALUES (?, ?, ?, ?)";

    private static final String SQL_CONSULTA =
            "SELECT m.idMedicion, m.idUsuario, m.peso, m.imc, m.fechaMedicion, "
            + "c.idClasificacion, c.nombre, c.imcMinimo, c.imcMaximo "
            + "FROM medicion AS m "
            + "INNER JOIN clasificacionImc AS c ON m.imc BETWEEN c.imcMinimo AND c.imcMaximo ";

    private static final String ORDEN = "ORDER BY m.fechaMedicion DESC, m.idMedicion DESC";

    private static final String SQL_POR_USUARIO = SQL_CONSULTA + "WHERE m.idUsuario = ? " + ORDEN;

    private static final String SQL_ULTIMA = SQL_POR_USUARIO + " LIMIT 1";

    private static final String SQL_POR_ID =
            SQL_CONSULTA + "WHERE m.idMedicion = ? AND m.idUsuario = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM medicion WHERE idMedicion = ? AND idUsuario = ?";

    private ConexionBd conexionBd;

    public MedicionRepository() {
    }

    /** Inserta la medición y regresa el idMedicion generado. */
    public int insertar(Medicion medicion) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, medicion.getIdUsuario());
            sentencia.setBigDecimal(2, decimal(medicion.getPeso()));
            sentencia.setBigDecimal(3, decimal(medicion.getImc()));
            sentencia.setObject(4, medicion.getFechaMedicion());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                llaves.next();
                return llaves.getInt(1);
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible guardar la medición.", ex);
        }
    }

    /** Historial del usuario, de la medición más reciente a la más antigua. */
    public List<Medicion> listarPorUsuario(int idUsuario) {
        List<Medicion> mediciones = new ArrayList<>();
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_POR_USUARIO)) {
            sentencia.setInt(1, idUsuario);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    mediciones.add(mapear(resultado));
                }
            }
            return mediciones;
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible consultar el historial.", ex);
        }
    }

    /** Última medición del usuario o null si aún no tiene. */
    public Medicion obtenerUltima(int idUsuario) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ULTIMA)) {
            sentencia.setInt(1, idUsuario);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? mapear(resultado) : null;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible consultar el IMC actual.", ex);
        }
    }

    /** Medición del usuario indicado o null si no existe o pertenece a otro. */
    public Medicion buscarPorId(int idMedicion, int idUsuario) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_POR_ID)) {
            sentencia.setInt(1, idMedicion);
            sentencia.setInt(2, idUsuario);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? mapear(resultado) : null;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible consultar la medición.", ex);
        }
    }

    /** Elimina la medición del usuario; regresa true si existía. */
    public boolean eliminar(int idMedicion, int idUsuario) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setInt(1, idMedicion);
            sentencia.setInt(2, idUsuario);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible eliminar la medición.", ex);
        }
    }

    /** Convierte a DECIMAL(5,2) para guardar el valor exacto con dos decimales. */
    private BigDecimal decimal(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private Medicion mapear(ResultSet resultado) throws SQLException {
        ClasificacionImc clasificacion = new ClasificacionImc();
        clasificacion.setIdClasificacion(resultado.getInt("idClasificacion"));
        clasificacion.setNombre(resultado.getString("nombre"));
        clasificacion.setImcMinimo(resultado.getDouble("imcMinimo"));
        clasificacion.setImcMaximo(resultado.getDouble("imcMaximo"));

        Medicion medicion = new Medicion();
        medicion.setIdMedicion(resultado.getInt("idMedicion"));
        medicion.setIdUsuario(resultado.getInt("idUsuario"));
        medicion.setPeso(resultado.getDouble("peso"));
        medicion.setImc(resultado.getDouble("imc"));
        medicion.setFechaMedicion(resultado.getObject("fechaMedicion", LocalDateTime.class));
        medicion.setClasificacion(clasificacion);
        return medicion;
    }

    public ConexionBd getConexionBd() {
        return conexionBd;
    }

    @Autowired
    public void setConexionBd(ConexionBd conexionBd) {
        this.conexionBd = conexionBd;
    }
}
