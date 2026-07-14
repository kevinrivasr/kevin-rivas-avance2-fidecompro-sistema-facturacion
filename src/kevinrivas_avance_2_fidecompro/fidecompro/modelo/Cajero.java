package kevinrivas_avance_2_fidecompro.fidecompro.modelo;

/**
 *
 * @author Kevin Rivas
 */
public class Cajero extends Usuario {
    public Cajero(String cedula, String nombre, String apellido, String correo, String contrasena) {
        super(cedula, nombre, apellido, correo, contrasena);
    }

    @Override
    public String obtenerPermisos() {
        return "Acceso limitado: clientes y facturación.";
    }

    @Override
    public String getRol() { return "Cajero"; }

    @Override
    public String obtenerTipo() { return "Cajero"; }
}