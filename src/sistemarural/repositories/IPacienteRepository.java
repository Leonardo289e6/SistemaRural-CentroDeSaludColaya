package sistemarural.repositories;

import java.util.List;
import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.exceptions.PacienteDuplicadoException;
import sistemarural.exceptions.PersonaNoEncontradaException;
import sistemarural.models.Paciente;

/**
 * IPacienteRepository
 * ---------------------
 * Contrato de persistencia para la tabla 'pacientes'. Cualquier
 * implementación (JDBC, otra base de datos, memoria para pruebas) debe
 * cumplir este contrato sin afectar a las capas superiores.
 */
public interface IPacienteRepository {

    void guardar(Paciente paciente) throws ErrorPersistenciaException, PacienteDuplicadoException;

    Paciente buscarPorDni(String dni) throws ErrorPersistenciaException, PersonaNoEncontradaException;

    List<Paciente> listarTodos() throws ErrorPersistenciaException;
}