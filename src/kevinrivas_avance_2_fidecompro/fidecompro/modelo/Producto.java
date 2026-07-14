package kevinrivas_avance_2_fidecompro.fidecompro.modelo;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.StockInsuficienteException;

public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int stockActual;
    private int stockMinimo;
    private Categoria categoria;
    private String codigoBarras;
    private boolean activo;

    public Producto(String nombre, String descripcion, double precioUnitario,
                     int stockActual, int stockMinimo, Categoria categoria, String codigoBarras) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.categoria = categoria;
        this.codigoBarras = codigoBarras;
        this.activo = true;
    }

    public void ajustarStock(int cantidad) throws StockInsuficienteException {
        int nuevoStock = stockActual + cantidad;
        if (nuevoStock < 0) {
            throw new StockInsuficienteException("Stock insuficiente para el producto: " + nombre);
        }
        stockActual = nuevoStock;
    }

    public boolean verificarStockMinimo() { return stockActual <= stockMinimo; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public int getStockActual() { return stockActual; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getCodigoBarras() { return codigoBarras; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return nombre + " (₡" + precioUnitario + ") - Stock: " + stockActual;
    }
}