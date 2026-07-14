package kevinrivas_avance_2_fidecompro.fidecompro.gestion;

/**
 *
 * @author Kevin Rivas
 */
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.Usuario;
import kevinrivas_avance_2_fidecompro.fidecompro.excepciones.*;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {
    private List<Usuario> usuarios = new ArrayList<>();

    public void crearUsuario(Usuario usuario) throws RegistroDuplicadoException, DatosInvalidosException {
        if (usuario.getCedula() == null || usuario.getCedula().isEmpty()
                || usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new DatosInvalidosException("Debe completar todos los campos obligatorios");
        }
        if (!usuario.getCorreo().matches("^[\\w.-]+@fidecompro\\.com$")) {
            throw new DatosInvalidosException("Formato de correo inválido");
        }
        for (Usuario u : usuarios) {
            if (u.getCedula().equals(usuario.getCedula())) {
                throw new RegistroDuplicadoException("El usuario ya existe");
            }
        }
        usuarios.add(usuario);
    }

    public Usuario buscarPorCedula(String cedula) throws RegistroNoEncontradoException {
        for (Usuario u : usuarios) if (u.getCedula().equals(cedula)) return u;
        throw new RegistroNoEncontradoException("No existe el usuario");
    }

    public void eliminarUsuario(String cedula) throws RegistroNoEncontradoException {
        usuarios.remove(buscarPorCedula(cedula));
    }
    
    public void actualizarUsuario(String cedula, Usuario actualizado) throws RegistroNoEncontradoException {
        Usuario existente = buscarPorCedula(cedula);
        int index = usuarios.indexOf(existente);
        usuarios.set(index, actualizado);
    }

    public List<Usuario> listarUsuarios() { return usuarios; }

    public Usuario autenticar(String correo, String contrasena) throws CredencialesInvalidasException {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo)) {
                if (!u.isActivo()) throw new CredencialesInvalidasException("Usuario inactivo. Contacte al administrador");
                if (!u.validarContrasena(contrasena)) throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
                return u;
            }
        }
        throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
    }
}