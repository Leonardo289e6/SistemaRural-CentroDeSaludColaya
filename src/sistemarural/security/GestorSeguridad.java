package sistemarural.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * GestorSeguridad
 * -----------------
 * Complementa a DataMasker: mientras DataMasker oculta datos para
 * mostrarlos en pantalla, esta clase genera un HASH irreversible
 * (SHA-256) que puede usarse como identificador interno o de log sin
 * exponer el dato original en texto plano, conforme a la Ley N.° 29733.
 */
public class GestorSeguridad {

    private GestorSeguridad() {
    }

    /**
     * Genera un hash SHA-256 en hexadecimal a partir de un texto sensible
     * (ej. un DNI). El resultado no puede revertirse al valor original.
     */
    public static String generarHash(String textoSensible) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytesHash = digest.digest(textoSensible.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexBuilder = new StringBuilder();
            for (byte b : bytesHash) {
                hexBuilder.append(String.format("%02x", b));
            }
            return hexBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 siempre está disponible en la JVM estándar; 
            throw new IllegalStateException("Algoritmo de hash no disponible", e);
        }
    }
}
