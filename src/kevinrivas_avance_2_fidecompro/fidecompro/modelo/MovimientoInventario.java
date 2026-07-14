package kevinrivas_avance_2_fidecompro.fidecompro.modelo;

/**
 *
 * @author Kevin Rivas
 */
import java.util.Date;

public class MovimientoInventario {
    private static int contador = 0;

    private int idMovimiento;
    private Producto producto;
    private String tipoMovimiento; // entrada, salida, ajuste
    private int cantidad;
    private Date fecha;
    private Usuario usuario;
    private String motivo;

    public MovimientoInventario(Producto producto, String tipoMovimiento, int cantidad, Usuario usuario, String motivo) {
        this.idMovimiento = ++contador;
        this.producto = producto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fecha = new Date();
        this.usuario = usuario;
        this.motivo = motivo;
    }

    public int getIdMovimiento() { return idMovimiento; }
    public Producto getProducto() { return producto; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public int getCantidad() { return cantidad; }
    public Date getFecha() { return fecha; }
    public Usuario getUsuario() { return usuario; }
    public String getMotivo() { return motivo; }

    @Override
    public String toString() {
        return tipoMovimiento + " - " + producto.getNombre() + " (" + cantidad + ")";
    }
}