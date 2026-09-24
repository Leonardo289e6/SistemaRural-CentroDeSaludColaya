package sistemarural.models;

/**
 * Atencion
 * ---------
 * Representa un registro individual de atención médica (una fecha, un
 * diagnóstico, un tratamiento y quién la realizó). No tiene sentido que
 * una Atencion exista fuera de una HistoriaClinica: es una relación de
 * COMPOSICIÓN (ciclo de vida dependiente), a diferencia de la relación
 * de AGREGACIÓN entre Paciente e HistoriaClinica (esta última sí puede
 * existir referenciada de forma independiente).
 */
public class Atencion {

    private String fecha; // yyyy-MM-dd
    private String diagnostico;
    private String tratamiento;
    private String dniPersonalResponsable;

    public Atencion(String fecha, String diagnostico, String tratamiento,
                     String dniPersonalResponsable) {
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.dniPersonalResponsable = dniPersonalResponsable;
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

    public String getDniPersonalResponsable() {
        return dniPersonalResponsable;
    }

    @Override
    public String toString() {
        return fecha + " - Dx: " + diagnostico + " - Tto: " + tratamiento;
    }
}
