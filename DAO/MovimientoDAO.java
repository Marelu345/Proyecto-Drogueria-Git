package DAO;

import Clases.Movimiento;
import Conexion.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {
    public List<Movimiento> obtenerMovimientos() {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimiento";
        try (Connection conexion = ConexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                movimientos.add(new Movimiento(
                        rs.getInt("id_movimiento"),
                        rs.getString("Tipo"),
                        rs.getString("Categoria"),
                        rs.getDouble("Monto"),
                        rs.getTimestamp("fecha")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movimientos;
    }

    public static boolean actualizarMovimiento(Movimiento movimiento)   {
        String sql = "UPDATE movimiento SET Tipo=?, Categoria=?,  Monto=?, Fecha=? WHERE id_movimiento=?";
        try (Connection conexion = ConexionDB.getConnection();

             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, movimiento.getTipo());
            statement.setString(2, movimiento.getCategoria());
            statement.setDouble(3, movimiento.getMonto());
            statement.setTimestamp(4, movimiento.getFecha());
            statement.setInt(5, movimiento.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean eliminarMovimiento(int id) {
        String sql = "DELETE FROM movimiento WHERE id_movimiento=?";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
