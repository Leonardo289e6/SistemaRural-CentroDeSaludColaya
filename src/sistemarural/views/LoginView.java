package sistemarural.views;

import java.awt.*;
import javax.swing.*;
import sistemarural.controllers.AuthController;

public class LoginView extends JFrame {

    private final JTextField txtUsuario = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JLabel lblEstado = new JLabel(" ", SwingConstants.CENTER);
    private final JButton btnIngresar = new JButton("Ingresar");
    private final AuthController authController;
    private final Runnable alIngresar;
    private static final int INTENTOS_MAXIMOS_LOGIN = 3;
    private int intentos = 0;
    private final Runnable alAgotarIntentos;

    public LoginView(AuthController authController, Runnable alIngresar,Runnable alAgotarIntentos) {
        super("SistemaRural-PE | Ingreso");
        this.authController = authController;
        this.alIngresar = alIngresar;
        this.alAgotarIntentos = alAgotarIntentos;
        construirUI();
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        intentos++;
        String resultado = authController.iniciarSesion(usuario, password);

        if (authController.haySesionActiva()) {
            dispose();
            alIngresar.run();
            return;
        }

        txtPassword.setText("");
        if (intentos >= INTENTOS_MAXIMOS_LOGIN) {
            JOptionPane.showMessageDialog(this,
                    "Número máximo de intentos alcanzado. Se cerrará el programa.",
                    "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            dispose();
            alAgotarIntentos.run();
        } else {
            lblEstado.setText("<html><div style='text-align:center'>" + resultado
                    + " (intento " + intentos + "/" + INTENTOS_MAXIMOS_LOGIN + ")</div></html>");
            pack();
        }
    }



    private void construirUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 15));
        root.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel titulo = new JLabel("<html><div style='text-align:center'>"
                + "<span style='font-size:18pt'><b>SistemaRural-PE</b></span><br>"
                + "Centro de Salud Rural Santa Rosa</div></html>", SwingConstants.CENTER);
        root.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        form.add(txtUsuario, c);
        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("Contraseña:"), c);
        c.gridx = 1;
        form.add(txtPassword, c);
        root.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel(new GridLayout(2, 1, 0, 8));
        lblEstado.setForeground(new Color(180, 30, 30));
        sur.add(lblEstado);
        sur.add(btnIngresar);
        root.add(sur, BorderLayout.SOUTH);

        setContentPane(root);
        getRootPane().setDefaultButton(btnIngresar); // Enter = ingresar

        btnIngresar.addActionListener(e -> intentarLogin());
        pack();
        setLocationRelativeTo(null);
    }
}
