package sistemarural.views;

import java.awt.*;
import javax.swing.*;
import sistemarural.controllers.AuthController;
import sistemarural.controllers.HistorialController;

public class DashboardView extends JFrame {

    private final HistorialController historial;
    private final AuthController auth;
    private final Runnable alSalir;

    public DashboardView(HistorialController historial, AuthController auth, Runnable alSalir) {
        super("SistemaRural-PE | Panel principal");
        this.historial = historial;
        this.auth = auth;
        this.alSalir = alSalir;
        construirUI();
    }

    private void construirUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Cabecera: sesión + salir
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.add(new JLabel("Sesión: " + auth.getSesionActual().getInformacion()), BorderLayout.WEST);
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> { dispose(); alSalir.run(); });
        cabecera.add(btnSalir, BorderLayout.EAST);
        root.add(cabecera, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Registrar paciente", new JPanel());
        tabs.addTab("Buscar paciente", new JPanel());
        tabs.addTab("Registrar atención", new JPanel());
        tabs.addTab("Historia clínica", new JPanel());
        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);
        setSize(720, 480);
        setLocationRelativeTo(null);
    }
}

