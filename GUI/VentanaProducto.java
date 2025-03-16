package GUI;

import Clases.Producto;
import DAO.ProductoDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaProducto {
    public JPanel main;
    public JTable table1;
    public JTextField textField1;
    public JTextField textField2;
    public JTextField textField3;
    public JTextField textField4;
    public JTextField textField5;
    public JTextField textField6;
    public JButton button1;
    public ProductoDAO productoDAO = new ProductoDAO();

    public VentanaProducto() {
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarProducto();

            }
        });
    }

    public void agregarProducto() {
        Producto nuevoProducto = new Producto(0, textField1.getText(), textField2.getText(), textField3.getText(), Double.parseDouble(textField4.getText()), Integer.parseInt(textField5.getText()), Integer.parseInt(textField6.getText())
        );
        if (productoDAO.agregarProducto(nuevoProducto)) {
            JOptionPane.showMessageDialog(null, "Producto agregado.");
        }
    }

    public void ejecutar() {
        JFrame frame = new JFrame("Gestión de Productos");
        frame.setContentPane(this.main);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
