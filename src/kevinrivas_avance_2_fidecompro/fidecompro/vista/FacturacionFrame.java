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

public class FacturacionFrame extends JFrame {

    private final Usuario usuarioActual;
    private final Sistema sistema;
    private JTextField txtCedulaCliente, txtCodigoProducto, txtCantidad;
    private JLabel lblCliente, lblSubtotal, lblImpuesto, lblTotal;
    private DefaultTableModel modeloTabla;
    private Factura facturaActual;

    public FacturacionFrame(Usuario usuarioActual, Sistema sistema) {
        this.usuarioActual = usuarioActual;
        this.sistema = sistema;
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Facturación");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new GridLayout(2, 3, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCedulaCliente = new JTextField();
        JButton btnBuscarCliente = new JButton("Buscar cliente");
        lblCliente = new JLabel("Cliente: (ninguno)");

        panelSuperior.add(new JLabel("Cédula del cliente:"));
        panelSuperior.add(txtCedulaCliente);
        panelSuperior.add(btnBuscarCliente);
        panelSuperior.add(lblCliente);
        panelSuperior.add(new JLabel(""));
        panelSuperior.add(new JLabel(""));

        JPanel panelLinea = new JPanel(new FlowLayout());
        txtCodigoProducto = new JTextField(10);
        txtCantidad = new JTextField(5);
        JButton btnAgregarLinea = new JButton("Agregar producto");
        panelLinea.add(new JLabel("Código producto:")); panelLinea.add(txtCodigoProducto);
        panelLinea.add(new JLabel("Cantidad:")); panelLinea.add(txtCantidad);
        panelLinea.add(btnAgregarLinea);

        modeloTabla = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio unitario", "Subtotal"}, 0);
        JTable tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelTotales = new JPanel(new GridLayout(3, 1));
        lblSubtotal = new JLabel("Subtotal: ₡0.00");
        lblImpuesto = new JLabel("IVA (13%): ₡0.00");
        lblTotal = new JLabel("Total: ₡0.00");
        panelTotales.add(lblSubtotal); panelTotales.add(lblImpuesto); panelTotales.add(lblTotal);

        JButton btnConfirmar = new JButton("Confirmar factura");
        JButton btnNuevaFactura = new JButton("Nueva factura");

        JPanel panelAcciones = new JPanel(new FlowLayout());
        panelAcciones.add(btnConfirmar);
        panelAcciones.add(btnNuevaFactura);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelTotales, BorderLayout.WEST);
        panelInferior.add(panelAcciones, BorderLayout.EAST);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelLinea, BorderLayout.NORTH);
        panelCentro.add(scroll, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnBuscarCliente.addActionListener(e -> buscarCliente());
        btnAgregarLinea.addActionListener(e -> agregarLinea());
        btnConfirmar.addActionListener(e -> confirmarFactura());
        btnNuevaFactura.addActionListener(e -> nuevaFactura());
    }

    private void buscarCliente() {
        try {
            Cliente cliente = sistema.gestorClientes.buscarPorCedula(txtCedulaCliente.getText().trim());
            lblCliente.setText("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
            facturaActual = new Factura(cliente, usuarioActual);
        } catch (RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarLinea() {
        if (facturaActual == null) {
            JOptionPane.showMessageDialog(this, "Primero debe seleccionar un cliente", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Producto producto = sistema.gestorProductos.buscarPorCodigo(txtCodigoProducto.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            facturaActual.agregarLinea(producto, cantidad);
            actualizarTabla();
            txtCodigoProducto.setText("");
            txtCantidad.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RegistroNoEncontradoException | StockInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (LineaFactura l : facturaActual.getLineas()) {
            modeloTabla.addRow(new Object[]{l.getProducto().getNombre(), l.getCantidad(),
                    l.getPrecioUnitario(), l.calcularSubtotalLinea()});
        }
        lblSubtotal.setText(String.format("Subtotal: ₡%.2f", facturaActual.calcularSubtotal()));
        lblImpuesto.setText(String.format("IVA (13%%): ₡%.2f", facturaActual.calcularImpuesto()));
        lblTotal.setText(String.format("Total: ₡%.2f", facturaActual.calcularTotal()));
    }

    private void confirmarFactura() {
        if (facturaActual == null || facturaActual.getLineas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            sistema.gestorFacturas.registrarFactura(facturaActual);
            JOptionPane.showMessageDialog(this, "Factura " + facturaActual.getNumeroFactura()
                    + " generada correctamente.\nTotal: ₡" + String.format("%.2f", facturaActual.getTotal()));
            nuevaFactura();
        } catch (StockInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nuevaFactura() {
        facturaActual = null;
        txtCedulaCliente.setText("");
        lblCliente.setText("Cliente: (ninguno)");
        modeloTabla.setRowCount(0);
        lblSubtotal.setText("Subtotal: ₡0.00");
        lblImpuesto.setText("IVA (13%): ₡0.00");
        lblTotal.setText("Total: ₡0.00");
    }
}