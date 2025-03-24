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

    public boolean registrarMovimiento(Movimiento movimiento) {
        String sql = "INSERT INTO movimiento (tipo, categoria, monto) VALUES (?, ?, ?)";

        try {
            Connection conexion = ConexionDB.getConnection();
            PreparedStatement stmt = conexion.prepareStatement(sql);

            stmt.setString(1, movimiento.getTipo());
            stmt.setString(2, movimiento.getCategoria());
            stmt.setDouble(3, movimiento.getMonto());

            boolean registrado = stmt.executeUpdate() > 0;


            if (registrado) {
                CajaDAO.actualizarSaldoAutomatico(movimiento.getMonto(), movimiento.getTipo());
            }

            return registrado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public static boolean actualizarMovimiento(Movimiento movimiento)   {
        String sql = "UPDATE movimiento SET Tipo=?, Categoria=?, Monto=?, fecha=? WHERE id_movimiento=?";
        try (Connection conexion = ConexionDB.getConnection();

             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, movimiento.getTipo());
            statement.setString(2, movimiento.getCategoria());
            statement.setDouble(3, movimiento.getMonto());
            statement.setTimestamp(4, new Timestamp(movimiento.getFecha().getTime()));

            // Aquí agregamos el ID del movimiento como quinto parámetro
            statement.setInt(5, movimiento.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarMovimiento(int id) {
        String sqlD = "DELETE FROM movimiento WHERE id_movimiento=?";
        String sqlS = "SELECT Monto, Tipo FROM movimiento WHERE id_movimiento=?";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmtD = conexion.prepareStatement(sqlD);
             PreparedStatement stmtS = conexion.prepareStatement(sqlS)){

            stmtS.setInt(1, id);
            ResultSet rs = stmtS.executeQuery();

            if (rs.next()) {
                double monto = rs.getDouble("Monto");
                String tipo = rs.getString("Tipo");

                stmtD.setInt(1, id);
                boolean eliminado = stmtD.executeUpdate() > 0;

                if (eliminado) {
                    if (tipo.equals("Ingreso")) {
                            CajaDAO.actualizarSaldoAutomatico(-monto, "Ingreso");
                    }else if (tipo.equals("Egreso")) {
                        CajaDAO.actualizarSaldoAutomatico(monto, "Ingreso");
                    }
                }
                return eliminado;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;

    }
}
