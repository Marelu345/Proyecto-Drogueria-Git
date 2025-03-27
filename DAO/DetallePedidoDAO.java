package DAO;

import Clases.DetallePedido;
import Conexion.ConexionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetallePedidoDAO {
    private ConexionDB conexion = new ConexionDB();


    public boolean AgregarDetallePedido(DetallePedido detalles) {
        Connection con = conexion.getConnection();
        String sql = "INSERT INTO detalle_pedido (id_pedido, id_producto, Cantidad, Subtotal) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, detalles.getId_pedido());
                ps.setInt(2, detalles.getId_producto());
                ps.setInt(3, detalles.getCantidad());
                ps.setDouble(4, Double.valueOf(detalles.getSubtotal()));

        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

