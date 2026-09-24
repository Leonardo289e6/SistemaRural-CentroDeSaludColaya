package sistemarural.exceptions;

/**
 * DatoInvalidoException
 * -----------------------
 * Excepción de validación que se lanza cuando un dato ingresado no
 * cumple el formato esperado (ej. un DNI que no tiene 8 dígitos). Forma
 * parte del criterio de éxito medible: "el sistema no debe permitir el
 * registro de datos con formato incorrecto".
 */
public class DatoInvalidoException extends Exception {

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
