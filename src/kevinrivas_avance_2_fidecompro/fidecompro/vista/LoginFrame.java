package kevinrivas_avance_2_fidecompro.fidecompro.vista;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.Usuario;
import kevinrivas_avance_2_fidecompro.fidecompro.gestion.Sistema;
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.CredencialesInvalidasException;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final Sistema sistema;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;

    public LoginFrame(Sistema sistema) {
        this.sistema = sistema;
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Fidecompro - Inicio de Sesión");
        setSize(380, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Sistema Fidecompro", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Correo:"), gbc);
        txtCorreo = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        txtContrasena = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtContrasena, gbc);

        JButton btnIngresar = new JButton("Iniciar sesión");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnIngresar, gbc);

        btnIngresar.addActionListener(e -> iniciarSesion());

        add(panel);
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos obligatorios",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = sistema.gestorUsuarios.autenticar(correo, contrasena);
            JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario.getNombre());
            new MenuPrincipalFrame(usuario, sistema).setVisible(true);
            dispose();
        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}