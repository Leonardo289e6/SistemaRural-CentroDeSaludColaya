package sistemarural.security;

import sistemarural.exceptions.DatoInvalidoException;

/**
 * ValidadorDatos
 * ---------------
 * Valida el formato de los datos personales antes de registrarlos,
 * evitando que información mal escrita (ej: un DNI incompleto) entre al
 * sistema. Es una restricción de diseño explícita del proyecto
 */
public class ValidadorDatos {

    private ValidadorDatos() {
    }

    public static void validarDni(String dni) throws DatoInvalidoException {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new DatoInvalidoException(
                    "El DNI '" + dni + "' no es válido: debe tener exactamente 8 dígitos numéricos.");
        }
    }

    public static void validarTelefono(String telefono) throws DatoInvalidoException {
        if (telefono == null || !telefono.matches("\\d{9}")) {
            throw new DatoInvalidoException(
                    "El teléfono '" + telefono + "' no es válido: debe tener 9 dígitos numéricos.");
        }
    }

    public static void validarFecha(String fecha, String nombreCampo) throws DatoInvalidoException {
        if (fecha == null || !fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new DatoInvalidoException(
                    "El campo '" + nombreCampo + "' debe tener el formato yyyy-MM-dd (ej. 2026-09-23).");
        }
    }

    public static void validarTextoNoVacio(String valor, String nombreCampo) throws DatoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException("El campo '" + nombreCampo + "' no puede estar vacío.");
        }
    }
}
