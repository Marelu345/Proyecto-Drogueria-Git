package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/drogueria", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}