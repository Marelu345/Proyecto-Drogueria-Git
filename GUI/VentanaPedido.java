package GUI;


import Conexion.ConexionDB;
import DAO.VentaDAO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    private JTextArea AreaCliente;
    private JTextField textField1;
    private JButton enviarMensajeButton;

    private ConexionDB conexion = new ConexionDB();
    private ArrayList<NombreProducto> productos = new ArrayList<>();
    private DefaultTableModel modeloTabla;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private VentaDAO ventaDAO = new VentaDAO();

    public VentanaPedido(){
        cargarProductos();
        configurarTabla();
        iniciarChat();

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


        pedirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaVenta ventanaVenta = new VentanaVenta();
                ventanaVenta.main();
            }
        });
        enviarMensajeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (out != null && !textField1.getText().isEmpty()) {
                    String sendMessage = textField1.getText();
                    out.println(sendMessage);
                    AreaCliente.append("Cliente: " + sendMessage + "\n");
                    textField1.setText("");
                    if (sendMessage.equalsIgnoreCase("salir")) {
                        cerrarConexion();
                    }
                }
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
    public void iniciarChat() {
        new Thread(() -> {
            String serverAddress = JOptionPane.showInputDialog("Ingresa la IP del servidor (localhost si es local): ");
            if (serverAddress == null || serverAddress.isEmpty()) serverAddress = "localhost";

            try {
                socket = new Socket(serverAddress, 12345);
                AreaCliente.append("Conectado al servidor.\n");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String receiveMessage;
                while ((receiveMessage = in.readLine()) != null) {
                    if (receiveMessage.equalsIgnoreCase("salir")) {
                        AreaCliente.append("El servidor ha cerrado la conexión.\n");
                        break;
                    }
                    AreaCliente.append("Servidor: " + receiveMessage + "\n");
                }
            } catch (IOException e) {
                AreaCliente.append("Error al conectar: " + e.getMessage() + "\n");
            } finally {
                cerrarConexion();
            }
        }).start();
    }
    public void cerrarConexion() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            AreaCliente.append("Conexiones cerradas.\n");
        } catch (IOException e) {
            AreaCliente.append("Error al cerrar conexiones: " + e.getMessage() + "\n");
        }
    }


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
        JFrame frame = new JFrame("Pedido Cliente");
        frame.setContentPane(new VentanaPedido().main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(true);
        frame.setVisible(true);

    }

}

