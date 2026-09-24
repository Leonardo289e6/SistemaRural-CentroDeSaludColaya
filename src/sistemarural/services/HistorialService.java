package sistemarural.services;

import java.util.List;
import java.util.stream.Collectors;
import sistemarural.exceptions.DatoInvalidoException;
import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.exceptions.PersonaNoEncontradaException;
import sistemarural.models.Atencion;
import sistemarural.models.Paciente;
import sistemarural.repositories.IHistoriaClinicaRepository;
import sistemarural.repositories.IPacienteRepository;
import sistemarural.security.ValidadorDatos;

/**
 * HistorialService
 * -----------------
 * Reglas de negocio para pacientes y sus atenciones médicas.
 */
public class HistorialService {

    private final IPacienteRepository pacienteRepository;
    private final IHistoriaClinicaRepository historiaClinicaRepository;

    public HistorialService(IPacienteRepository pacienteRepository,
                             IHistoriaClinicaRepository historiaClinicaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.historiaClinicaRepository = historiaClinicaRepository;
    }

    public void registrarNuevoPaciente(Paciente paciente)
            throws DatoInvalidoException, ErrorPersistenciaException {
        ValidadorDatos.validarDni(paciente.getDni());
        ValidadorDatos.validarTextoNoVacio(paciente.getNombres(), "nombres");
        ValidadorDatos.validarTextoNoVacio(paciente.getApellidos(), "apellidos");
        ValidadorDatos.validarFecha(paciente.getFechaNacimiento(), "fecha de nacimiento");
        pacienteRepository.guardar(paciente);
    }

    public Paciente buscarPaciente(String dni) throws ErrorPersistenciaException, PersonaNoEncontradaException {
        return pacienteRepository.buscarPorDni(dni);
    }

    public void registrarAtencion(String dniPaciente, String fecha, String diagnostico, String tratamiento)
            throws DatoInvalidoException, ErrorPersistenciaException, PersonaNoEncontradaException {
        // Verifica primero que el paciente exista (integridad referencial
        // a nivel de aplicación, además de la FK en la base de datos).
        pacienteRepository.buscarPorDni(dniPaciente);
        ValidadorDatos.validarFecha(fecha, "fecha de atención");
        ValidadorDatos.validarTextoNoVacio(diagnostico, "diagnóstico");
        ValidadorDatos.validarTextoNoVacio(tratamiento, "tratamiento");
        historiaClinicaRepository.registrarAtencion(new Atencion(dniPaciente, fecha, diagnostico, tratamiento));
    }

    public List<Atencion> obtenerHistoria(String dniPaciente) throws ErrorPersistenciaException {
        return historiaClinicaRepository.listarPorPaciente(dniPaciente);
    }

    /**
     * PROGRAMACIÓN FUNCIONAL: filtra pacientes por apellido y transforma
     * (map) el resultado en nombres completos, usando streams en vez de
     * un bucle imperativo.
     */
    public List<String> buscarNombresPorApellido(String apellidoParcial) throws ErrorPersistenciaException {
        return pacienteRepository.listarTodos().stream()
                .filter(p -> p.getApellidos().toLowerCase().startsWith(apellidoParcial.toLowerCase()))
                .map(Paciente::getNombreCompleto)
                .collect(Collectors.toList());
    }

    public long contarPacientesRegistrados() throws ErrorPersistenciaException {
        return pacienteRepository.listarTodos().stream()
                .map(Paciente::getDni)
                .count();
    }
}
