package DAO;

import Clases.Producto;
import Conexion.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase DAO  para gestionar las operaciones relacionadas con los productos.
 * Permite agregar, obtener, actualizar y eliminar productos en la base de datos.
 */
public class ProductoDAO {

    /**
     * Agrega un nuevo producto a la base de datos.
     *
     * @param producto Producto con los datos a registrar.
     * @return True si el producto se agregó correctamente, False en caso contrario.
     */
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

    /**
     * Obtiene una lista de todos los productos registrados en la base de datos.
     *
     * @return Lista de objetos en Producto.
     */
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

    /**
     * Actualiza los datos de un producto en la base de datos.
     *
     * @param producto Producto con los nuevos datos.
     * @return True si la actualización fue exitosa, False en caso contrario.
     */
    public boolean actualizarProducto(Producto producto) {
        String query = "UPDATE producto SET Nombre = ?, Descripcion = ? , Precio_U = ?, Stock = ?, Stock_M = ?, Tipo = ? WHERE id_producto = ?";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getDescripcion());
            statement.setDouble(3, producto.getPrecio());
            statement.setInt(4, producto.getStock());
            statement.setInt(5, producto.getStockMinimo());
            statement.setString(6, producto.getTipo());
            statement.setInt(7, producto.getIdProducto());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un producto de la base de datos.
     *
     * @param id ID del producto a eliminar.
     * @return True si el producto fue eliminado correctamente, False en caso contrario.
     */
    public boolean eliminarProducto(String id) {
        String proctE = "DELETE FROM producto WHERE id_producto =?";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(proctE)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}