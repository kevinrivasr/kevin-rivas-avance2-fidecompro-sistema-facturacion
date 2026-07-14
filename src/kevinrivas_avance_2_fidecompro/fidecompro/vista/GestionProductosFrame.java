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

public class GestionProductosFrame extends JFrame {

    private final Sistema sistema;
    private JTextField txtCodigo, txtNombre, txtDescripcion, txtPrecio, txtStock, txtStockMinimo;
    private JComboBox<Categoria> comboCategoria;
    private DefaultTableModel modeloTabla;

    public GestionProductosFrame(Sistema sistema) {
        this.sistema = sistema;
        configurarVentana();
        refrescarTabla();
    }

    private void configurarVentana() {
        setTitle("Gestión de Productos");
        setSize(760, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        txtStockMinimo = new JTextField();
        comboCategoria = new JComboBox<>(sistema.gestorProductos.listarCategorias().toArray(new Categoria[0]));

        panelFormulario.add(new JLabel("Código de barras:")); panelFormulario.add(txtCodigo);
        panelFormulario.add(new JLabel("Nombre:")); panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Descripción:")); panelFormulario.add(txtDescripcion);
        panelFormulario.add(new JLabel("Categoría:")); panelFormulario.add(comboCategoria);
        panelFormulario.add(new JLabel("Precio unitario:")); panelFormulario.add(txtPrecio);
        panelFormulario.add(new JLabel("Stock inicial:")); panelFormulario.add(txtStock);
        panelFormulario.add(new JLabel("Stock mínimo:")); panelFormulario.add(txtStockMinimo);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnDesactivar = new JButton("Desactivar");
        JButton btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnCrear); panelBotones.add(btnActualizar);
        panelBotones.add(btnConsultar); panelBotones.add(btnDesactivar); panelBotones.add(btnLimpiar);

        modeloTabla = new DefaultTableModel(new Object[]{"Código", "Nombre", "Categoría", "Precio", "Stock", "Mínimo", "Activo"}, 0);
        JTable tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnCrear.addActionListener(e -> crearProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnConsultar.addActionListener(e -> consultarProducto());
        btnDesactivar.addActionListener(e -> desactivarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void crearProducto() {
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());
            Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

            Producto producto = new Producto(txtNombre.getText().trim(), txtDescripcion.getText().trim(),
                    precio, stock, stockMinimo, categoria, txtCodigo.getText().trim());
            sistema.gestorProductos.registrarProducto(producto);
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente");
            limpiarCampos();
            refrescarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Información inválida", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RegistroDuplicadoException | DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        try {
            Producto p = sistema.gestorProductos.buscarPorCodigo(txtCodigo.getText().trim());

            if (txtNombre.getText().trim().isEmpty()) {
                throw new DatosInvalidosException("Información inválida");
            }
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());

            p.setNombre(txtNombre.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setPrecioUnitario(precio);
            p.setStockMinimo(stockMinimo);
            p.setCategoria((Categoria) comboCategoria.getSelectedItem());

            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente");
            limpiarCampos();
            refrescarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Información inválida", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RegistroNoEncontradoException | DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void consultarProducto() {
        try {
            Producto p = sistema.gestorProductos.buscarPorCodigo(txtCodigo.getText().trim());
            txtNombre.setText(p.getNombre());
            txtDescripcion.setText(p.getDescripcion());
            txtPrecio.setText(String.valueOf(p.getPrecioUnitario()));
            txtStock.setText(String.valueOf(p.getStockActual()));
            txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
        } catch (RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desactivarProducto() {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea desactivar el producto?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                sistema.gestorProductos.desactivarProducto(txtCodigo.getText().trim());
                JOptionPane.showMessageDialog(this, "Producto desactivado");
                limpiarCampos();
                refrescarTabla();
            }
        } catch (RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText(""); txtNombre.setText(""); txtDescripcion.setText("");
        txtPrecio.setText(""); txtStock.setText(""); txtStockMinimo.setText("");
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : sistema.gestorProductos.listarActivos()) {
            modeloTabla.addRow(new Object[]{p.getCodigoBarras(), p.getNombre(),
                    p.getCategoria().getNombre(), p.getPrecioUnitario(), p.getStockActual(),
                    p.getStockMinimo(), p.isActivo() ? "Sí" : "No"});
        }
    }
}