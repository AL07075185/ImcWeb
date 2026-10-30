package mx.webapp.imc.modelo.repositorio;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Entrega conexiones a la base de datos imcDb. El DataSource se declara en
 * WEB-INF/applicationContext.xml con los datos de bd.properties.
 */
@Component
public class ConexionBd {

    private DataSource dataSource;

    public ConexionBd() {
    }

    /** Abre una conexión; quien la recibe la cierra con try-with-resources. */
    public Connection obtenerConexion() throws SQLException {
        return dataSource.getConnection();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
