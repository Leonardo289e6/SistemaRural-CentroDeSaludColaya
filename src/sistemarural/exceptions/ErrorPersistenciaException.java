package sistemarural.exceptions;

/**
 * ErrorPersistenciaException
 * ----------------------------
 * Envuelve cualquier SQLException que ocurra al hablar con la base de
 * datos, para que las capas superiores (services/controllers) manejen
 * un único tipo de error de negocio en vez de acoplarse a java.sql.*.
 */
public class ErrorPersistenciaException extends Exception {

    public ErrorPersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
