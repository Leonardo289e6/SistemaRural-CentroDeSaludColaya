package sistemarural.models;

/**
 * Paciente
 * --------
 * Representa a un paciente del Centro de Salud Rural. HEREDA de Persona
 * los datos comunes y agrega su propia información: el tipo de seguro de
 * salud (dato relevante en el contexto rural peruano: SIS, EsSalud,
 * particular) y el identificador de su historia clínica.
 */
public class Paciente extends Persona {

    private String tipoSeguro;      // ej. "SIS", "EsSalud", "Particular"
    private String idHistoriaClinica;

    public Paciente(String dni, String nombres, String apellidos,
                     String fechaNacimiento, String telefono, String tipoSeguro) {
        super(dni, nombres, apellidos, fechaNacimiento, telefono);
        this.tipoSeguro = tipoSeguro;
        this.idHistoriaClinica = "HC-" + dni;
    }

    public String getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public String getIdHistoriaClinica() {
        return idHistoriaClinica;
    }

    /**
     * POLIMORFISMO: implementación específica del rol para un Paciente.
     */
    @Override
    public String obtenerRol() {
        return "Paciente";
    }

    @Override
    public String getInformacion() {
        return super.getInformacion() + " | Seguro: " + tipoSeguro;
    }
}
