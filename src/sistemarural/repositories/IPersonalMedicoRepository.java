package sistemarural.repositories;

import sistemarural.exceptions.CredencialesInvalidasException;
import sistemarural.exceptions.ErrorPersistenciaException;

/**
 * IPersonalMedicoRepository
 * ---------------------------
 * Contrato de persistencia para la tabla 'personal_medico', usada
 * exclusivamente para validar el login (autenticación) antes de dar
 * acceso al resto de servicios del sistema.
 */
public interface IPersonalMedicoRepository {

    /**
     * Busca una fila por su usuario. Devuelve un DTO con el hash de
     * contraseña incluido, para que AuthService haga la comparación;
     * el repositorio no decide si la contraseña es correcta.
     */
    RegistroPersonalMedico buscarPorUsuario(String usuario)
            throws ErrorPersistenciaException, CredencialesInvalidasException;
}
