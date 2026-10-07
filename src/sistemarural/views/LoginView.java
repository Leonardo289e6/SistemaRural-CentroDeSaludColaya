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

    public LoginView(AuthController authController, Runnable alIngresar) {
        super("SistemaRural-PE | Ingreso");
        this.authController = authController;
        this.alIngresar = alIngresar;
        construirUI();
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        String resultado = authController.iniciarSesion(usuario, password);

        if (authController.haySesionActiva()) {
            dispose();
            alIngresar.run();
            return;
        }

        txtPassword.setText("");
        lblEstado.setText(resultado);
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
