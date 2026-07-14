package kevinrivas_avance_2_fidecompro.fidecompro.modelo;

/**
 *
 * @author Kevin Rivas
 */
import java.util.Date;

public class Cliente extends Persona {
    private String telefono;
    private String direccion;
    private Date fechaRegistro;

    public Cliente(String cedula, String nombre, String apellido, String telefono, String correo, String direccion) {
        super(cedula, nombre, apellido, correo);
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaRegistro = new Date();
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Date getFechaRegistro() { return fechaRegistro; }

    @Override
    public String obtenerTipo() { return "Cliente"; }
}