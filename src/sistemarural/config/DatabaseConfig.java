package sistemarural.config;

/**
 * DatabaseConfig
 * ----------------
 * Centraliza los datos de conexión a la base de datos, leyéndolos SOLO de
 * variables de entorno. Nunca se escriben credenciales en el código fuente
 * ni se suben al repositorio Git.
 *
 * Variables obligatorias (si falta alguna, el programa avisa y no conecta):
 *   DB_HOST, DB_USER, DB_PASSWORD
 *
 * Variables opcionales (tienen valor por defecto no sensible):
 *   DB_PORT (6543), DB_NAME (postgres), DB_SSLMODE (require)
 */
public final class DatabaseConfig {

    private DatabaseConfig() {
    }

    /** Lee una variable obligatoria; si no existe, falla con un mensaje claro. */
    private static String requerir(String nombre) {
        String valor = System.getenv(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta la variable de entorno " + nombre);
        }
        return valor;
    }

    public static String getHost() {
        return requerir("DB_HOST");
    }

    public static String getPort() {
        return System.getenv().getOrDefault("DB_PORT", "6543");
    }

    public static String getDatabase() {
        return System.getenv().getOrDefault("DB_NAME", "postgres");
    }

    public static String getUser() {
        return requerir("DB_USER");
    }

    public static String getPassword() {
        return requerir("DB_PASSWORD");
    }

    public static String getSslMode() {
        return System.getenv().getOrDefault("DB_SSLMODE", "require");
    }

    /**
     * Construye la URL JDBC completa a partir de los datos anteriores.
     */
    public static String construirUrlJdbc() {
        return "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDatabase()
                + "?sslmode=" + getSslMode();
    }
}
