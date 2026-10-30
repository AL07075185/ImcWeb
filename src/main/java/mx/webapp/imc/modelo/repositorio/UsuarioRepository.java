package mx.webapp.imc.modelo.repositorio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import mx.webapp.imc.modelo.entidad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Acceso a la tabla usuario con JDBC y PreparedStatement.
 */
@Repository
public class UsuarioRepository {

    private static final String COLUMNAS =
            "idUsuario, nombre, apellidoPaterno, apellidoMaterno, nombreUsuario, contrasenaHash, "
            + "edad, sexo, estatura, fechaRegistro";

    private static final String SQL_INSERTAR =
            "INSERT INTO usuario (nombre, apellidoPaterno, apellidoMaterno, nombreUsuario, "
            + "contrasenaHash, edad, sexo, estatura, fechaRegistro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_POR_NOMBRE_USUARIO =
            "SELECT " + COLUMNAS + " FROM usuario WHERE nombreUsuario = ?";

    private static final String SQL_POR_ID =
            "SELECT " + COLUMNAS + " FROM usuario WHERE idUsuario = ?";

    private static final String SQL_EXISTE =
            "SELECT COUNT(*) FROM usuario WHERE nombreUsuario = ?";

    private ConexionBd conexionBd;

    public UsuarioRepository() {
    }

    /** Inserta el usuario y regresa el idUsuario generado. */
    public int insertar(Usuario usuario) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellidoPaterno());
            if (usuario.getApellidoMaterno() == null) {
                sentencia.setNull(3, Types.VARCHAR);
            } else {
                sentencia.setString(3, usuario.getApellidoMaterno());
            }
            sentencia.setString(4, usuario.getNombreUsuario());
            sentencia.setString(5, usuario.getContrasenaHash());
            sentencia.setInt(6, usuario.getEdad());
            sentencia.setString(7, String.valueOf(usuario.getSexo()));
            sentencia.setBigDecimal(8, BigDecimal.valueOf(usuario.getEstatura()).setScale(2, RoundingMode.HALF_UP));
            sentencia.setObject(9, usuario.getFechaRegistro());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                llaves.next();
                return llaves.getInt(1);
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible registrar al usuario.", ex);
        }
    }

    /** Regresa el usuario o null si el nombre de usuario no existe. */
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return buscarUno(SQL_POR_NOMBRE_USUARIO, nombreUsuario);
    }

    /** Regresa el usuario o null si el id no existe. */
    public Usuario buscarPorId(int idUsuario) {
        return buscarUno(SQL_POR_ID, idUsuario);
    }

    public boolean existeNombreUsuario(String nombreUsuario) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_EXISTE)) {
            sentencia.setString(1, nombreUsuario);
            try (ResultSet resultado = sentencia.executeQuery()) {
                resultado.next();
                return resultado.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible consultar el nombre de usuario.", ex);
        }
    }

    private Usuario buscarUno(String sql, Object parametro) {
        try (Connection conexion = conexionBd.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setObject(1, parametro);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? mapear(resultado) : null;
            }
        } catch (SQLException ex) {
            throw new AccesoDatosException("No fue posible consultar al usuario.", ex);
        }
    }

    private Usuario mapear(ResultSet resultado) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getInt("idUsuario"));
        usuario.setNombre(resultado.getString("nombre"));
        usuario.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        usuario.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        usuario.setNombreUsuario(resultado.getString("nombreUsuario"));
        usuario.setContrasenaHash(resultado.getString("contrasenaHash"));
        usuario.setEdad(resultado.getInt("edad"));
        usuario.setSexo(resultado.getString("sexo").charAt(0));
        usuario.setEstatura(resultado.getDouble("estatura"));
        usuario.setFechaRegistro(resultado.getObject("fechaRegistro", LocalDateTime.class));
        return usuario;
    }

    public ConexionBd getConexionBd() {
        return conexionBd;
    }

    @Autowired
    public void setConexionBd(ConexionBd conexionBd) {
        this.conexionBd = conexionBd;
    }
}
