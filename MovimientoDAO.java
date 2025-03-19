import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public boolean actualizarMovimiento(Movimiento movimiento)   {
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


public boolean eliminarMovimiento(int id) {
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
