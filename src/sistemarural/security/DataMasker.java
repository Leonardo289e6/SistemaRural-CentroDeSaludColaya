package sistemarural.security;

/**
 * DataMasker
 * -----------
 * Utilidad encargada de enmascarar información sensible antes de mostrarla
 * en pantalla o guardarla en registros/logs, en cumplimiento de la
 * Ley N.° 29733 - Ley de Protección de Datos Personales.
 */
public class DataMasker {

    // Constructor privado: es una clase de utilidades, no se instancia
    private DataMasker() {
    }

    /**
     * Enmascara un DNI dejando visibles solo los últimos 2 dígitos.
     * Ej: "47581234" -> "******34"
     */
    public static String enmascararDni(String dni) {
        if (dni == null || dni.length() < 2) {
            return "****";
        }
        String visible = dni.substring(dni.length() - 2);
        return "*".repeat(dni.length() - 2) + visible;
    }

    /**
     * Enmascara un número de teléfono dejando visibles solo los últimos 3
     * Ej: "987654321" -> "******321"
     */
    public static String enmascararTelefono(String telefono) {
        if (telefono == null || telefono.length() < 3) {
            return "***";
        }
        String visible = telefono.substring(telefono.length() - 3);
        return "*".repeat(telefono.length() - 3) + visible;
    }
}
