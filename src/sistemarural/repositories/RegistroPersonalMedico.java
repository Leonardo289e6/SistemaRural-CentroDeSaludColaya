package sistemarural.repositories;

/**
 * RegistroPersonalMedico
 * -------------------------
 * DTO (record de Java) que transporta la fila cruda de 'personal_medico',
 * incluyendo el hash de la contraseña. Se usa solo entre el repositorio
 * y AuthService: nunca debe exponerse hacia la UI ni hacia otras capas,
 * para no filtrar el hash de la contraseña fuera de donde se necesita.
 */
public record RegistroPersonalMedico(long id, String usuario, String passwordHash,
                                      String nombres, String rol) {
}
