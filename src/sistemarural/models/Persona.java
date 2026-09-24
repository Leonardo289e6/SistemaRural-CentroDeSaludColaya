package sistemarural.models;

/**
 * Persona (clase abstracta)
 * --------------------------
 * Superclase del dominio con los datos que SÍ existen en la tabla
 * 'pacientes' de la base de datos real: dni, nombres, apellidos y
 * fecha de nacimiento.
 *
 * Nota de diseño: la tabla 'personal_medico' no guarda dni, apellidos
 * ni fecha de nacimiento (solo usuario, nombres y rol), así que los
 * objetos PersonalSalud construidos desde el login dejan esos campos
 * vacíos. Se mantiene la herencia porque ambos tipos SÍ comparten
 * "nombres" y el comportamiento polimórfico de obtenerRol().
 */
public abstract class Persona {

    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String fechaNacimiento; // formato: yyyy-MM-dd

    protected Persona(String dni, String nombres, String apellidos, String fechaNacimiento) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Método abstracto: cada subclase define su rol. Es la base del
     * polimorfismo (Persona no sabe qué tipo concreto la implementa).
     */
    public abstract String obtenerRol();

    public String getDni() {
        return dni;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getNombreCompleto() {
        if (apellidos == null || apellidos.isBlank()) {
            return nombres;
        }
        return nombres + " " + apellidos;
    }

    /**
     * POLIMORFISMO en uso: el texto cambia según la subclase real del
     * objeto, porque obtenerRol() se resuelve en tiempo de ejecución.
     */
    public String getInformacion() {
        return String.format("[%s] %s", obtenerRol(), getNombreCompleto());
    }

    @Override
    public String toString() {
        return getInformacion();
    }
}
