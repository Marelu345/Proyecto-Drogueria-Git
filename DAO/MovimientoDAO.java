package DAO;

import Clases.Movimiento;
import Conexion.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 * Clase DAO para gestionar las operaciones relacionadas con los movimientos financieros.
 * Permite obtener, registrar, actualizar y eliminar movimientos en la base de datos.
 */
public class MovimientoDAO {

    /**
     * Obtiene la lista de todos los movimientos registrados.
     * @return Lista de objetos Movimiento con la información de cada registro.
     */
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

    /**
     * Registra un nuevo movimiento en la base de datos.
     *
     * @param movimiento Objeto Movimiento con los datos a registrar.
     * @return Es True si el movimiento se registró correctamente, false en caso contrario.
     */
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

    /**
     * Actualiza los datos de un movimiento existente en la base de datos.
     *
     * @param movimiento Objeto Movimiento con la nueva información.
     * @return Es True si el movimiento fue actualizado correctamente, False en caso contrario.
     */
    public static boolean actualizarMovimiento(Movimiento movimiento) {
        String sql = "SELECT Tipo, Monto FROM movimiento WHERE id_movimiento=?";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmtSelect = conexion.prepareStatement(sql)) {

            stmtSelect.setInt(1, movimiento.getId());
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                String tipoAnterior = rs.getString("Tipo");
                double montoAnterior = rs.getDouble("Monto");

                String sqlUpdate = "UPDATE movimiento SET Tipo=?, Categoria=?, Monto=?, fecha=? WHERE id_movimiento=?";
                PreparedStatement stmtUpdate = conexion.prepareStatement(sqlUpdate);
                stmtUpdate.setString(1, movimiento.getTipo());
                stmtUpdate.setString(2, movimiento.getCategoria());
                stmtUpdate.setDouble(3, movimiento.getMonto());
                stmtUpdate.setTimestamp(4, new java.sql.Timestamp(movimiento.getFecha().getTime()));
                stmtUpdate.setInt(5, movimiento.getId());

                boolean actualizado = stmtUpdate.executeUpdate() > 0;

                if (actualizado) {
                    double ajuste = 0;

                    if (tipoAnterior.equals(movimiento.getTipo())) {
                        ajuste = movimiento.getMonto() - montoAnterior;
                    } else {
                        ajuste = tipoAnterior.equals("Ingreso") ? -montoAnterior : montoAnterior;
                        ajuste += movimiento.getTipo().equals("Ingreso") ? movimiento.getMonto() : -movimiento.getMonto();
                    }

                    CajaDAO.actualizarSaldoAutomatico(ajuste, movimiento.getTipo());
                }
                return actualizado;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un movimiento de la base de datos y ajusta el saldo en la caja.
     *
     * @param id Identificador del movimiento a eliminar.
     * @return Es True si el movimiento fue eliminado correctamente, False en caso contrario.
     */
    public boolean eliminarMovimiento(int id) {
        String sqlD = "DELETE FROM movimiento WHERE id_movimiento=?";
        String sqlS = "SELECT Monto, Tipo FROM movimiento WHERE id_movimiento=?";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmtD = conexion.prepareStatement(sqlD);
             PreparedStatement stmtS = conexion.prepareStatement(sqlS)) {

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
                    } else if (tipo.equals("Egreso")) {
                        CajaDAO.actualizarSaldoAutomatico(monto, "Ingreso");
                    }
                }
                return eliminado;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
