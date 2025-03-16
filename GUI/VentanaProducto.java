package GUI;

import Clases.Producto;
import DAO.ProductoDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaProducto {
    private JPanel main;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton button1;
    private ProductoDAO productoDAO = new ProductoDAO();

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
