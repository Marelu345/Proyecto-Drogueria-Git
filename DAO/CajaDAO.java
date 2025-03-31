package DAO;

import Conexion.ConexionDB;

import java.sql.*;
/**
 * Clase DAO para gestionar las operaciones relacionadas con la caja.
 * Permite obtener el saldo actual, inicializar la caja y actualizar el saldo automáticamente.
 */
public class CajaDAO {

    /**
     *
     * Obtiene el saldo actual de la caja.
     *
     */
    public Object[] obtenerSaldo() {
        String sql = "SELECT Concepto,Saldo_actual FROM caja WHERE id_caja = 1";
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

    /**
     * Inicializa la caja con un saldo inicial en caso de que no exista (0).
     */
    private void inicializarCaja() {
        String sql = "INSERT INTO caja (id_caja,Concepto, Saldo_actual) VALUES (1, 0.00)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el saldo de la caja de manera automática dependiendo el tipo de operación.
     * Si el tipo es "Ingreso" se suma el monto.
     * Si el tipo es "Egreso" se resta el monto.
     *
     * @param monto Cantidad a modificar en el saldo.
     * @param tipo Tipo de operación: Ingreso o Egreso.
     * @return True si la actualización fue exitosa, false en caso contrario.
     */
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
