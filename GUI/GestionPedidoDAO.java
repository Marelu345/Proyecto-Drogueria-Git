package GUI;

import Conexion.ConexionDB;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionPedidoDAO {
    private ConexionDB conexionBD = new ConexionDB();

    public List<GestionPedido> ObtenerPedido()
    {
        List<GestionPedido> pedidos = new ArrayList<>();
        Connection con = conexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pedido");

            while (rs.next()) {
                GestionPedido pedidos1 = new GestionPedido(rs.getInt("id_pedido"), rs.getInt("id_cliente"), rs.getString("Estado"), rs.getInt("Subtotal"), rs.getInt("total"));
                pedidos.add(pedidos1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
    public boolean cambiarEstado(int id_pedido,String estadoActual) {
        if ("Entregado".equals(estadoActual)){
            return false;
        }

        Connection connection = ConexionDB.getConnection();

        /**
         * Pues esta consulta SQL lo que hace es cambiar el estado del pedido según su estado actual:
         * - Si el pedido está en "En preparación", lo cambia a "Enviado".
         * - Si el pedido ya está en "Enviado", lo cambia a "Entregado".
         *
         * Esto sirve para que el estado del pedido vaya avanzando según el proceso normal,
         * pero sin permitir saltos o cambios inesperados.
         */
        String sql = "UPDATE pedido SET Estado ="+
                "CASE WHEN Estado = 'En preparacion' THEN 'Enviado'"+
                "WHEN Estado ='Enviado' THEN 'Entregado' " +
                "ELSE 'Estado' END " +
                "WHERE id_pedido = ?";

        try {
            /**
             * Aquí lo que se hace es preparar la consulta para actualizar el estado del pedido,
             * pero se usa un PreparedStatement porque así se evitan problemas con los datos
             * y también ayuda a que la consulta sea más segura.
             * Básicamente, se le pasa el ID del pedido para que solo cambie ese en específico.
             */
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, id_pedido);

            /**
             * Aquí se ejecuta la actualización en la base de datos y se revisa cuántas filas fueron afectadas.
             * Si al menos una fila cambió, significa que el estado del pedido sí se actualizó, entonces devuelve true.
             * Si no cambia nada, pues devuelve false.
             */
            int filasActualizadas = pst.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            /**
             * Si hay un error cuando se intenta actualizar en la base de datos,
             * el error se muestra en la consola para ver qué pasó.
             * Como tal, se devuelve false porque no se pudo cambiar el estado del pedido.
             */
            e.printStackTrace();
            return false;
        }
    }

    public static boolean ingresarPedido(GestionPedido gestionPedido) {
        Connection con = ConexionDB.getConnection();
        String query = "INSERT INTO pedido (id_pedido, id_cliente, Estado,  Subtotal, Total) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            // Asignación de los valores para cada columna
            pst.setInt(1, gestionPedido.getId_pedido());
            pst.setInt(2, gestionPedido.getId_cliente());
            pst.setString(3, gestionPedido.getEstado());
            pst.setInt(4, gestionPedido.getSubtotal());
            pst.setInt(5, gestionPedido.getTotal());

            // Ejecutar la consulta
            int resultado = pst.executeUpdate();

            // Retornar true si se insertaron los datos correctamente
            return resultado > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void datosActualizados(int id_Pedido, int id_Cliente, String estado, int subtotal, int total) {
        // Consulta SQL para actualizar la tabla "pedido"
        String query = "UPDATE pedido SET id_cliente = ?, Estado = ?, Subtotal = ?, Total = ? WHERE id_pedido = ?";
        Connection con = conexionBD.getConnection();

        try {
            // Preparar la consulta con los parámetros necesarios
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id_Cliente);       // ID del cliente
            stmt.setString(2, estado);       // Estado del pedido
            stmt.setInt(3, subtotal);        // Subtotal actualizado
            stmt.setInt(4, total);           // Total actualizado
            stmt.setInt(5, id_Pedido);       // ID del pedido a actualizar

            // Ejecutar la consulta de actualización
            int filas = stmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Pedido actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el pedido.");
            }
        } catch (SQLException e) {
            // Manejo de errores SQL
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el pedido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean eliminar(int id) {

        Connection con = conexionBD.getConnection();

        String query = "DELETE FROM pedido WHERE id_pedido = ?";

        try {



            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;

        }

        return true;

    }



public int obtenerPrecioActual(int idProducto) {
        Connection con = conexionBD.getConnection();
        int precioActual = 0;

        try {
            String query = "SELECT dineroIngresado FROM pedidos WHERE id_Pedidos = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                precioActual = rs.getInt("dineroIngresado");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el precio actual: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return precioActual;
    }

}
