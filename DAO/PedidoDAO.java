package DAO;

import Clases.Pedido;
import Conexion.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private ConexionDB conexion = new ConexionDB();

    public boolean agregar(Pedido pedido) {

        Connection con = conexion.getConnection();
        String query = "INSERT INTO pedido (id_pedido, id_venta, Estado, TipoU, Subtotal, Total) VALUES ('En preparación', ?,?,?)";
        try
        {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1,pedido.getId_pedido());
            pst.setInt(2,pedido.getId_venta());
            pst.setString(3,pedido.getEstado());
            pst.setDouble(4,pedido.getSubtotal());
            pst.setDouble(5,pedido.getTotal());
            pst.executeUpdate();


        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;

        }

        return true;
    }
    /**
     * Este metodo obtiene todos los pedidos que están guardados en la base de datos.
     * Básicamente, consulta la tabla "pedido" y mete cada pedido en una lista.
     *
     * @return Una lista con todos los pedidos que hay en la base de datos.
     */
    public List<Pedido> obtenerPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";
        try (Connection conexion = ConexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            /**
             * Recorre los resultados obtenidos de la consulta y los agrega a la lista.
             * Se crea un nuevo objeto Pedido con los valores obtenidos de la base de datos.
             */
            while (rs.next()) {
                pedidos.add(new Pedido(
                        rs.getInt("Id_pedido"),
                        rs.getInt("Id_venta"),
                        rs.getInt("subtotal"),
                        rs.getInt("Total"),
                        rs.getString("Estado"),
                        rs.getString("TipoU")
                ));
            }
        } catch (SQLException e) {
            /**
             * En caso depronto de error al ejecutar la consulta, se imprime el error para saber qué pasó.
             */
            e.printStackTrace();
        }

        /**
         * Devuelve la lista con todos los pedidos obtenidos.
         */
        return pedidos;
    }

    /**
     * Cambia el estado de un pedido dependiendo de su estado actual.
     * Si el pedido ya está en "Entregado", pues no se hace ningún cambio.
     * Esto es útil para evitar actualizar pedidos que ya finalizaron.
     *
     * @param id_pedido El ID del pedido que se quiere actualizar.
     * @param estadoActual El estado actual del pedido.
     * @return true si el estado se actualizó, false si no se pudo cambiar.
     */
    public boolean cambiarEstado(int id_pedido,String estadoActual) {
        if ("Entregado".equals(estadoActual)){
            return false;
        }

        Connection connection = conexion.getConnection();

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
    public String obtenerEstado(int id_pedido) {
        String estado = "";
        Connection con = conexion.getConnection();
        String sql = "SELECT Estado FROM pedido WHERE id_pedido = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id_pedido);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) estado = rs.getString("Estado");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estado;
    }


    public boolean eliminar(int id) {

        Connection con = conexion.getConnection();

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
}
