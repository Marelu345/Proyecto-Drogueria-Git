package GUI;

import Conexion.ConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class VentasGUI {
    private JButton agregarButton;
    private JButton actualizarButton;
    private JTextField textField1;
    private JComboBox estadoBox;
    private JButton eliminarButton;
    private JTextField textField3;
    private JTextField textField4;
    private JTable table1;
    private JTextField textField5;
    private JTextField textField6;
    private JComboBox comboBox1;
    private JTextField textField8;
    private JTable table2;
    private JButton agregarrButton1;
    private JButton actualizarButton1;
    private JButton eliminarButton1;
    private JComboBox clienteCombo;
    private JPanel main;
    private JComboBox productosBox;
    private JComboBox tipoBox;
    private JTextField textField2;
    private JButton enviarServerButton;
    private JTextArea AreaServer;
    private HashMap<String, Integer> clientesMap = new HashMap<>();
    private HashMap<String, Integer> productoMap = new HashMap<>();
    private HashMap<String, Integer> categoriaMap = new HashMap<>();
    private ConexionDB conexionDB = new ConexionDB();
    private GestionPedidoDAO gestionPedidoDAO = new GestionPedidoDAO();
    private GestionVentasDAO gestionVentasDAO = new GestionVentasDAO();
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;



    public VentasGUI() {
        iniciarServidor();
        ultimoP();
        visualizarOrden();
        inicializarUltimoPedido();
        productos();
        cliente();

        textField1.setEnabled(false);
        textField5.setEnabled(false);
        textField6.setEnabled(false);
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgregarPedido();
                visualizarOrden();
                ultimoP();
            }
        });
        agregarrButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgregarOrdenProdu();
                visualizarOrden();
                ultimoP();
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarpedidoOrden();
            }
        });
        enviarServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (out != null && !textField1.getText().isEmpty()) {
                    String sendMessage = textField1.getText();
                    out.println(sendMessage);
                    AreaServer.append("Servidor: " + sendMessage + "\n");
                    textField1.setText("");
                    if (sendMessage.equalsIgnoreCase("salir")) {
                        cerrarConnections();
                    }
                }
            }
        });
    }

    public void ultimoP() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Id Pedido");
        modelo.addColumn("Id Cliente");
        modelo.addColumn("Estado del Pedido");
        modelo.addColumn("Subtotal");
        modelo.addColumn("Total");

        table1.setModel(modelo);
        modelo.setRowCount(0);

        Connection con = conexionDB.getConnection();

        try (Statement stmt = con.createStatement()) {
            String query = "SELECT p.id_pedido, c.Nombre AS nombre_cliente, p.Estado, p.Subtotal, p.Total " +
                    "FROM pedido p " +
                    "JOIN cliente c ON p.id_cliente = c.id_cliente " +
                    "ORDER BY p.id_pedido DESC LIMIT 1;";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String idPedido = rs.getString(1);
                String nombreCliente = rs.getString(2);
                String estado = rs.getString(3);
                String subtotal = rs.getString(4);
                String total = rs.getString(5);

                String[] fila = {idPedido, nombreCliente, estado, subtotal, total};
                modelo.addRow(fila);

                textField6.setText(idPedido);
                textField1.setText(idPedido);
                clienteCombo.setSelectedItem(nombreCliente);
                textField4.setText(total);
                textField3.setText(subtotal);
                estadoBox.setSelectedItem(estado);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la consulta en último pedido.", e);
        }
    }

    public void inicializarUltimoPedido() {
        int idPedido = obtenerIdPedido();

        if (idPedido != -1) {
            textField6.setText(String.valueOf(idPedido));
        } else {
            JOptionPane.showMessageDialog(null, "No hay pedidos disponibles.");
            textField6.setText("");
        }
    }

    public int obtenerIdPedido() {
        int idPedido = -1;
        Connection con = conexionDB.getConnection();
        String query = "SELECT MAX(id_pedido) AS id_pedido FROM pedido";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                idPedido = rs.getInt("id_pedido");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún pedido en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del último pedido: " + e.getMessage());
        }
        return idPedido;
    }

    public void visualizarOrden() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Venta");
        modelo.addColumn("ID Pedido");
        modelo.addColumn("Producto");
        modelo.addColumn("Tipo");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio Unitario");
        modelo.addColumn("Fecha");
        table2.setModel(modelo);
        modelo.setRowCount(0);

        int idPedido;
        try {
            String input = textField6.getText();
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("El campo de texto está vacío.");
            }
            idPedido = Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un ID de pedido válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection con = conexionDB.getConnection();
        try (Statement stmt = con.createStatement()) {
            String query = "SELECT v.id_venta, v.id_pedido, p.Nombre AS nombre_producto, " +
                    "v.tipo, v.cantidad, v.PrecioU, v.Fecha " +
                    "FROM venta AS v " +
                    "JOIN producto AS p ON v.id_producto = p.id_producto " +
                    "WHERE v.id_pedido = " + idPedido;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String idVenta = rs.getString("id_venta");
                String idPedidoDb = rs.getString("id_pedido");
                String producto = rs.getString("nombre_producto");
                String tipo = rs.getString("tipo");
                String cantidad = rs.getString("cantidad");
                String precioUnitario = rs.getString("PrecioU");
                String fecha = rs.getString("Fecha");

                modelo.addRow(new Object[]{idVenta, idPedidoDb, producto, tipo, cantidad, precioUnitario, fecha});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al visualizar las órdenes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void iniciarServidor() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(12345);
                AreaServer.append("Servidor iniciado. Esperando cliente...\n");
                clientSocket = serverSocket.accept();
                AreaServer.append("Cliente conectado.\n");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                String receiveMessage;
                while ((receiveMessage = in.readLine()) != null) {
                    if (receiveMessage.equalsIgnoreCase("salir")) {
                        AreaServer.append("Cliente ha salido del chat.\n");
                        break;
                    }
                    AreaServer.append("Cliente: " + receiveMessage + "\n");
                }
            } catch (IOException e) {
                AreaServer.append("Error en el servidor: " + e.getMessage() + "\n");
            } finally {
                cerrarConnections();
            }
        }).start();
    }

    public void cerrarConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
            AreaServer.append("Conexiones cerradas.\n");
        } catch (IOException e) {
            AreaServer.append("Error al cerrar conexiones: " + e.getMessage() + "\n");
        }
    }

    public void AgregarPedido() {
        String nombreCliente = (String) clienteCombo.getSelectedItem();
        int id_Cliente = clientesMap.getOrDefault(nombreCliente, -1);

        String estado = (String) estadoBox.getSelectedItem();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha = now.format(formatter);

        GestionPedido pedidos = new GestionPedido(0, id_Cliente, estado, 0, 0);
        if (gestionPedidoDAO.ingresarPedido(pedidos)) {
            JOptionPane.showMessageDialog(null, "¡Pedido agregado con éxito!");

            int idPedido = obtenerIdPedido();
            textField6.setText(String.valueOf(idPedido));
            visualizarOrden();
            ultimoP();
        }
    }

    public void AgregarOrdenProdu() {
        if (textField6.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Recuerda llenar todos los campos antes de añadir la orden. Gracias.");
            return;
        }

        String productos = (String) productosBox.getSelectedItem();
        int id_Producto = productoMap.getOrDefault(productos, -1);

        if (id_Producto == -1) {
            JOptionPane.showMessageDialog(null, "Producto no válido seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidadProducto;
        try {
            cantidadProducto = Integer.parseInt(textField2.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un número válido en el campo de cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String presentacion = (String) tipoBox.getSelectedItem();
        int id_Pedido = Integer.parseInt(textField6.getText());
        int precioUnitario = obtenerPrecioUnitario(id_Producto);

        int multiplicador = obtenerMultiplicador(presentacion);
        if (multiplicador == -1) {
            JOptionPane.showMessageDialog(null, "Presentación no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidadTotal = cantidadProducto * multiplicador;

        int stockActual = obtenerStock(id_Producto);
        if (stockActual - cantidadTotal < 0) {
            JOptionPane.showMessageDialog(null, "Stock insuficiente. Stock actual: " + stockActual + ". No se puede añadir el producto a la orden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int precioTotal = precioUnitario * cantidadTotal;

        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha = fechaActual.format(formatoFecha);

        GestionVentas gestionVentas = new GestionVentas(0, id_Pedido, id_Producto, cantidadTotal, presentacion, precioUnitario, fecha);

        if (gestionVentasDAO.ingresarGestionVentas(gestionVentas)) {
            JOptionPane.showMessageDialog(null, "Venta agregada con éxito. \nCantidad total: " + cantidadTotal + " unidades." +
                    "\nPrecio total sin IVA: $" + precioTotal +
                    "\nFecha de registro: " + fecha);

            int nuevoSubtotal = calcularNuevoSubtotal(id_Pedido, precioTotal);

            actualizarSubtotalPedido(id_Pedido, nuevoSubtotal);

            String estadoPedido = obtenerEstadoPedido(id_Pedido);
            if ("Enviado".equalsIgnoreCase(estadoPedido)) {
                int nuevoStock = stockActual - cantidadTotal;
                actualizarStock(id_Producto, nuevoStock);
            } else {
                JOptionPane.showMessageDialog(null, "El estado del pedido no es 'Enviado'. El stock no se actualizará.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Error al agregar la venta. Por favor, inténtalo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarpedidoOrden() {
        if (textField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa el ID del pedido antes de actualizar.");
            return;
        }

        int idPedido = Integer.parseInt(textField1.getText());
        String estadoActual = obtenerEstadoPedido(idPedido);
        String nombreCliente = (String) clienteCombo.getSelectedItem();
        String nuevoEstado = (String) estadoBox.getSelectedItem();
        int idCliente = clientesMap.getOrDefault(nombreCliente, -1);

        if (idCliente == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona un cliente válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int subtotal = obtenerSubtotalPedido(idPedido);
        int total = calcularTotalConIVA(subtotal);

        gestionPedidoDAO.datosActualizados(idPedido, idCliente, nuevoEstado, subtotal, total);

        if (out != null) {
            if ("Enviado".equalsIgnoreCase(nuevoEstado) && !"Enviado".equalsIgnoreCase(estadoActual)) {
                String mensaje = "Tu pedido #" + idPedido + " ha sido enviado";
                out.println(mensaje);
                AreaServer.append("Servidor (mensaje automático): " + mensaje + "\n");
            }
            else if ("Entregado".equalsIgnoreCase(nuevoEstado) && !"Entregado".equalsIgnoreCase(estadoActual)) {
                String mensaje = "Tu pedido #" + idPedido + " ha sido entregado";
                out.println(mensaje);
                AreaServer.append("Servidor (mensaje automático): " + mensaje + "\n");
            }
        }

        if ("Enviado".equalsIgnoreCase(nuevoEstado)) {
            Connection con = conexionDB.getConnection();
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id_producto, cantidad FROM venta WHERE id_pedido = " + idPedido)) {

                while (rs.next()) {
                    int idProductoOrdenado = rs.getInt("id_producto");
                    int cantidad = rs.getInt("cantidad");

                    boolean stockActualizado = restarStock(idProductoOrdenado, cantidad);
                    if (stockActualizado) {
                        validarStockBajo(idProductoOrdenado);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "No se pudo actualizar el stock para el producto con ID: " + idProductoOrdenado,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar el stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        if ("Entregado".equalsIgnoreCase(nuevoEstado)) {
            registrarMovimiento(total);
        }

        visualizarOrden();
        ultimoP();
    }

    public void registrarMovimiento(int monto) {
        Connection con = conexionDB.getConnection();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaActual = now.format(formatter);

        String query = "INSERT INTO movimiento (Tipo, Categoria, Monto, fecha) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "Ingreso");
            pstmt.setString(2, "Venta");
            pstmt.setInt(3, monto);
            pstmt.setString(4, fechaActual);

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Movimiento registrado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar el movimiento.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el movimiento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public int obtenerSubtotalPedido(int idPedido) {
        int subtotal = 0;
        Connection con = conexionDB.getConnection();
        String query = "SELECT Subtotal FROM pedido WHERE id_pedido = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                subtotal = rs.getInt("Subtotal");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el subtotal: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return subtotal;
    }

    public int calcularTotalConIVA(int subtotal) {
        return (int) Math.round(subtotal * 1.19);
    }

    public boolean restarStock(int idProducto, int cantidad) {
        Connection con = conexionDB.getConnection();
        String query = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);
            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al reducir el stock: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public void validarStockBajo(int idProducto) {
        int stockActual = obtenerStock(idProducto);
        int limiteCritico = 5;
        if (stockActual <= limiteCritico) {
            JOptionPane.showMessageDialog(null,
                    "El stock del producto con ID: " + idProducto + " está bajo. Stock actual: " + stockActual,
                    "Alerta de Stock Bajo", JOptionPane.WARNING_MESSAGE);
        }
    }

    public String obtenerEstadoPedido(int idPedido) {
        String estado = "";
        String consulta = "SELECT estado FROM pedido WHERE id_pedido = ?";
        try (Connection con = conexionDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(consulta)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estado = rs.getString("estado");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener el estado del pedido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return estado;
    }

    public int calcularNuevoSubtotal(int idPedido, int precioTotal) {
        int subtotalActual = obtenerSubtotalActual(idPedido);
        return subtotalActual + precioTotal;
    }

    public int obtenerSubtotalActual(int idPedido) {
        int subtotal = 0;
        Connection con = conexionDB.getConnection();
        String query = "SELECT Subtotal FROM pedido WHERE id_pedido = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                subtotal = rs.getInt("Subtotal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener el subtotal del pedido: " + e.getMessage());
        }

        return subtotal;
    }

    public void actualizarSubtotalPedido(int idPedido, int nuevoSubtotal) {
        Connection con = conexionDB.getConnection();
        String query = "UPDATE pedido SET Subtotal = ? WHERE id_pedido = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, nuevoSubtotal);
            pstmt.setInt(2, idPedido);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el subtotal del pedido: " + e.getMessage());
        }
    }

    public void actualizarStock(int idProducto, int nuevoStock) {
        String consulta = "UPDATE producto SET Stock = ? WHERE id_producto = ?";
        try (Connection con = conexionDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(consulta)) {
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, idProducto);

            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Stock actualizado correctamente para el producto con ID: " + idProducto);
            } else {
                System.out.println("No se pudo actualizar el stock para el producto con ID: " + idProducto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int obtenerStock(int idProducto) {
        String consulta = "SELECT Stock FROM producto WHERE id_producto = ?";
        try (Connection con = conexionDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(consulta)) {
            stmt.setInt(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Stock");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener el Stock del producto.");
        }
        return 0;
    }

    public int obtenerPrecioUnitario(int idProducto) {
        int precioUnitario = 0;
        Connection con = ConexionDB.getConnection();
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT precio_U FROM producto WHERE id_producto = " + idProducto;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                precioUnitario = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return precioUnitario;
    }

    public void cliente() {
        clienteCombo.removeAllItems();
        Connection con = conexionDB.getConnection();
        String query = "SELECT id_cliente, Nombre FROM cliente";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id_cliente");
                String nombre = rs.getString("Nombre");

                clientesMap.put(nombre, id);
                clienteCombo.addItem(nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los clientes");
        }
    }

    public void productos() {
        productosBox.removeAllItems();
        Connection con = conexionDB.getConnection();
        String query = "SELECT id_producto, Nombre, Stock FROM producto";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id_producto");
                String nombre = rs.getString("Nombre");
                int stock = rs.getInt("Stock");

                if (stock > 0) {
                    productoMap.put(nombre, id);
                    productosBox.addItem(nombre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los productos desde la base de datos.");
        }
    }

    public int obtenerMultiplicador(String presentacion) {
        switch (presentacion) {
            case "Unidad":
                return 1;
            case "Blister":
                return 10;
            case "Caja":
                return 100;
            default:
                return -1;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pedido Cliente");
        frame.setContentPane(new VentasGUI().main);
        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}