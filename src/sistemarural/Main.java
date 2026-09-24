package sistemarural;

import sistemarural.config.CloudDbConnection;
import sistemarural.controllers.AuthController;
import sistemarural.controllers.HistorialController;
import sistemarural.models.Paciente;
import sistemarural.repositories.HistoriaClinicaRepositoryJdbc;
import sistemarural.repositories.IHistoriaClinicaRepository;
import sistemarural.repositories.IPacienteRepository;
import sistemarural.repositories.IPersonalMedicoRepository;
import sistemarural.repositories.PacienteRepositoryJdbc;
import sistemarural.repositories.PersonalMedicoRepositoryJdbc;
import sistemarural.services.AuthService;
import sistemarural.services.HistorialService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Main
 * -----
 * Punto de entrada. Antes de mostrar cualquier servicio, exige un login
 * válido contra la tabla 'personal_medico'. Solo si el login es exitoso
 * se habilita el menú (registrar pacientes, atenciones, etc.).
 */
public class Main {

    private static final int INTENTOS_MAXIMOS_LOGIN = 3;

    public static void main(String[] args) {
        System.out.println("======================================================");
        System.out.println(" SistemaRural-PE | Centro de Salud Rural Santa Rosa");
        System.out.println("======================================================\n");

        CloudDbConnection cloudDbConnection = CloudDbConnection.obtenerInstancia();
        try {
            cloudDbConnection.obtenerConexion(); // Falla rápido si las credenciales están mal
        } catch (SQLException e) {
            System.out.println("No se pudo conectar a la base de datos: " + e.getMessage());
            System.out.println("Revisa las variables de entorno DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD.");
            return;
        }

        IPacienteRepository pacienteRepository = new PacienteRepositoryJdbc(cloudDbConnection);
        IHistoriaClinicaRepository historiaClinicaRepository = new HistoriaClinicaRepositoryJdbc(cloudDbConnection);
        IPersonalMedicoRepository personalMedicoRepository = new PersonalMedicoRepositoryJdbc(cloudDbConnection);

        HistorialService historialService = new HistorialService(pacienteRepository, historiaClinicaRepository);
        AuthService authService = new AuthService(personalMedicoRepository);

        HistorialController historialController = new HistorialController(historialService);
        AuthController authController = new AuthController(authService);

        Scanner scanner = new Scanner(System.in);

        // ---------- LOGIN OBLIGATORIO ----------
        boolean acceso = false;
        for (int intento = 1; intento <= INTENTOS_MAXIMOS_LOGIN && !acceso; intento++) {
            System.out.println("\n--- Ingreso de personal médico (intento " + intento
                    + "/" + INTENTOS_MAXIMOS_LOGIN + ") ---");
            System.out.print("Usuario: ");
            String usuario = scanner.nextLine().trim();
            System.out.print("Contraseña: ");
            String password = scanner.nextLine().trim();

            String resultado = authController.iniciarSesion(usuario, password);
            System.out.println(resultado);
            acceso = authController.haySesionActiva();
        }

        if (!acceso) {
            System.out.println("\nNúmero máximo de intentos alcanzado. Cerrando el programa.");
            cloudDbConnection.desconectar();
            return;
        }

        // ---------- MENÚ (solo alcanzable con sesión activa) ----------
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Menú | Sesión: " + authController.getSesionActual().getInformacion() + " ---");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Buscar paciente por DNI");
            System.out.println("3. Registrar atención médica");
            System.out.println("4. Ver historia clínica de un paciente");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> {
                    System.out.print("DNI (8 dígitos): ");
                    String dni = scanner.nextLine().trim();
                    System.out.print("Nombres: ");
                    String nombres = scanner.nextLine().trim();
                    System.out.print("Apellidos: ");
                    String apellidos = scanner.nextLine().trim();
                    System.out.print("Fecha de nacimiento (yyyy-MM-dd): ");
                    String fechaNacimiento = scanner.nextLine().trim();
                    System.out.println(historialController.registrarPaciente(
                            new Paciente(dni, nombres, apellidos, fechaNacimiento)));
                }
                case "2" -> {
                    System.out.print("DNI a buscar: ");
                    String dni = scanner.nextLine().trim();
                    System.out.println(historialController.buscarPacientePorDni(dni));
                }
                case "3" -> {
                    System.out.print("DNI del paciente: ");
                    String dni = scanner.nextLine().trim();
                    System.out.print("Fecha de atención (yyyy-MM-dd): ");
                    String fecha = scanner.nextLine().trim();
                    System.out.print("Diagnóstico: ");
                    String diagnostico = scanner.nextLine().trim();
                    System.out.print("Tratamiento: ");
                    String tratamiento = scanner.nextLine().trim();
                    System.out.println(historialController.registrarAtencion(dni, fecha, diagnostico, tratamiento));
                }
                case "4" -> {
                    System.out.print("DNI del paciente: ");
                    String dni = scanner.nextLine().trim();
                    List<String> historia = historialController.verHistoriaClinica(dni);
                    if (historia.isEmpty()) {
                        System.out.println("No hay atenciones registradas para este paciente.");
                    } else {
                        historia.forEach(linea -> System.out.println("  - " + linea));
                    }
                }
                case "0" -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        }

        cloudDbConnection.desconectar();
        System.out.println("\nSesión finalizada. Hasta pronto.");
    }
}
