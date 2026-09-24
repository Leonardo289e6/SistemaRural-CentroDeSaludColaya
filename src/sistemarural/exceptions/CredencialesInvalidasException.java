package sistemarural.exceptions;

/**
 * CredencialesInvalidasException
 * --------------------------------
 * Se lanza cuando el usuario o la contraseña ingresados en el login no
 * coinciden con ningún registro de la tabla 'personal_medico', o cuando
 * la contraseña no coincide con el hash almacenado. Es la puerta de
 * entrada obligatoria: sin una sesión válida, el programa no permite
 * usar ningún otro servicio (registrar pacientes, atenciones, etc.).
 */
public class CredencialesInvalidasException extends Exception {

    public CredencialesInvalidasException() {
        super("Usuario o contraseña incorrectos. Acceso denegado.");
    }
}
