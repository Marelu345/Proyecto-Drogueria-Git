package GUI;

import Conexion.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionVentasDAO {
    private ConexionDB conexionBD = new ConexionDB();

    public List<GestionVentas> obtenerVenta()
    {
        List<GestionVentas> ventas = new ArrayList<>();
        Connection con = conexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pedido");

            while (rs.next()) {
                GestionVentas pedidos1 = new GestionVentas(rs.getInt("id_venta"), rs.getInt("id_pedido"),rs.getInt("id_producto"),rs.getInt("cantidad"), rs.getString("tipo"), rs.getInt("precioU"), rs.getString("fecha"));
                ventas.add(pedidos1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventas;
    }

    public boolean ingresarGestionVentas(GestionVentas venta) {
        String consulta = "INSERT INTO venta (id_pedido, id_producto, cantidad, tipo, PrecioU, Fecha) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = conexionBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(consulta)) {
            // Asignar los valores a la consulta
            stmt.setInt(1, venta.getId_pedido());       // ID del Pedido
            stmt.setInt(2, venta.getId_producto());    // ID del Producto
            stmt.setInt(3, venta.getCantidad());      // Cantidad
            stmt.setString(4, venta.getTipo());       // Tipo o Presentación
            stmt.setInt(5, venta.getPrecioU()); // Precio Unitario
            stmt.setTimestamp(6, Timestamp.valueOf(venta.getFecha())); // Fecha en formato Timestamp

            int filasInsertadas = stmt.executeUpdate(); // Ejecutar la consulta
            return filasInsertadas > 0; // Retorna true si se insertó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar la venta: " + e.getMessage());
            return false;
        }
    }



}
