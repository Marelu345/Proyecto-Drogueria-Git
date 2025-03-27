package GUI;

import Clases.Pedido;
import Conexion.ConexionDB;
import DAO.PedidoDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VentanaPedido {
    private JComboBox comboBox1;
    private JPanel main;
    private JButton agregarButton;
    private JTable table1;
    private JTextField cantidad;
    private JTextField textFieldBuscar;
    private JComboBox comboBox2;
    private JButton eliminarButton;
    private JButton pedirButton;

    private ConexionDB conexion = new ConexionDB();
    private ArrayList<NombreProducto> productos = new ArrayList<>();
    private DefaultTableModel modeloTabla;

    public VentanaPedido(){
        cargarProductos();
        configurarTabla();

        textFieldBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarProductos(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarProductos(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarProductos(); }
        });

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProductoATabla();
            }
        });


        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {;eliminarProductoDeTabla();
            }

        });


        /*table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {

                    //textField4.setText(table1.getValueAt(selectedRow, 4).toString()); // Capacidad
                    comboBox1.setSelectedItem(table1.getValueAt(selectedRow, 1).toString());
                    comboBox2.setSelectedItem(table1.getValueAt(selectedRow, 2).toString());
                    id_pedido = Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());


                }


            }
        });*/

        pedirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaVenta ventanaVenta = new VentanaVenta();
                ventanaVenta.main();
            }
        });
    }
    public void configurarTabla() {
        modeloTabla = new DefaultTableModel(new Object[]{"Producto", "Tipo Unidad", "Cantidad"}, 0);
        table1.setModel(modeloTabla);
    }

    public void agregarProductoATabla() {
        NombreProducto productoSeleccionado = (NombreProducto) comboBox1.getSelectedItem();
        String tipoUnidad = (String) comboBox2.getSelectedItem();
        String cantidadTexto = cantidad.getText().trim();

        if (productoSeleccionado == null || tipoUnidad == null || cantidadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos");
            return;
        }

        int cantidadNum;
        try {
            cantidadNum = Integer.parseInt(cantidadTexto);
            if (cantidadNum <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un número válido en cantidad");
            return;
        }

        modeloTabla.addRow(new Object[]{productoSeleccionado.getNombre(), tipoUnidad, cantidadNum});
    }
    public void eliminarProductoDeTabla() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada >= 0) {
            modeloTabla.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla para eliminar");
        }
    }
    public void cargarProductos() {
        Connection con = conexion.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_producto, nombre FROM producto");
            productos.clear();
            while (rs.next()) {
                productos.add(new NombreProducto(rs.getInt("id_producto"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void filtrarProductos() {
        String texto = textFieldBuscar.getText().toLowerCase();
        comboBox1.removeAllItems();
        for (NombreProducto prod : productos) {
            if (prod.getNombre().toLowerCase().contains(texto)) {
                comboBox1.addItem(prod);
            }
        }
    }

    /*public void obtenerDatos() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Nombre Producto");
        modelo.addColumn("Tipo de Unidad");
        modelo.addColumn("Cantidad");

        table1.setModel(modelo);

        String[] dato = new String[7];
        Connection con = conexion.getConnection();

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT producto.Nombre AS id_producto,pedido.TipoU FROM pedido JOIN producto ON pedido.id_producto = producto.id_producto;");

            while (rs.next()){
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);


                modelo.addRow(dato);
            }
        }

        catch (SQLException e){
            e.printStackTrace();

        }



    }
     */


    /*public void obtenerCombobox() {

        Connection con = conexion.getConnection();

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_producto, nombre FROM producto");
            while (rs.next()) {
                int id = rs. getInt("id_producto");
                String nombre = rs.getString("nombre");
                comboBox1.addItem(new VentanaPedido.nombreProducto(id,nombre));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }*/


    class NombreProducto{
        private int id_producto;
        private String nombre;

        public  NombreProducto(int id_producto, String nombre){
            this.id_producto = id_producto;
            this.nombre = nombre;
        }
        public String getNombre() {
            return nombre;
        }
        @Override
        public String toString(){
            return  nombre;
        }
    }


    public static void main(String[] args ) {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(new VentanaPedido().main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(true);
        frame.setVisible(true);

    }

}

