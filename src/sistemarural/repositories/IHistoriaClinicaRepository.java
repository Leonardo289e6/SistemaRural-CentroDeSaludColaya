package sistemarural.repositories;

import java.util.List;
import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.models.Atencion;

/**
 * IHistoriaClinicaRepository
 * ----------------------------
 * 
 */
public interface IHistoriaClinicaRepository {

    void registrarAtencion(Atencion atencion) throws ErrorPersistenciaException;

    List<Atencion> listarPorPaciente(String dniPaciente) throws ErrorPersistenciaException;
}
