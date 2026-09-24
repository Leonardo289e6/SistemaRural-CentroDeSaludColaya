package sistemarural.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * CloudDbConnection
 * ------------------
 * Representa la única conexión JDBC activa hacia la base de datos en la
 * nube (PostgreSQL / Supabase) del Centro de Salud Rural.
 *
 * Aplica el patrón SINGLETON: garantiza que en toda la aplicación exista
 * una sola instancia de conexión, evitando abrir múltiples canales hacia
 * la nube desde un establecimiento rural con ancho de banda limitado.
 *
 * A diferencia de una versión de demostración, esta clase SÍ abre una
 * conexión JDBC real usando el driver de PostgreSQL (org.postgresql.Driver)
 * y los datos de DatabaseConfig.
 */
public class CloudDbConnection {

    private static CloudDbConnection instanciaUnica;

    private Connection conexion;

    private CloudDbConnection() {
    }

    /**
     * Punto de acceso global a la instancia única.
     */
    public static CloudDbConnection obtenerInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new CloudDbConnection();
        }
        return instanciaUnica;
    }

    /**
     * Abre la conexión JDBC real si aún no existe o si se cerró.
     * Se puede llamar varias veces sin riesgo: si ya hay una conexión
     * abierta, la reutiliza en vez de crear una nueva (Singleton).
     */
    public Connection obtenerConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            String url = DatabaseConfig.construirUrlJdbc();
            System.out.println("[CloudDbConnection] Conectando a: " + url);
            conexion = DriverManager.getConnection(
                    url, DatabaseConfig.getUser(), DatabaseConfig.getPassword());
            System.out.println("[CloudDbConnection] Conexión establecida correctamente.");
        }
        return conexion;
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[CloudDbConnection] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("[CloudDbConnection] Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
