package sistemarural.models;

/**
 * Cita
 * ----
 * Representa una cita médica agendada entre un paciente y un integrante
 * del personal de salud. Responde directamente al requerimiento de
 * software "gestionar citas médicas" identificado en el caso del
 * Centro de Salud Rural 'Santa Rosa'.
 */
public class Cita {

    private String id;
    private String dniPaciente;
    private String dniPersonal;
    private String fecha; // yyyy-MM-dd
    private String hora;  // HH:mm
    private String motivo;
    private EstadoCita estado;

    public Cita(String id, String dniPaciente, String dniPersonal,
                String fecha, String hora, String motivo) {
        this.id = id;
        this.dniPaciente = dniPaciente;
        this.dniPersonal = dniPersonal;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = EstadoCita.PROGRAMADA;
    }

    public String getId() {
        return id;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public String getDniPersonal() {
        return dniPersonal;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format("Cita[%s] %s %s - Paciente:%s - Personal:%s - %s (%s)",
                id, fecha, hora, dniPaciente, dniPersonal, motivo, estado);
    }
}
