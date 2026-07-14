package kevinrivas_avance_2_fidecompro.fidecompro.vista;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.Cliente;
import kevinrivas_avance_2_fidecompro.fidecompro.gestion.Sistema;
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionClientesFrame extends JFrame {

    private final Sistema sistema;
    private JTextField txtCedula, txtNombre, txtApellido, txtTelefono, txtCorreo, txtDireccion;
    private DefaultTableModel modeloTabla;

    public GestionClientesFrame(Sistema sistema) {
        this.sistema = sistema;
        configurarVentana();
        refrescarTabla();
    }

    private void configurarVentana() {
        setTitle("Gestión de Clientes");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCedula = new JTextField();
        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();

        panelFormulario.add(new JLabel("Cédula:")); panelFormulario.add(txtCedula);
        panelFormulario.add(new JLabel("Nombre:")); panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Apellido:")); panelFormulario.add(txtApellido);
        panelFormulario.add(new JLabel("Teléfono:")); panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Correo:")); panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Dirección:")); panelFormulario.add(txtDireccion);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnCrear); panelBotones.add(btnActualizar);
        panelBotones.add(btnConsultar); panelBotones.add(btnEliminar); panelBotones.add(btnLimpiar);

        modeloTabla = new DefaultTableModel(new Object[]{"Cédula", "Nombre", "Apellido", "Teléfono", "Correo", "Dirección"}, 0);
        JTable tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnCrear.addActionListener(e -> crearCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnConsultar.addActionListener(e -> consultarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void crearCliente() {
        try {
            Cliente cliente = new Cliente(txtCedula.getText().trim(), txtNombre.getText().trim(),
                    txtApellido.getText().trim(), txtTelefono.getText().trim(),
                    txtCorreo.getText().trim(), txtDireccion.getText().trim());
            sistema.gestorClientes.crearCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente");
            limpiarCampos();
            refrescarTabla();
        } catch (RegistroDuplicadoException | DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCliente() {
        try {
            Cliente cliente = sistema.gestorClientes.buscarPorCedula(txtCedula.getText().trim());
            if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()) {
                throw new DatosInvalidosException("Información inválida");
            }
            cliente.setNombre(txtNombre.getText().trim());
            cliente.setApellido(txtApellido.getText().trim());
            cliente.setTelefono(txtTelefono.getText().trim());
            cliente.setCorreo(txtCorreo.getText().trim());
            cliente.setDireccion(txtDireccion.getText().trim());
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente");
            limpiarCampos();
            refrescarTabla();
        } catch (RegistroNoEncontradoException | DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarCliente() {
        try {
            Cliente cliente = sistema.gestorClientes.buscarPorCedula(txtCedula.getText().trim());
            txtNombre.setText(cliente.getNombre());
            txtApellido.setText(cliente.getApellido());
            txtTelefono.setText(cliente.getTelefono());
            txtCorreo.setText(cliente.getCorreo());
            txtDireccion.setText(cliente.getDireccion());
        } catch (RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el cliente?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                sistema.gestorClientes.eliminarCliente(txtCedula.getText().trim());
                JOptionPane.showMessageDialog(this, "Cliente eliminado");
                limpiarCampos();
                refrescarTabla();
            }
        } catch (RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtCedula.setText(""); txtNombre.setText(""); txtApellido.setText("");
        txtTelefono.setText(""); txtCorreo.setText(""); txtDireccion.setText("");
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente c : sistema.gestorClientes.listarClientes()) {
            modeloTabla.addRow(new Object[]{c.getCedula(), c.getNombre(), c.getApellido(),
                    c.getTelefono(), c.getCorreo(), c.getDireccion()});
        }
    }
}