package kevinrivas_avance_2_fidecompro.fidecompro.gestion;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.Cliente;
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.*;
import java.util.*;

public class GestorClientes {
    private List<Cliente> clientes = new ArrayList<>();
    private Map<String, Cliente> indicePorCedula = new HashMap<>();

    public void crearCliente(Cliente cliente) throws RegistroDuplicadoException, DatosInvalidosException {
        if (cliente.getCedula() == null || cliente.getCedula().isEmpty()
                || cliente.getNombre() == null || cliente.getNombre().isEmpty()
                || cliente.getApellido() == null || cliente.getApellido().isEmpty()) {
            throw new DatosInvalidosException("Información inválida");
        }
        if (indicePorCedula.containsKey(cliente.getCedula())) {
            throw new RegistroDuplicadoException("El cliente ya existe");
        }
        clientes.add(cliente);
        indicePorCedula.put(cliente.getCedula(), cliente);
    }

    public Cliente buscarPorCedula(String cedula) throws RegistroNoEncontradoException {
        Cliente c = indicePorCedula.get(cedula);
        if (c == null) throw new RegistroNoEncontradoException("No existe el cliente");
        return c;
    }

    public void eliminarCliente(String cedula) throws RegistroNoEncontradoException {
        Cliente c = buscarPorCedula(cedula);
        clientes.remove(c);
        indicePorCedula.remove(cedula);
    }

    public List<Cliente> listarClientes() { return clientes; }
}