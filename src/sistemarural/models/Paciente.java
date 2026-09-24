package sistemarural.models;

/**
 * Paciente
 * --------
 * Refleja EXACTAMENTE las columnas de la tabla 'pacientes':
 * dni, nombres, apellidos, fecha_nacimiento. No se agregan campos que
 * no existan en la base de datos real, para que lo que ves en el
 * objeto sea siempre lo que hay en la fila.
 */
public class Paciente extends Persona {

    public Paciente(String dni, String nombres, String apellidos, String fechaNacimiento) {
        super(dni, nombres, apellidos, fechaNacimiento);
    }

    /**
     * POLIMORFISMO: implementación específica del rol para un Paciente.
     */
    @Override
    public String obtenerRol() {
        return "Paciente";
    }
}
