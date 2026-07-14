package kevinrivas_avance_2_fidecompro;

/**
 *
 * @author Kevin Rivas
 * Enlace al github https://github.com/kevinrivasr/kevin-rivas-avance2-fidecompro-sistema-facturacion
 */
import kevinrivas_avance_2_fidecompro.fidecompro.gestion.Sistema;
import kevinrivas_avance_2_fidecompro.fidecompro.modelo.*;
import kevinrivas_avance_2_fidecompro.fidecompro.vista.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        cargarDatosIniciales(sistema);

        SwingUtilities.invokeLater(() -> new LoginFrame(sistema).setVisible(true));
    }

    private static void cargarDatosIniciales(Sistema sistema) {
        try {
            sistema.gestorUsuarios.crearUsuario(
                    new Administrador("101110111", "Kevin", "Rivas", "kevin@fidecompro.com", "admin123"));
            sistema.gestorUsuarios.crearUsuario(
                    new Cajero("202220222", "Maria", "Solano", "maria@fidecompro.com", "cajero123"));

            Categoria electronica = sistema.gestorProductos.registrarCategoria("Electrónica", "Dispositivos electrónicos");
            Categoria oficina = sistema.gestorProductos.registrarCategoria("Oficina", "Artículos de oficina");

            sistema.gestorProductos.registrarProducto(
                    new Producto("Mouse inalámbrico", "Mouse USB", 8500, 50, 10, electronica, "COD001"));
            sistema.gestorProductos.registrarProducto(
                    new Producto("Resma de papel", "Papel carta 500 hojas", 3200, 100, 20, oficina, "COD002"));

            sistema.gestorClientes.crearCliente(
                    new Cliente("303330333", "Carlos", "Jiménez", "8888-8888", "carlos@correo.com", "San José"));
        } catch (Exception ex) {
            System.out.println("Error al cargar datos iniciales: " + ex.getMessage());
        }
    }
}
