package sistemarural.services;

import sistemarural.exceptions.CredencialesInvalidasException;
import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.models.PersonalSalud;
import sistemarural.repositories.IPersonalMedicoRepository;
import sistemarural.repositories.RegistroPersonalMedico;
import sistemarural.security.GestorSeguridad;

/**
 * AuthService
 * ------------
 * Valida el ingreso (login por consola) de un integrante del personal de salud
 * contra la tabla 'personal_medico' antes de permitir el uso de
 * cualquier otro servicio del sistema (registrar pacientes, atenciones,
 * citas, etc.). Esta es la puerta de acceso obligatoria que pediste.
 *
 * La contraseña nunca se compara ni se guarda en texto plano: se
 * calcula su hash SHA-256 (GestorSeguridad) y se compara contra
 * 'password_hash' en la base de datos, cumpliendo la Ley N.° 29733.
 */
public class AuthService {

    private final IPersonalMedicoRepository personalMedicoRepository;

    public AuthService(IPersonalMedicoRepository personalMedicoRepository) {
        this.personalMedicoRepository = personalMedicoRepository;
    }

    public PersonalSalud login(String usuario, String passwordPlano)
            throws CredencialesInvalidasException, ErrorPersistenciaException {

        RegistroPersonalMedico registro = personalMedicoRepository.buscarPorUsuario(usuario);

        String hashIngresado = GestorSeguridad.generarHash(passwordPlano);
        if (!hashIngresado.equals(registro.passwordHash())) {
            throw new CredencialesInvalidasException();
        }

        return new PersonalSalud(registro.usuario(), registro.nombres(), registro.rol());
    }
}
