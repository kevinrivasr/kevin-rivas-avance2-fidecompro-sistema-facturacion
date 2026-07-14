package kevinrivas_avance_2_fidecompro.fidecompro.vista;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.*;
import kevinrivas_avance_2_fidecompro.fidecompro.gestion.Sistema;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private final Usuario usuarioActual;
    private final Sistema sistema;
    private JLabel lblAlertas;

    public MenuPrincipalFrame(Usuario usuarioActual, Sistema sistema) {
        this.usuarioActual = usuarioActual;
        this.sistema = sistema;
        configurarVentana();
        iniciarHiloAlertaStock();
    }

    private void configurarVentana() {
        setTitle("Fidecompro - Menú Principal (" + usuarioActual.getRol() + ")");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuarioActual.getNombre()
                + " (" + usuarioActual.getRol() + ")");
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblBienvenida);
        panel.add(Box.createVerticalStrut(15));

        JButton btnClientes = new JButton("Gestión de Clientes");
        btnClientes.addActionListener(e -> new GestionClientesFrame(sistema).setVisible(true));
        panel.add(btnClientes);
        panel.add(Box.createVerticalStrut(10));

        JButton btnFacturacion = new JButton("Facturación");
        btnFacturacion.addActionListener(e -> new FacturacionFrame(usuarioActual, sistema).setVisible(true));
        panel.add(btnFacturacion);
        panel.add(Box.createVerticalStrut(10));

        // Solo el Administrador ve estas opciones (uso de polimorfismo con instanceof)
        if (usuarioActual instanceof Administrador) {
            JButton btnUsuarios = new JButton("Gestión de Usuarios");
            btnUsuarios.addActionListener(e -> new GestionUsuariosFrame(sistema).setVisible(true));
            panel.add(btnUsuarios);
            panel.add(Box.createVerticalStrut(10));

            JButton btnProductos = new JButton("Gestión de Productos");
            btnProductos.addActionListener(e -> new GestionProductosFrame(sistema).setVisible(true));
            panel.add(btnProductos);
            panel.add(Box.createVerticalStrut(10));
        }

        lblAlertas = new JLabel("Verificando stock...");
        lblAlertas.setForeground(Color.RED);
        lblAlertas.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblAlertas);

        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.addActionListener(e -> {
            dispose();
            new LoginFrame(sistema).setVisible(true);
        });
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnSalir);

        add(panel);
    }

    // Multihilo, acá revisa cada 5 segundos si hay productos con stock bajo
    private void iniciarHiloAlertaStock() {
        Thread hiloAlerta = new Thread(() -> {
            while (true) {
                long bajos = sistema.gestorProductos.listarStockBajo().size();
                SwingUtilities.invokeLater(() ->
                        lblAlertas.setText(bajos + " producto(s) con stock bajo"));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        hiloAlerta.setDaemon(true);
        hiloAlerta.start();
    }
}