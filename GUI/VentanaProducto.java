package GUI;

import Clases.Producto;
import DAO.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
        obtenerProductos();
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarProducto();

            }
        });
    }
    public void obtenerProductos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripción");
        modelo.addColumn("Tipo");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Stock Mínimo");

        table1.setModel(modelo);

        List<Producto> productos = productoDAO.obtenerProductos();
        while (!productos.isEmpty()) {
            Producto p   = productos.remove(0);
            modelo.addRow(new Object[]{p.getIdProducto(), p.getNombre(), p.getDescripcion(), p.getTipo(), p.getPrecio(), p.getStock(), p.getStockMinimo()});
        }
    }

    public void agregarProducto() {
        Producto nuevoProducto = new Producto(0, textField1.getText(), textField2.getText(), textField3.getText(), Double.parseDouble(textField4.getText()), Integer.parseInt(textField5.getText()), Integer.parseInt(textField6.getText())
        );
        if (productoDAO.agregarProducto(nuevoProducto)) {
            JOptionPane.showMessageDialog(null, "Producto agregado.");
        }
    }

    public static void main(String[] args) {
        VentanaProducto ventana = new VentanaProducto();
        JFrame frame = new JFrame("Productos");
        frame.setContentPane(ventana.main);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setResizable(true);
        frame.setVisible(true);
        ventana.obtenerProductos();
    }
}
