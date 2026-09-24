package sistemarural.controllers;

import java.util.List;
import java.util.stream.Collectors;
import sistemarural.exceptions.DatoInvalidoException;
import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.exceptions.PersonaNoEncontradaException;
import sistemarural.models.Atencion;
import sistemarural.models.Paciente;
import sistemarural.security.DataMasker;
import sistemarural.services.HistorialService;

/**
 * HistorialController
 * ---------------------
 * Traduce las excepciones de negocio y de persistencia en mensajes
 * listos para mostrar en la UI (o en la consola).
 */
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    public String registrarPaciente(Paciente paciente) {
        try {
            historialService.registrarNuevoPaciente(paciente);
            return "Paciente guardado en la base de datos: " + paciente.getNombreCompleto();
        } catch (DatoInvalidoException e) {
            return "Error de validación: " + e.getMessage();
        } catch (ErrorPersistenciaException e) {
            return "Error de base de datos: " + e.getMessage();
        }
    }

    public String buscarPacientePorDni(String dni) {
        try {
            Paciente paciente = historialService.buscarPaciente(dni);
            return "Paciente encontrado: " + paciente.getInformacion()
                    + " | DNI: " + DataMasker.enmascararDni(paciente.getDni());
        } catch (PersonaNoEncontradaException e) {
            return "Aviso: " + e.getMessage();
        } catch (ErrorPersistenciaException e) {
            return "Error de base de datos: " + e.getMessage();
        }
    }

    public String registrarAtencion(String dniPaciente, String fecha, String diagnostico, String tratamiento) {
        try {
            historialService.registrarAtencion(dniPaciente, fecha, diagnostico, tratamiento);
            return "Atención registrada correctamente para el paciente " + dniPaciente;
        } catch (DatoInvalidoException e) {
            return "Error de validación: " + e.getMessage();
        } catch (PersonaNoEncontradaException e) {
            return "No se puede registrar la atención: " + e.getMessage();
        } catch (ErrorPersistenciaException e) {
            return "Error de base de datos: " + e.getMessage();
        }
    }

    public List<String> verHistoriaClinica(String dniPaciente) {
        try {
            return historialService.obtenerHistoria(dniPaciente).stream()
                    .map(Atencion::toString)
                    .collect(Collectors.toList());
        } catch (ErrorPersistenciaException e) {
            return List.of("Error de base de datos: " + e.getMessage());
        }
    }
}
