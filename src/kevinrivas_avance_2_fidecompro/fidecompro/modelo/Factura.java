package kevinrivas_avance_2_fidecompro.fidecompro.modelo;

/**
 *
 * @author Kevin Rivas
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.StockInsuficienteException;

public class Factura {
    private static int contador = 1000;

    private int idFactura;
    private String numeroFactura;
    private Cliente cliente;
    private Usuario usuario;
    private Date fecha;
    private String estado; // pendiente, pagada, anulada
    private List<LineaFactura> lineas;

    public Factura(Cliente cliente, Usuario usuario) {
        this.idFactura = ++contador;
        this.numeroFactura = "FID-" + idFactura;
        this.cliente = cliente;
        this.usuario = usuario;
        this.fecha = new Date();
        this.lineas = new ArrayList<>();
        this.estado = "pendiente";
    }

    public void agregarLinea(Producto producto, int cantidad) throws StockInsuficienteException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        if (producto.getStockActual() < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        lineas.add(new LineaFactura(producto, cantidad));
    }

    public double calcularSubtotal() {
        double s = 0;
        for (LineaFactura l : lineas) s += l.calcularSubtotalLinea();
        return s;
    }

    public double calcularImpuesto() { return calcularSubtotal() * 0.13; }

    public double calcularTotal() { return calcularSubtotal() + calcularImpuesto(); }

    public void generarFactura() throws StockInsuficienteException {
        for (LineaFactura l : lineas) {
            l.getProducto().ajustarStock(-l.getCantidad());
        }
        estado = "pagada";
    }

    public void anularFactura() throws StockInsuficienteException {
        if (estado.equals("anulada")) return;
        for (LineaFactura l : lineas) {
            l.getProducto().ajustarStock(l.getCantidad());
        }
        estado = "anulada";
    }

    public String getNumeroFactura() { return numeroFactura; }
    public Cliente getCliente() { return cliente; }
    public Usuario getUsuario() { return usuario; }
    public Date getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public List<LineaFactura> getLineas() { return lineas; }
    public double getTotal() { return calcularTotal(); }
}