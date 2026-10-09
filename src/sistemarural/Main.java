package sistemarural;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import sistemarural.config.CloudDbConnection;
import sistemarural.controllers.AuthController;
import sistemarural.controllers.HistorialController;
import sistemarural.repositories.HistoriaClinicaRepositoryJdbc;
import sistemarural.repositories.IHistoriaClinicaRepository;
import sistemarural.repositories.IPacienteRepository;
import sistemarural.repositories.IPersonalMedicoRepository;
import sistemarural.repositories.PacienteRepositoryJdbc;
import sistemarural.repositories.PersonalMedicoRepositoryJdbc;
import sistemarural.services.AuthService;
import sistemarural.services.HistorialService;
import sistemarural.views.DashboardView;
import sistemarural.views.LoginView;

/**
 * Main (versión gráfica). Conecta a la BD, arma las dependencias y abre
 * LoginView; solo si el login es válido se abre DashboardView.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorada) { }

        CloudDbConnection cloudDbConnection = CloudDbConnection.obtenerInstancia();
        try {
            cloudDbConnection.obtenerConexion(); // Falla rápido si las credenciales están mal
        } catch (SQLException | IllegalStateException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a la base de datos: " + e.getMessage()
                    + "\nRevisa las variables de entorno DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            return;
        }

        IPacienteRepository pacienteRepository = new PacienteRepositoryJdbc(cloudDbConnection);
        IHistoriaClinicaRepository historiaClinicaRepository = new HistoriaClinicaRepositoryJdbc(cloudDbConnection);
        IPersonalMedicoRepository personalMedicoRepository = new PersonalMedicoRepositoryJdbc(cloudDbConnection);

        HistorialService historialService = new HistorialService(pacienteRepository, historiaClinicaRepository);
        AuthService authService = new AuthService(personalMedicoRepository);

        HistorialController historialController = new HistorialController(historialService);
        AuthController authController = new AuthController(authService);

        Runnable cerrarTodo = () -> {
            cloudDbConnection.desconectar();
            System.exit(0);
        };

        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView(
                    authController,
                    () -> new DashboardView(historialController, authController, cerrarTodo).setVisible(true),
                    cerrarTodo);
            login.setVisible(true);
        });
    }
}
