
package GUI;

import Clases.Cliente;
import Conexion.ConexionDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginGUI extends JDialog {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton button1;
    private JButton button2;
    private Connection conexion;
    public Cliente cliente;

    public LoginGUI(JFrame parent) {
        super(parent);
        setTitle("Iniciar Cliente");
        setContentPane(panel1);
        setMinimumSize(new Dimension(700, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textField1.getText();
                String cedula = textField2.getText();
                cliente = getIdentificarCliente(nombre, cedula);

                if (cliente != null) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Inicio de sesión exitoso", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    VentanaPedido.main(null);
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Nombre o Identificación incorrecta", "Intente otra vez", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    public Cliente getIdentificarCliente(String nombre, String cedula) {
        Cliente cliente = null;
        try {
            conexion = ConexionDB.getConnection();
            String sql = "SELECT * FROM cliente WHERE Nombre = ? AND Cedula = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, cedula);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                cliente = new Cliente();
                cliente.setId(resultSet.getInt("ID_CLIENTE"));
                cliente.setNombre(resultSet.getString("Nombre"));
                cliente.setCedula(resultSet.getString("Cedula"));
                cliente.setEmail(resultSet.getString("Email"));
                cliente.setTelefono(resultSet.getString("Telefono"));
            }
            resultSet.close();
            preparedStatement.close();
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public static void main(String[] args) {
        LoginGUI loginGUI = new LoginGUI(null);
        if (loginGUI.cliente != null) {
            System.out.println("Identificación exitosa de " + loginGUI.cliente.getId());
            System.out.println("Nombre: " + loginGUI.cliente.getNombre());
            System.out.println("Cedula: " + loginGUI.cliente.getCedula());
            System.out.println("Email: " + loginGUI.cliente.getEmail());
            System.out.println("Teléfono: " + loginGUI.cliente.getTelefono());
        } else {
            System.out.println("Identificación cancelada o incorrecta.");
        }
    }
}
