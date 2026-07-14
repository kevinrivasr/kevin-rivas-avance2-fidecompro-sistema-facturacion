package kevinrivas_avance_2_fidecompro.fidecompro.modelo;

/**
 *
 * @author Kevin Rivas
 */
public abstract class Usuario extends Persona {
    protected String contrasena;
    protected boolean activo;

    public Usuario(String cedula, String nombre, String apellido, String correo, String contrasena) {
        super(cedula, nombre, apellido, correo);
        this.contrasena = contrasena;
        this.activo = true;
    }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public boolean validarContrasena(String intento) {
        return this.contrasena != null && this.contrasena.equals(intento);
    }

    // Métodos abstractos
    public abstract String obtenerPermisos();
    public abstract String getRol();
}