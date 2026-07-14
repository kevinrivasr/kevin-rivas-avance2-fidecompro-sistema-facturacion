package kevinrivas_avance_2_fidecompro.fidecompro.gestion;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.Factura;
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.*;
import java.util.ArrayList;
import java.util.List;

public class GestorFacturas {
    private List<Factura> facturas = new ArrayList<>();

    public void registrarFactura(Factura factura) throws StockInsuficienteException {
        factura.generarFactura();
        facturas.add(factura);
    }

    public Factura buscarPorNumero(String numero) throws RegistroNoEncontradoException {
        for (Factura f : facturas) if (f.getNumeroFactura().equals(numero)) return f;
        throw new RegistroNoEncontradoException("No existe la factura");
    }

    public List<Factura> listarFacturas() { return facturas; }
}