package kevinrivas_avance_2_fidecompro.fidecompro.modelo;

/**
 *
 * @author Kevin Rivas
 */
public class LineaFactura {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public LineaFactura(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecioUnitario();
    }

    public double calcularSubtotalLinea() { return precioUnitario * cantidad; }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
}