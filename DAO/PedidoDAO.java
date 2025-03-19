package DAO;

import Clases.Pedido;
import Conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PedidoDAO {
    private ConexionDB conexion = new ConexionDB();

    public boolean agregar(Pedido pedido) {

        Connection con = conexion.getConnection();
        String query = "INSERT INTO pedido (id_pedido, id_venta, Estado, TipoU, Subtotal, Total)CURRENT_DATE);";
//VALUES (?, ?, ?, (SELECT precio FROM inventario WHERE id_producto = ?) * ?,
        try
        {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, pedido.getId_venta());
           /* pst.setInt(2, pedido.getId_producto());
            pst.setInt(3, pedido.getCantidad());
            pst.setInt(4, pedido.getId_producto());
            pst.setInt(5, pedido.getCantidad());*/
            pst.executeUpdate();


        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;

        }

        return true;
    }


    public boolean actualizar(Pedido pedido){

        Connection con = conexion.getConnection();
        String query = " UPDATE ordenes SET total = (SELECT SUM(precioT) FROM pedido WHERE id_orden = ?) WHERE id_orden = ?;";
        try
        {
            PreparedStatement pst = con.prepareStatement(query);
           /* pst.setInt(1, pedido.getId_orden());
            pst.setInt(2, pedido.getId_orden());*/
            pst.executeUpdate();



        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;

        }

        return true;

    }

    /*public boolean restar(int id_pedido, int id_orden){

        Connection con = conexion.getConnection();
        String query = " UPDATE ordenes SET total = total-(SELECT precioT FROM pedido WHERE id_pedido = ?) WHERE id_orden = ?;";
        try
        {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id_pedido);
            pst.setInt(2, id_orden);


            pst.executeUpdate();




        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;

        }

        return false;
    }*/

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
