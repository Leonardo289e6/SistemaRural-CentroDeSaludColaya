package sistemarural.repositories;

import sistemarural.config.CloudDbConnection;
import sistemarural.exceptions.CredencialesInvalidasException;
import sistemarural.exceptions.ErrorPersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PersonalMedicoRepositoryJdbc
 * ------------------------------
 * Implementación REAL de IPersonalMedicoRepository sobre la tabla
 * 'personal_medico'.
 */
public class PersonalMedicoRepositoryJdbc implements IPersonalMedicoRepository {

    private final CloudDbConnection cloudDbConnection;

    public PersonalMedicoRepositoryJdbc(CloudDbConnection cloudDbConnection) {
        this.cloudDbConnection = cloudDbConnection;
    }

    @Override
    public RegistroPersonalMedico buscarPorUsuario(String usuario)
            throws ErrorPersistenciaException, CredencialesInvalidasException {
        String sql = "SELECT id, usuario, password_hash, nombres, rol "
                + "FROM personal_medico WHERE usuario = ?";
        try {
            Connection conexion = cloudDbConnection.obtenerConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, usuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        // No se revela si el usuario existe o no: mismo
                        // mensaje genérico que una contraseña incorrecta.
                        throw new CredencialesInvalidasException();
                    }
                    return new RegistroPersonalMedico(
                            rs.getLong("id"),
                            rs.getString("usuario"),
                            rs.getString("password_hash"),
                            rs.getString("nombres"),
                            rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            throw new ErrorPersistenciaException(
                    "No se pudo validar las credenciales: " + e.getMessage(), e);
        }
    }
}
