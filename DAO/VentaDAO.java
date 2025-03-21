package DAO;

import Clases.Venta;
import Conexion.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    public boolean registrarVenta(Venta venta) {
        String sql = "INSERT INTO venta (id_producto, id_cliente, PrecioU, Iva, PrecioT) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, venta.getIdProducto());
            stmt.setInt(2, venta.getIdCliente());
            stmt.setDouble(3, venta.getPrecioUnitario());
            stmt.setDouble(4, venta.getIva());
            stmt.setDouble(5, venta.getPrecioTotal());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Venta> obtenerVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta";

        try (Connection conexion = ConexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ventas.add(new Venta(
                        rs.getInt("id_venta"),
                        rs.getInt("id_producto"),
                        rs.getInt("id_cliente"),
                        rs.getDouble("PrecioU"),
                        rs.getDouble("Iva"),
                        rs.getDouble("PrecioT"),
                        rs.getTimestamp("Fecha")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventas;
    }
}
