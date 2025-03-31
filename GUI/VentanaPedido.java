package GUI;

import Conexion.ConexionDB;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class VentanaPedido {
    private JComboBox<String> comboBox1;
    private JPanel main;
    private JButton agregarButton;
    private JTable table1;
    private JTextField cantidad;
    private JTextField textFieldBuscar;
    private JComboBox<String> comboBox2;
    private JButton eliminarButton;
    private JButton pedirButton;
    private JLabel lblTotal;
    private JLabel lblCliente;
    private JTextArea AreaCliente;
    private JTextField textField1;
    private JButton enviarMensajeButton;

    private ConexionDB conexion = new ConexionDB();
    private HashMap<String, Integer> productoMap = new HashMap<>();
    private DefaultTableModel modeloTabla;
    private int idClienteActual;
    private String nombreCliente;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public VentanaPedido(int idCliente) {
        this.idClienteActual = idCliente;
        this.nombreCliente = obtenerNombreCliente(idCliente);
        initComponents();
        iniciarChat();
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

    public void initComponents() {
        configurarInterfaz();
        cargarDatosIniciales();
        configurarEventos();
    }

    public void configurarInterfaz() {
        modeloTabla = new DefaultTableModel(new Object[]{"Producto", "Tipo Unidad", "Cantidad", "Precio Unitario", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(modeloTabla);

        comboBox2.addItem("Unidad");
        comboBox2.addItem("Blister");
        comboBox2.addItem("Caja");

        lblCliente.setText("Cliente: " + nombreCliente);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public void cargarDatosIniciales() {
        cargarProductos();
        actualizarTotal();
    }

    public void configurarEventos() {
        textFieldBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarProductos(); }
            public void removeUpdate(DocumentEvent e) { filtrarProductos(); }
            public void changedUpdate(DocumentEvent e) { filtrarProductos(); }
        });

        agregarButton.addActionListener(e -> agregarProductoATabla());
        eliminarButton.addActionListener(e -> eliminarProductoDeTabla());
        pedirButton.addActionListener(e -> finalizarPedido());

        cantidad.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarTotal(); }
            public void removeUpdate(DocumentEvent e) { actualizarTotal(); }
            public void changedUpdate(DocumentEvent e) { actualizarTotal(); }
        });
    }

    public String obtenerNombreCliente(int idCliente) {
        try (Connection con = conexion.getConnection();
             PreparedStatement pstmt = con.prepareStatement("SELECT Nombre FROM cliente WHERE id_cliente = ?")) {
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString("Nombre") : "Cliente no encontrado";
        } catch (SQLException e) {
            return "Error al cargar cliente";
        }
    }

    public void cargarProductos() {
        try (Connection con = conexion.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_producto, nombre, precio_U FROM producto WHERE stock > 0")) {

            productoMap.clear();
            comboBox1.removeAllItems();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                productoMap.put(nombre, rs.getInt("id_producto"));
                comboBox1.addItem(nombre);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + e.getMessage());
        }
    }

    public void filtrarProductos() {
        String texto = textFieldBuscar.getText().toLowerCase();
        comboBox1.removeAllItems();
        productoMap.keySet().stream()
                .filter(nombre -> nombre.toLowerCase().contains(texto))
                .forEach(comboBox1::addItem);
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

    public void agregarProductoATabla() {
        String producto = (String) comboBox1.getSelectedItem();
        String tipoUnidad = (String) comboBox2.getSelectedItem();
        String cantidadTexto = cantidad.getText().trim();

        if (producto == null || tipoUnidad == null || cantidadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos");
            return;
        }

        try {
            int cantidadNum = Integer.parseInt(cantidadTexto);
            if (cantidadNum <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad debe ser mayor a 0");
                return;
            }

            int idProducto = productoMap.get(producto);
            int multiplicador = 1;

            switch (tipoUnidad) {
                case "Blister": multiplicador = 10; break;
                case "Caja": multiplicador = 100; break;
            }

            int cantidadTotal = cantidadNum * multiplicador;
            int stockDisponible = obtenerStock(idProducto);

            if (cantidadTotal > stockDisponible) {
                JOptionPane.showMessageDialog(null,
                        "Stock insuficiente\n" +
                                "Disponible: " + stockDisponible + " unidades\n" +
                                "Solicitado: " + cantidadTotal + " unidades (" + cantidadNum + " " + tipoUnidad + ")",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int precioUnitario = obtenerPrecioUnitario(idProducto);
            int subtotal = precioUnitario * cantidadTotal;

            modeloTabla.addRow(new Object[]{
                    producto,
                    tipoUnidad,
                    cantidadNum,
                    "$" + precioUnitario,
                    "$" + subtotal
            });

            actualizarTotal();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese cantidad válida");
        }
    }

    public void eliminarProductoDeTabla() {
        int fila = table1.getSelectedRow();
        if (fila >= 0) {
            modeloTabla.removeRow(fila);
            actualizarTotal();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
        }
    }

    public void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String subtotalStr = modeloTabla.getValueAt(i, 4).toString().replace("$", "");
            total += Double.parseDouble(subtotalStr);
        }
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    public void finalizarPedido() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Agregue productos al pedido");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirmar pedido?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            registrarPedido();
        }
    }

    public void registrarPedido() {
        try (Connection con = conexion.getConnection()) {
            con.setAutoCommit(false);

            double subtotal = calcularSubtotal();
            double total = subtotal * 1.19;

            int idPedido = insertarPedido(con, subtotal, total);

            registrarProductosPedido(con, idPedido);

            con.commit();
            JOptionPane.showMessageDialog(null,
                    String.format("Pedido #%d registrado exitosamente\nCliente: %s\nTotal: $%.2f",
                            idPedido, nombreCliente, total));

            modeloTabla.setRowCount(0);
            actualizarTotal();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al registrar pedido: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public int insertarPedido(Connection con, double subtotal, double total) throws SQLException {
        String sql = "INSERT INTO pedido (id_cliente, Estado, Subtotal, Total) VALUES (?, 'Pendiente', ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idClienteActual);
            pstmt.setDouble(2, subtotal);
            pstmt.setDouble(3, total);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public void registrarProductosPedido(Connection con, int idPedido) throws SQLException {
        String sql = "INSERT INTO venta (id_pedido, id_producto, cantidad, tipo, PrecioU, Fecha) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String producto = (String) modeloTabla.getValueAt(i, 0);
                String tipo = (String) modeloTabla.getValueAt(i, 1);
                int cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString());
                int precio = Integer.parseInt(modeloTabla.getValueAt(i, 3).toString().replace("$", ""));

                pstmt.setInt(1, idPedido);
                pstmt.setInt(2, productoMap.get(producto));
                pstmt.setInt(3, cantidad * obtenerMultiplicador(tipo));
                pstmt.setString(4, tipo);
                pstmt.setInt(5, precio);
                pstmt.setString(6, fecha);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            subtotal += Double.parseDouble(modeloTabla.getValueAt(i, 4).toString().replace("$", ""));
        }
        return subtotal;
    }

    public int obtenerMultiplicador(String presentacion) {
        return switch (presentacion) {
            case "Unidad" -> 1;
            case "Blister" -> 10;
            case "Caja" -> 100;
            default -> 1;
        };
    }

    public int obtenerPrecioUnitario(int idProducto) {
        try (Connection con = conexion.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT precio_U FROM producto WHERE id_producto = " + idProducto)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    public int obtenerStock(int idProducto) {
        try (Connection con = conexion.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT stock FROM producto WHERE id_producto = " + idProducto)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    public static void main(int idCliente) {
        JFrame frame = new JFrame("Realizar Pedido");
        frame.setContentPane(new VentanaPedido(idCliente).main);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}