package mx.webapp.imc.modelo.repositorio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mx.webapp.imc.modelo.entidad.ClasificacionImc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Acceso al catálogo clasificacionImc (rangos de la OMS).
 */
@Repository
public class ClasificacionImcRepository {

    private static final String COLUMNAS = "idClasificacion, nombre, imcMinimo, imcMaximo";

    private static final String SQL_POR_IMC =
            "SELECT " + COLUMNAS + " FROM clasificacionImc WHERE ? BETWEEN imcMinimo AND imcMaximo";

    private static final String SQL_POR_ID =
            "SELECT " + COLUMNAS + " FROM clasificacionImc WHERE idClasificacion = ?";

    private static final String SQL_TODAS =
            "SELECT " + COLUMNAS + " FROM clasificacionImc ORDER BY imcMinimo";

    private ConexionBd conexionBd;

    public ClasificacionImcRepository() {
    }

    /** Regresa la clasificación cuyo rango contiene el IMC, o null si no hay. */
    public ClasificacionImc buscarPorImc(double imc) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_POR_IMC)) {
            sentencia.setBigDecimal(1, BigDecimal.valueOf(imc).setScale(2, RoundingMode.HALF_UP));
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? mapear(resultado) : null;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible obtener la clasificación del IMC.", ex);
        }
    }

    /** Regresa la clasificación o null si el id no existe. */
    public ClasificacionImc buscarPorId(int idClasificacion) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_POR_ID)) {
            sentencia.setInt(1, idClasificacion);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? mapear(resultado) : null;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible obtener la clasificación.", ex);
        }
    }

    public List<ClasificacionImc> listarTodas() {
        List<ClasificacionImc> clasificaciones = new ArrayList<>();
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_TODAS);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                clasificaciones.add(mapear(resultado));
            }
            return clasificaciones;
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible obtener las clasificaciones.", ex);
        }
    }

    private ClasificacionImc mapear(ResultSet resultado) throws SQLException {
        ClasificacionImc clasificacion = new ClasificacionImc();
        clasificacion.setIdClasificacion(resultado.getInt("idClasificacion"));
        clasificacion.setNombre(resultado.getString("nombre"));
        clasificacion.setImcMinimo(resultado.getDouble("imcMinimo"));
        clasificacion.setImcMaximo(resultado.getDouble("imcMaximo"));
        return clasificacion;
    }

    public ConexionBd getConexionBd() {
        return conexionBd;
    }

    @Autowired
    public void setConexionBd(ConexionBd conexionBd) {
        this.conexionBd = conexionBd;
    }
}
