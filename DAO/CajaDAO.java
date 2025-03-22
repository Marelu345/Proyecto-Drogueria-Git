package DAO;

import Conexion.ConexionDB;

import java.sql.*;

public class CajaDAO {

    public Object[] obtenerSaldo() {
        String sql = "SELECT Concepto,Saldo_actual FROM caja WHERE id_caja = 1"  ;
        Object[] datos = {"Desconocido", 0.0};

        try (Connection conexion = ConexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                datos[0] = rs.getString("Concepto");
                datos[1] = rs.getDouble("Saldo_actual");
            } else {
                inicializarCaja();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    private void inicializarCaja() {
        String sql = "INSERT INTO caja (id_caja,Concepto, Saldo_actual) VALUES (1, 0.00)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean actualizarSaldoAutomatico(double monto, String tipo) {
        String sql = "UPDATE caja SET Saldo_actual = Saldo_actual + ? WHERE id_caja = 1";
        if (tipo.equals("Egreso")) {
            sql = "UPDATE caja SET Saldo_actual = Saldo_actual - ? WHERE id_caja = 1";
        }

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setDouble(1, monto);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

