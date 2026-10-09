package sistemarural.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import sistemarural.config.CloudDbConnection;
import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.models.Atencion;

/**
 * HistoriaClinicaRepositoryJdbc
 * -------------------------------
 * Implementación REAL de IHistoriaClinicaRepository sobre la tabla
 * 'historias_clinicas'. Cada atención registrada queda como una fila
 * enlazada al paciente por 'paciente_dni' (clave foránea).
 */
public class HistoriaClinicaRepositoryJdbc implements IHistoriaClinicaRepository {

    private final CloudDbConnection cloudDbConnection;

    public HistoriaClinicaRepositoryJdbc(CloudDbConnection cloudDbConnection) {
        this.cloudDbConnection = cloudDbConnection;
    }

    @Override
    public void registrarAtencion(Atencion atencion) throws ErrorPersistenciaException {
        String sql = "INSERT INTO historias_clinicas (paciente_dni, fecha_atencion, diagnostico, tratamiento, atendido_por) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conexion = cloudDbConnection.obtenerConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, atencion.getDniPaciente());
                stmt.setDate(2, Date.valueOf(atencion.getFecha()));
                stmt.setString(3, atencion.getDiagnostico());
                stmt.setString(4, atencion.getTratamiento());
                if (atencion.getAtendidoPorId() != null) {
                    stmt.setLong(5, atencion.getAtendidoPorId());
                } else {
                    stmt.setNull(5, Types.INTEGER);
                }
                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        atencion.setId(keys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new ErrorPersistenciaException(
                    "No se pudo registrar la atención en la base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Atencion> listarPorPaciente(String dniPaciente) throws ErrorPersistenciaException {
        String sql = "SELECT h.id, h.paciente_dni, h.fecha_atencion, h.diagnostico, h.tratamiento, "
                + "h.atendido_por, pm.nombres AS atendido_nombres, pm.rol AS atendido_rol "
                + "FROM historias_clinicas h "
                + "LEFT JOIN personal_medico pm ON pm.id = h.atendido_por "
                + "WHERE h.paciente_dni = ? ORDER BY h.fecha_atencion DESC, h.id DESC";
        List<Atencion> resultado = new ArrayList<>();
        try {
            Connection conexion = cloudDbConnection.obtenerConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, dniPaciente);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Atencion atencion = new Atencion(
                                rs.getString("paciente_dni"),
                                rs.getDate("fecha_atencion").toString(),
                                rs.getString("diagnostico"),
                                rs.getString("tratamiento"));
                        atencion.setId(rs.getLong("id"));
                        long atendidoPor = rs.getLong("atendido_por");
                        if (!rs.wasNull()) {
                            atencion.setAtendidoPorId(atendidoPor);
                            atencion.setAtendidoPorNombre(
                                    rs.getString("atendido_nombres") + " (" + rs.getString("atendido_rol") + ")");
                        }
                        resultado.add(atencion);
                    }
                }
            }
        } catch (SQLException e) {
            throw new ErrorPersistenciaException(
                    "No se pudo listar las atenciones del paciente: " + e.getMessage(), e);
        }
        return resultado;
    }
}