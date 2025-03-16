package DAO;

import Clases.Producto;
import Conexion.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO producto (Nombre, Descripcion, Tipo, Precio_U, Stock, Stock_M) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setString(3, producto.getTipo());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setInt(5, producto.getStock());
            stmt.setInt(6, producto.getStockMinimo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (Connection conexion = ConexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("Nombre"),
                        rs.getString("Descripcion"),
                        rs.getString("Tipo"),
                        rs.getDouble("Precio_U"),
                        rs.getInt("Stock"),
                        rs.getInt("Stock_M")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
}
