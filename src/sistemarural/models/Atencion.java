package sistemarural.models;

/**
 * Atencion
 * ---------
 * Refleja una fila de la tabla 'historias_clinicas': paciente_dni,
 * fecha_atencion, diagnóstico y tratamiento (el 'id' lo genera la base
 * de datos automáticamente, por eso no aparece aquí como campo editable).
 *
 * Es una relación de COMPOSICIÓN respecto al paciente: una atención no
 * tiene sentido sin el paciente al que pertenece.
 */
public class Atencion {

    private Long id; // null hasta que la BD le asigna un id real
    private String dniPaciente;
    private String fecha;
    private String diagnostico;
    private String tratamiento;

    public Atencion(String dniPaciente, String fecha, String diagnostico, String tratamiento) {
        this.dniPaciente = dniPaciente;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    @Override
    public String toString() {
        return fecha + " - Dx: " + diagnostico + " - Tto: " + tratamiento;
    }
}
