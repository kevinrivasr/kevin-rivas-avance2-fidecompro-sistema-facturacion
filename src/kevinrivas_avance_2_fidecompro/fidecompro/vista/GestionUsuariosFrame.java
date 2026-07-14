package kevinrivas_avance_2_fidecompro.fidecompro.vista;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.*;
import kevinrivas_avance_2_fidecompro.fidecompro.gestion.Sistema;
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionUsuariosFrame extends JFrame {

    private final Sistema sistema;
    private JTextField txtCedula, txtNombre, txtApellido, txtCorreo;
    private JComboBox<String> comboRol;
    private DefaultTableModel modeloTabla;

    public GestionUsuariosFrame(Sistema sistema) {
        this.sistema = sistema;
        configurarVentana();
        refrescarTabla();
    }

    private void configurarVentana() {
        setTitle("Gestión de Usuarios");
        setSize(680, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCedula = new JTextField();
        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtCorreo = new JTextField();
        comboRol = new JComboBox<>(new String[]{"Administrador", "Cajero"});

        panelFormulario.add(new JLabel("Cédula:")); panelFormulario.add(txtCedula);
        panelFormulario.add(new JLabel("Nombre:")); panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Apellido:")); panelFormulario.add(txtApellido);
        panelFormulario.add(new JLabel("Correo (@fidecompro.com):")); panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Rol:")); panelFormulario.add(comboRol);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnCrear); panelBotones.add(btnActualizar);
        panelBotones.add(btnConsultar); panelBotones.add(btnEliminar); panelBotones.add(btnLimpiar);

        modeloTabla = new DefaultTableModel(new Object[]{"Cédula", "Nombre", "Apellido", "Correo", "Rol", "Permisos"}, 0);
        JTable tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnCrear.addActionListener(e -> crearUsuario());
        btnActualizar.addActionListener(e -> actualizarUsuario());
        btnConsultar.addActionListener(e -> consultarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    // Polimorfismo: según el rol seleccionado se instancia una subclase distinta de Usuario
    private void crearUsuario() {
        try {
            String cedula = txtCedula.getText().trim();
            String contrasenaGenerada = "Fid" + Math.abs(cedula.hashCode() % 1000);

            Usuario usuario;
            if (comboRol.getSelectedItem().equals("Administrador")) {
                usuario = new Administrador(cedula, txtNombre.getText().trim(),
                        txtApellido.getText().trim(), txtCorreo.getText().trim(), contrasenaGenerada);
            } else {
                usuario = new Cajero(cedula, txtNombre.getText().trim(),
                        txtApellido.getText().trim(), txtCorreo.getText().trim(), contrasenaGenerada);
            }

            sistema.gestorUsuarios.crearUsuario(usuario);
            JOptionPane.showMessageDialog(this, "Usuario creado. Contraseña generada: " + contrasenaGenerada);
            limpiarCampos();
            refrescarTabla();
        } catch (RegistroDuplicadoException | DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarUsuario() {
        try {
            String cedula = txtCedula.getText().trim();
            Usuario existente = sistema.gestorUsuarios.buscarPorCedula(cedula);

            if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()
                    || txtCorreo.getText().trim().isEmpty()) {
                throw new DatosInvalidosException("Información inválida");
            }

            Usuario actualizado;
            if (comboRol.getSelectedItem().equals("Administrador")) {
                actualizado = new Administrador(cedula, txtNombre.getText().trim(),
                        txtApellido.getText().trim(), txtCorreo.getText().trim(), existente.getContrasena());
            } else {
                actualizado = new Cajero(cedula, txtNombre.getText().trim(),
                        txtApellido.getText().trim(), txtCorreo.getText().trim(), existente.getContrasena());
            }
            actualizado.setActivo(existente.isActivo());

            sistema.gestorUsuarios.actualizarUsuario(cedula, actualizado);
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente");
            limpiarCampos();
            refrescarTabla();
        } catch (RegistroNoEncontradoException | DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void consultarUsuario() {
        try {
            Usuario u = sistema.gestorUsuarios.buscarPorCedula(txtCedula.getText().trim());
            txtNombre.setText(u.getNombre());
            txtApellido.setText(u.getApellido());
            txtCorreo.setText(u.getCorreo());
            comboRol.setSelectedItem(u.getRol());
        } catch (RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el usuario?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                sistema.gestorUsuarios.eliminarUsuario(txtCedula.getText().trim());
                JOptionPane.showMessageDialog(this, "Usuario eliminado");
                limpiarCampos();
                refrescarTabla();
            }
        } catch (RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtCedula.setText(""); txtNombre.setText(""); txtApellido.setText(""); txtCorreo.setText("");
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (Usuario u : sistema.gestorUsuarios.listarUsuarios()) {
            // Llamada polimórfica: cada subclase responde distinto a obtenerPermisos()
            modeloTabla.addRow(new Object[]{u.getCedula(), u.getNombre(), u.getApellido(),
                    u.getCorreo(), u.getRol(), u.obtenerPermisos()});
        }
    }
}