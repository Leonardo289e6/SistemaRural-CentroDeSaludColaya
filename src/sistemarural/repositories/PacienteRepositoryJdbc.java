package sistemarural.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemarural.config.CloudDbConnection;
import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.exceptions.PacienteDuplicadoException;
import sistemarural.exceptions.PersonaNoEncontradaException;
import sistemarural.models.Paciente;

/**
 * PacienteRepositoryJdbc
 * ------------------------
 * Implementación: ejecuta
 * sentencias SQL contra la tabla 'pacientes' usando JDBC y la conexión
 * Singleton de CloudDbConnection. Usa PreparedStatement en todos los
 * casos para evitar inyección SQL.
 */
public class PacienteRepositoryJdbc implements IPacienteRepository {

    private final CloudDbConnection cloudDbConnection;

    public PacienteRepositoryJdbc(CloudDbConnection cloudDbConnection) {
        this.cloudDbConnection = cloudDbConnection;
    }

    @Override
    public void guardar(Paciente paciente) throws ErrorPersistenciaException, PacienteDuplicadoException {
        // INSERT simple: si el DNI ya existe, la BD rechaza la fila (no se sobrescribe).
        String sql = "INSERT INTO pacientes (dni, nombres, apellidos, fecha_nacimiento) "
                + "VALUES (?, ?, ?, ?)";
        try {
            Connection conexion = cloudDbConnection.obtenerConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, paciente.getDni());
                stmt.setString(2, paciente.getNombres());
                stmt.setString(3, paciente.getApellidos());
                stmt.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) { // unique_violation (PostgreSQL)
                throw new PacienteDuplicadoException(paciente.getDni());
            }
            throw new ErrorPersistenciaException(
                    "No se pudo guardar el paciente en la base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public Paciente buscarPorDni(String dni) throws ErrorPersistenciaException, PersonaNoEncontradaException {
        String sql = "SELECT dni, nombres, apellidos, fecha_nacimiento FROM pacientes WHERE dni = ?";
        try {
            Connection conexion = cloudDbConnection.obtenerConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, dni);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new PersonaNoEncontradaException(dni);
                    }
                    return mapearFila(rs);
                }
            }
        } catch (SQLException e) {
            throw new ErrorPersistenciaException(
                    "No se pudo buscar el paciente en la base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paciente> listarTodos() throws ErrorPersistenciaException {
        String sql = "SELECT dni, nombres, apellidos, fecha_nacimiento FROM pacientes ORDER BY apellidos";
        List<Paciente> resultado = new ArrayList<>();
        try {
            Connection conexion = cloudDbConnection.obtenerConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearFila(rs));
                }
            }
        } catch (SQLException e) {
            throw new ErrorPersistenciaException(
                    "No se pudo listar los pacientes: " + e.getMessage(), e);
        }
        return resultado;
    }

    private Paciente mapearFila(ResultSet rs) throws SQLException {
        return new Paciente(
                rs.getString("dni"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getDate("fecha_nacimiento").toString());
    }
}