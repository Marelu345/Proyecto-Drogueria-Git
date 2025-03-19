package DAO;

import Conexion.ConexionDB;

import java.sql.*;

public class CajaDAO {

    public double obtenerSaldo() {
        String sql = "SELECT Saldo_actual FROM caja WHERE id_caja = 1"  ;
        double saldo = 0.0;

        try (Connection conexion = ConexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                saldo = rs.getDouble("Saldo_actual");
            } else {
                inicializarCaja();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saldo;
    }

    private void inicializarCaja() {
        String sql = "INSERT INTO caja (id_caja, Saldo_actual) VALUES (1, 0.00)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
