package DAO;

import Conexion.ConexionDB;
import Clases.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Esta es la clase encargada de manejar las operaciones de base de datos relacionadas con los clientes.
 * Permite agregar, obtener, actualizar y eliminar clientes en la base de datos.
 */
public class ClienteDAO {
    /**
     * Agrega un nuevo cliente a la base de datos.
     * @param cliente Objeto Cliente con los datos a registrar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean agregarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (Nombre, Cedula, Email, Telefono) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getCedula());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Obtiene la lista de todos los clientes registrados en la base de datos.
     * @return Lista de clientes.
     */
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
    /**
     * Obtiene el último cliente registrado en la base de datos.
     * @return Objeto Cliente con los datos del último cliente.
     */
    public Cliente obtenerUltimoCliente() {
        Cliente cliente = null;
        String sql = "SELECT * FROM cliente ORDER BY id_cliente DESC LIMIT 1";

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSets = preparedStatement.executeQuery()) {

            if (resultSets.next()) {
                cliente = new Cliente(
                        resultSets.getInt("id_cliente"),
                        resultSets.getString("Nombre"),
                        resultSets.getString("cedula"),
                        resultSets.getString("Email"),
                        resultSets.getString("telefono")
                );
            }
            resultSets.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cliente;
    }



///**
// * Actualiza la información de un cliente en la base de datos.

    public boolean actualizarCliente(Cliente cliente)   {
        String sql = "UPDATE cliente SET Nombre=?, Cedula=?,  Email=?, Telefono=? WHERE id_cliente=?";
        try (Connection conexion = ConexionDB.getConnection();

        PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getCedula());
            statement.setString(3, cliente.getEmail());
            statement.setString(4, cliente.getTelefono());
            statement.setInt(5, cliente.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



///**
// * Elimina un cliente de la base de datos basado en su ID.

    public boolean eliminarCliente(String id) {
        String deleteCliente = "DELETE FROM cliente WHERE id_cliente=?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement psCliente = conexion.prepareStatement(deleteCliente)) {
            psCliente.setString(1, id);
            return psCliente.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}








