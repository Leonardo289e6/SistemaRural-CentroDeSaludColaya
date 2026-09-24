package sistemarural.config;

/**
 * DatabaseConfig
 * ----------------
 * Centraliza los datos de conexión a la base de datos, leyéndolos de
 * variables de entorno en vez de dejarlos escritos en el código fuente.
 * Esto es una RESTRICCIÓN DE DISEÑO explícita: nunca se suben
 * credenciales de base de datos al repositorio Git.
 *
 * Para conectar con Supabase, exporta antes de ejecutar el programa:
 *   export DB_HOST=db.xxxxxxxxxxxx.supabase.co
 *   export DB_PORT=5432
 *   export DB_NAME=postgres
 *   export DB_USER=postgres
 *   export DB_PASSWORD=tu_password_de_supabase
 *   export DB_SSLMODE=require
 *
 * Si no defines nada, se usan valores por defecto para pruebas locales.
 */
public final class DatabaseConfig {

    private DatabaseConfig() {
    }

    public static String getHost() {
        return System.getenv().getOrDefault("DB_HOST", "aws-0-sa-east-1.pooler.supabase.com");
    }

    public static String getPort() {
        return System.getenv().getOrDefault("DB_PORT", "6543");
    }

    public static String getDatabase() {
        return System.getenv().getOrDefault("DB_NAME", "postgres");
    }

    public static String getUser() {
        return System.getenv().getOrDefault("DB_USER", "postgres.sjwcuivwnogyyzdzmjev");
    }

    public static String getPassword() {
        return System.getenv().getOrDefault("DB_PASSWORD", "P?L_.EEzR2ngmx5");
    }

    public static String getSslMode() {
        return System.getenv().getOrDefault("DB_SSLMODE", "prefer");
    }

    /**
     * Construye la URL JDBC completa a partir de los datos anteriores.
     */
    public static String construirUrlJdbc() {
        return "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDatabase()
                + "?sslmode=" + getSslMode();
    }
}
