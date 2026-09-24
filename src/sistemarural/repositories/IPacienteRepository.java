package sistemarural.repositories;

import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.exceptions.PersonaNoEncontradaException;
import sistemarural.models.Paciente;

import java.util.List;

/**
 * IPacienteRepository
 * ---------------------
 * Contrato de persistencia para la tabla 'pacientes'. Cualquier
 * implementación (JDBC, otra base de datos, memoria para pruebas) debe
 * cumplir este contrato sin afectar a las capas superiores.
 */
public interface IPacienteRepository {

    void guardar(Paciente paciente) throws ErrorPersistenciaException;

    Paciente buscarPorDni(String dni) throws ErrorPersistenciaException, PersonaNoEncontradaException;

    List<Paciente> listarTodos() throws ErrorPersistenciaException;
}
