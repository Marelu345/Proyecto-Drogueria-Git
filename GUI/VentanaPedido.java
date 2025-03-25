package GUI;

import Clases.Pedido;
import Conexion.ConexionDB;
import DAO.PedidoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VentanaPedido {
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JPanel main;
    private JTextField disponible;
    private JButton agregarButton;
    private JTable table1;
    private JButton eliminarButton;
    private JButton pedirButton;

    private ConexionDB conexion = new ConexionDB();

    private PedidoDAO pedidoDAO = new PedidoDAO();

    int filas;
    int id_venta, id_pedido;

    public VentanaPedido(){
        obtenerCombobox();
        obtenerDatos();
        agregarButton.addActionListener(new ActionListener() {@Override
        public void actionPerformed(ActionEvent e) {
            VentanaPedido.nombreProducto nombreProducto = new VentanaPedido.nombreProducto();
            String dispo = disponible.getText();
            if(dispo.equals("Disponible")){

                VentanaPedido.nombreProducto prod = (VentanaPedido.nombreProducto) comboBox1.getSelectedItem();

                int id_producto = prod.getId_producto();
                //int cantidad = Integer.parseInt(textField4.getText());


                Pedido pedido = new Pedido(0,id_venta, 0,0,"estado","");


                if (pedidoDAO.agregar(pedido))
                {
                    JOptionPane.showMessageDialog(null, "Pedido creado con exito");
                    pedidoDAO.actualizar(pedido);
                  //  obtenerDatos();


                }
            }else
                JOptionPane.showMessageDialog(null, "Producto no Disponible");



            //textField.setText("");

            //obtenerDatos();
        }});


        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {;

                if (pedidoDAO.eliminar(id_pedido))
                {
                    JOptionPane.showMessageDialog(null, "Pedido eliminado con exito");
                }
              //  obtenerDatos();

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

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaPedido.nombreProducto nombreProducto = (VentanaPedido.nombreProducto) comboBox1.getSelectedItem();
            }
        });
        pedirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaVenta ventanaVenta = new VentanaVenta();
                ventanaVenta.main();
            }
        });
    }


    public void obtenerDatos() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Id_pedido");
        modelo.addColumn("Id_venta");
        modelo.addColumn("Nombre Producto");
        modelo.addColumn("Tipo de Unidad");
        modelo.addColumn("SubTotal");
        modelo.addColumn("Precio Total");




        table1.setModel(modelo);

        String[] dato = new String[7];
        Connection con = conexion.getConnection();

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_pedido, id_venta, producto.Nombre AS id_producto,pedido.TipoU,pedido.Subtotal,Total FROM pedido JOIN producto ON pedido.id_producto = producto.id_producto;");

            while (rs.next()){
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);
                dato[5] = rs.getString(6);


                modelo.addRow(dato);
            }
        }

        catch (SQLException e){
            e.printStackTrace();

        }



    }


    public void obtenerCombobox() {

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

    }


    class nombreProducto{
        private int id_producto;
        private String nombre, disponibilidad;

        public  nombreProducto(int id_producto, String nombre){
            this.id_producto = id_producto;
            this.nombre = nombre;
            this.disponibilidad = disponibilidad;
        }

        public nombreProducto() {

        }

        public  int getId_producto(){
            return  id_producto;
        }

        @Override
        public String toString(){
            return  nombre;
        }
    }


    public static void main() {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(new VentanaPedido().main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(true);
        frame.setVisible(true);

    }

}

