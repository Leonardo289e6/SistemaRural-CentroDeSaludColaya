package sistemarural.views;

import java.awt.*;
import javax.swing.*;
import sistemarural.controllers.AuthController;
import sistemarural.controllers.HistorialController;
import sistemarural.models.Paciente;

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
        tabs.addTab("Registrar paciente", panelRegistrarPaciente());
        tabs.addTab("Buscar paciente", new JPanel());
        tabs.addTab("Registrar atención", new JPanel());
        tabs.addTab("Historia clínica", new JPanel());
        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);
        setSize(720, 480);
        setLocationRelativeTo(null);
    } 
        // ---------- 1. Registrar paciente ----------
    private JPanel panelRegistrarPaciente() {
        JTextField dni = new JTextField(20);
        JTextField nombres = new JTextField(20);
        JTextField apellidos = new JTextField(20);
        JTextField nacimiento = new JTextField(20);
        JButton btn = new JButton("Registrar");

        btn.addActionListener(e -> {
            String r = historial.registrarPaciente(new Paciente(
                    dni.getText().trim(), nombres.getText().trim(),
                    apellidos.getText().trim(), nacimiento.getText().trim()));
            mostrar(r);
            dni.setText(""); nombres.setText(""); apellidos.setText(""); nacimiento.setText("");
        });

        return formulario(btn,
                "DNI (8 dígitos):", dni, "Nombres:", nombres,
                "Apellidos:", apellidos, "Fecha de nacimiento (yyyy-MM-dd):", nacimiento);
    }

    // ---------- Utilidades de UI ----------
    /** Formulario genérico: pares (etiqueta, campo) + botón. */
    private JPanel formulario(JButton boton, Object... elementos) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;
        for (int i = 0; i < elementos.length; i += 2) {
            c.gridx = 0; c.gridy = fila; c.weightx = 0;
            form.add(new JLabel((String) elementos[i]), c);
            c.gridx = 1; c.weightx = 1;
            form.add((JTextField) elementos[i + 1], c);
            fila++;
        }
        c.gridx = 1; c.gridy = fila; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        form.add(boton, c);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(form, BorderLayout.NORTH);
        return contenedor;
    }

    private void mostrar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }
}

