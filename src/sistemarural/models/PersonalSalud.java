package sistemarural.models;

/**
 * PersonalSalud
 * --------------
 * Representa a un trabajador de salud del establecimiento (enfermero/a,
 * técnico, médico). HEREDA de Persona los datos comunes y agrega el
 * cargo que desempeña y su número de colegiatura profesional.
 *
 * Junto con Paciente, demuestra el uso de HERENCIA (ambas extienden
 * Persona) y de POLIMORFISMO (cada una define obtenerRol() a su manera).
 */
public class PersonalSalud extends Persona {

    private String cargo;               // ej. "Licenciada en Enfermería"
    private String numeroColegiatura;

    public PersonalSalud(String dni, String nombres, String apellidos,
                          String fechaNacimiento, String telefono,
                          String cargo, String numeroColegiatura) {
        super(dni, nombres, apellidos, fechaNacimiento, telefono);
        this.cargo = cargo;
        this.numeroColegiatura = numeroColegiatura;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNumeroColegiatura() {
        return numeroColegiatura;
    }

    /**
     * POLIMORFISMO: implementación específica del rol para el personal
     * de salud; a diferencia de Paciente, aquí el "rol" es su cargo real.
     */
    @Override
    public String obtenerRol() {
        return cargo;
    }

    @Override
    public String getInformacion() {
        return super.getInformacion() + " | Colegiatura: " + numeroColegiatura;
    }
}
