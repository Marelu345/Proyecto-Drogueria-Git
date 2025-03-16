package DAO;

import Conexion.ConexionDB;
import Clases.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public boolean agregarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (Nombre, Cedula, Email, Telefono) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getCedula());
            stmt.setString(3, cliente.getCorreo());
            stmt.setString(4, cliente.getTelefono());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conexion = ConexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("Nombre"),
                        rs.getString("Cedula"),
                        rs.getString("Email"),
                        rs.getString("Telefono")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public boolean actualizarCliente(Cliente cliente)   {
        String sql = "UPDATE cliente SET Nombre=?, Cedula=?,  Email=?, Telefono=? WHERE id_cliente=?";
        try (Connection conexion = ConexionDB.getConnection();

        PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getCedula());
            statement.setString(3, cliente.getCorreo());
            statement.setString(4, cliente.getTelefono());
            statement.setInt(5, cliente.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}








