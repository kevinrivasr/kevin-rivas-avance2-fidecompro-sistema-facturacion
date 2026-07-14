package kevinrivas_avance_2_fidecompro.fidecompro.gestion;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.*;
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.*;
import java.util.*;

public class GestorProductos {
    private List<Producto> productos = new ArrayList<>();
    private Map<String, Producto> indicePorCodigo = new HashMap<>();
    private List<Categoria> categorias = new ArrayList<>();
    private int contadorId = 1;
    private int contadorCategoria = 1;

    public Categoria registrarCategoria(String nombre, String descripcion) {
        Categoria categoria = new Categoria(contadorCategoria++, nombre, descripcion);
        categorias.add(categoria);
        return categoria;
    }

    public List<Categoria> listarCategorias() { return categorias; }

    public void registrarProducto(Producto producto) throws RegistroDuplicadoException, DatosInvalidosException {
        if (producto.getCodigoBarras() == null || producto.getCodigoBarras().isEmpty()
                || producto.getNombre() == null || producto.getNombre().isEmpty()
                || producto.getPrecioUnitario() <= 0) {
            throw new DatosInvalidosException("Información inválida");
        }
        if (indicePorCodigo.containsKey(producto.getCodigoBarras())) {
            throw new RegistroDuplicadoException("El producto ya existe");
        }
        producto.setIdProducto(contadorId++);
        productos.add(producto);
        indicePorCodigo.put(producto.getCodigoBarras(), producto);
    }

    public Producto buscarPorCodigo(String codigo) throws RegistroNoEncontradoException {
        Producto p = indicePorCodigo.get(codigo);
        if (p == null) throw new RegistroNoEncontradoException("No existe el producto");
        return p;
    }

    public void desactivarProducto(String codigo) throws RegistroNoEncontradoException {
        buscarPorCodigo(codigo).setActivo(false);
    }

    public List<Producto> listarActivos() {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) if (p.isActivo()) resultado.add(p);
        return resultado;
    }

    public List<Producto> listarStockBajo() {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : listarActivos()) if (p.verificarStockMinimo()) resultado.add(p);
        return resultado;
    }
}