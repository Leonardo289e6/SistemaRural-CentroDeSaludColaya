package sistemarural.exceptions;

/**
 * PacienteDuplicadoException
 * ----------------------------
 * Se lanza cuando se intenta registrar un paciente cuyo DNI ya existe en
 * la tabla 'pacientes'. Evita que un registro nuevo sobrescriba los datos
 * de un paciente ya guardado.
 */
public class PacienteDuplicadoException extends Exception {

    public PacienteDuplicadoException(String dni) {
        super("Ya existe un paciente registrado con DNI " + dni + ".");
    }
}
