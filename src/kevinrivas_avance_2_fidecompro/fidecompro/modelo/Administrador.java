package kevinrivas_avance_2_fidecompro.fidecompro.modelo;

/**
 *
 * @author Kevin Rivas
 */
public class Administrador extends Usuario {

    public Administrador(String cedula, String nombre, String apellido, String correo, String contrasena) {
        super(cedula, nombre, apellido, correo, contrasena);
    }

    @Override
    public String obtenerPermisos() {
        return "Acceso total: usuarios, clientes, productos, inventario y facturación.";
    }

    @Override
    public String getRol() {
        return "Administrador";
    }

    @Override
    public String obtenerTipo() {
        return "Administrador";
    }
}
