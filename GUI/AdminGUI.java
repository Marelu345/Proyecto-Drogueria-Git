
package GUI;

import Clases.Admin;
import Conexion.ConexionDB;
import Imagenes.Imagenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminGUI extends JDialog {
    private JPanel main;
    private JPasswordField textField2;
    private JButton button1;
    private JButton button2;
    private JLabel logo;
    private JPanel foto;
    private Connection conexion;
    public Admin admin;

    public AdminGUI(JFrame parent) {
        super(parent);
        setTitle("Iniciar Administrador");
        setContentPane(main);
        setMinimumSize(new Dimension(700, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contrasena = textField2.getText();
                admin = getIdentificarAdmin(contrasena);

                if (admin != null) {
                    JOptionPane.showMessageDialog(AdminGUI.this, "Inicio de sesión exitoso", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    VentanaPrincipal.main(null);
                } else {
                    JOptionPane.showMessageDialog(AdminGUI.this, "Contraseña incorrecta", "Intente otra vez", JOptionPane.ERROR_MESSAGE);
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

    public Admin getIdentificarAdmin(String contrasena) {
        Admin admin = null;
        try {
            conexion = ConexionDB.getConnection();
            String sql = "SELECT * FROM admin WHERE Contrasena = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, contrasena);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                admin = new Admin();
                admin.setId(resultSet.getInt("ID"));
                admin.setContrasena(resultSet.getString("Contrasena"));
            }
            resultSet.close();
            preparedStatement.close();
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public static void main(String[] args) {
        AdminGUI  adminGUI= new AdminGUI(null);
        if (adminGUI.admin != null) {
            System.out.println("Inicio exitosa de " + adminGUI.admin.getId());
            System.out.println("Contrasena: " + adminGUI.admin.getContrasena());

        } else {
            System.out.println("Identificación cancelada o incorrecta.");
        }
    }


}
