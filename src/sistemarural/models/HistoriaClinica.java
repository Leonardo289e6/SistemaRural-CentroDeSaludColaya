package sistemarural.models;

import java.util.ArrayList;
import java.util.List;

/**
 * HistoriaClinica
 * ---------------
 * Almacena el registro de atenciones médicas (Atencion) de un paciente.
 *
 * Relación de AGREGACIÓN con Paciente: la historia se identifica por el
 * DNI del paciente, pero el paciente puede seguir existiendo de forma
 * independiente si la historia se elimina.
 *
 * Relación de COMPOSICIÓN con Atencion: las atenciones que contiene no
 * tienen sentido ni existencia fuera de esta historia clínica.
 */
public class HistoriaClinica {

    private String dniPaciente;
    private List<Atencion> atenciones;

    public HistoriaClinica(String dniPaciente) {
        this.dniPaciente = dniPaciente;
        this.atenciones = new ArrayList<>();
    }

    public void registrarAtencion(Atencion atencion) {
        atenciones.add(atencion);
    }

    public List<Atencion> getAtenciones() {
        return atenciones;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public int totalAtenciones() {
        return atenciones.size();
    }
}
