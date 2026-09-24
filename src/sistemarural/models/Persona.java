package sistemarural.models;

/**
 * Persona (clase abstracta)
 * --------------------------
 * Superclase del dominio que agrupa los atributos comunes a cualquier
 * persona registrada en el sistema del Centro de Salud Rural: tanto
 * pacientes como personal de salud comparten estos datos.
 *
 * Aplica HERENCIA: Paciente y PersonalSalud extienden esta clase.
 * Aplica POLIMORFISMO: el método obtenerRol() se declara abstracto aquí
 * y cada subclase lo implementa a su manera; getInformacion() lo invoca
 * sin saber en tiempo de compilación qué subtipo concreto es.
 *
 * Los atributos son "protected" (no privados) porque las subclases los
 * necesitan directamente, pero siguen sin ser accesibles desde fuera del
 * paquete models sin pasar por los getters/setters (encapsulamiento).
 */
public abstract class Persona {

    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String fechaNacimiento; // formato: yyyy-MM-dd
    protected String telefono;

    protected Persona(String dni, String nombres, String apellidos,
                       String fechaNacimiento, String telefono) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    /**
     * Método abstracto: cada subclase (Paciente, PersonalSalud) define
     * cuál es su rol dentro del sistema. Es la base del polimorfismo.
     */
    public abstract String obtenerRol();

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Ejemplo de POLIMORFISMO en uso: este método es el mismo para toda
     * Persona, pero el texto que produce cambia según la subclase real
     * del objeto, porque obtenerRol() se resuelve en tiempo de ejecución.
     */
    public String getInformacion() {
        return String.format("[%s] %s (DNI: %s)", obtenerRol(), getNombreCompleto(), dni);
    }

    @Override
    public String toString() {
        return getInformacion();
    }
}
