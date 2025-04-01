package GUI;

import Conexion.ConexionDB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReportesVentasGUI extends JFrame {
    private JFrame frame;
    private JComboBox<String> tipoReporteCombo;
    private JButton generarButton;
    private JTable tablaReportes;
    private JPanel main;
    private ConexionDB conexionDB;
    private Color backgroundColor = new Color(212, 229, 247); // Fondo claro
    private Color sectionColor = new Color(80, 160, 220); // Color de sección (similar a la imagen)
    private Color buttonColor = new Color(60, 140, 200); // Color de botón

    public ReportesVentasGUI() {
        conexionDB = new ConexionDB();
        initialize();
    }

    public void initialize() {
        frame = new JFrame("Reportes de Ventas - Caja");
        frame.setSize(900, 650);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(backgroundColor);

        // Panel de título de sección (similar al de la imagen)
        JPanel sectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sectionPanel.setBackground(sectionColor);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel sectionTitle = new JLabel("REPORTES");
        sectionTitle.setForeground(Color.WHITE);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionPanel.add(sectionTitle);

        // Panel principal de controles
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        controlPanel.setBackground(backgroundColor);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Componentes con nuevo estilo
        JLabel etiqueta = new JLabel("Tipo de Reporte:");
        etiqueta.setForeground(Color.DARK_GRAY);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tipoReporteCombo = new JComboBox<>(new String[]{"Diario", "Semanal", "Mensual", "Stock Minimo"});
        estiloComponente(tipoReporteCombo);

        generarButton = new JButton("Generar Reporte");
        estiloBoton(generarButton);

        controlPanel.add(etiqueta);
        controlPanel.add(tipoReporteCombo);
        controlPanel.add(generarButton);

        // Panel de tabla con bordes
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        tablePanel.setBackground(backgroundColor);

        // Tabla con nuevo estilo
        tablaReportes = new JTable();
        estiloTabla(tablaReportes);

        JScrollPane scrollPane = new JScrollPane(tablaReportes);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resultados del Reporte"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Construcción del frame principal
        frame.add(sectionPanel, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.CENTER);
        frame.add(tablePanel, BorderLayout.SOUTH);

        generarButton.addActionListener(e -> generarReporte());

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    // Métodos de estilo actualizados
    private void estiloComponente(JComponent componente) {
        componente.setBackground(Color.WHITE);
        componente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        componente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    private void estiloBoton(JButton boton) {
        boton.setBackground(buttonColor);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(buttonColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        boton.setFocusPainted(false);

        // Efecto hover mejorado
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(buttonColor.brighter());
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(buttonColor);
            }
        });
    }

    private void estiloTabla(JTable tabla) {
        tabla.setBackground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setSelectionBackground(sectionColor);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setRowHeight(28);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(0, 1));

        // Encabezado de tabla mejorado
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(sectionColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
    }

    // Los métodos de generación de reportes permanecen iguales
    private void generarReporte() {
        String tipoReporte = (String) tipoReporteCombo.getSelectedItem();
        DefaultTableModel modelo = new DefaultTableModel();

        switch (tipoReporte) {
            case "Diario": modelo = generarReporteDiario(); break;
            case "Semanal": modelo = generarReporteSemanal(); break;
            case "Mensual": modelo = generarReporteMensual(); break;
            case "Stock Minimo": modelo = generarReporteStock(); break;
        }

        tablaReportes.setModel(modelo);
        estiloTabla(tablaReportes);
    }

    public DefaultTableModel generarReporteDiario() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Fecha");
        modelo.addColumn("Total Ventas");
        modelo.addColumn("N° Pedidos");
        modelo.addColumn("Producto Más Vendido");

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaStr = fechaActual.format(formatter);

        Connection con = conexionDB.getConnection();
        String query = "SELECT " +
                "DATE(v.Fecha) as fecha, " +
                "SUM(p.Total) as total_ventas, " +
                "COUNT(DISTINCT v.id_pedido) as num_pedidos, " +
                "(SELECT pr.Nombre FROM producto pr " +
                " JOIN venta ve ON pr.id_producto = ve.id_producto " +
                " WHERE DATE(ve.Fecha) = DATE(v.Fecha) " +
                " GROUP BY pr.id_producto ORDER BY SUM(ve.cantidad) DESC LIMIT 1) as producto_mas_vendido " +
                "FROM venta v " +
                "JOIN pedido p ON v.id_pedido = p.id_pedido " +
                "WHERE DATE(v.Fecha) = ? " +
                "GROUP BY DATE(v.Fecha)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, fechaStr);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Object[] fila = {
                        rs.getString("fecha"),
                        rs.getDouble("total_ventas"),
                        rs.getInt("num_pedidos"),
                        rs.getString("producto_mas_vendido")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al generar reporte diario: " + e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel generarReporteStock() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre Producto");
        modelo.addColumn("Stock");
        modelo.addColumn("Stock Minimo");

        Connection con = conexionDB.getConnection();
        String query = "SELECT Nombre,  Stock, Stock_M, Tipo FROM producto WHERE Stock <= Stock_M;";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Object[] fila = {
                        rs.getString("Nombre"),
                        rs.getInt("Stock"),
                        rs.getInt("Stock_M"),
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al generar reporte Stock: " + e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel generarReporteSemanal() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Semana");
        modelo.addColumn("Total Ventas");
        modelo.addColumn("N° Pedidos");
        modelo.addColumn("Promedio Diario");
        modelo.addColumn("Día con Más Ventas");

        Connection con = conexionDB.getConnection();
        String query = "SELECT " +
                "YEARWEEK(v.Fecha) as semana, " +
                "SUM(p.Total) as total_ventas, " +
                "COUNT(DISTINCT v.id_pedido) as num_pedidos, " +
                "SUM(p.Total)/7 as promedio_diario, " +
                "(SELECT DATE(ve.Fecha) FROM venta ve " +
                " JOIN pedido pe ON ve.id_pedido = pe.id_pedido " +
                " WHERE YEARWEEK(ve.Fecha) = YEARWEEK(v.Fecha) " +
                " GROUP BY DATE(ve.Fecha) ORDER BY SUM(pe.Total) DESC LIMIT 1) as dia_max_ventas " +
                "FROM venta v " +
                "JOIN pedido p ON v.id_pedido = p.id_pedido " +
                "WHERE YEARWEEK(v.Fecha) = YEARWEEK(CURDATE()) " +
                "GROUP BY YEARWEEK(v.Fecha)";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                Object[] fila = {
                        rs.getString("semana"),
                        rs.getDouble("total_ventas"),
                        rs.getInt("num_pedidos"),
                        rs.getDouble("promedio_diario"),
                        rs.getString("dia_max_ventas")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al generar reporte semanal: " + e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel generarReporteMensual() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Mes");
        modelo.addColumn("Total Ventas");
        modelo.addColumn("N° Pedidos");
        modelo.addColumn("Promedio Diario");
        modelo.addColumn("Semana con Más Ventas");
        modelo.addColumn("Producto Más Vendido");

        Connection con = conexionDB.getConnection();
        String query = "SELECT " +
                "DATE_FORMAT(v.Fecha, '%Y-%m') as mes, " +
                "SUM(p.Total) as total_ventas, " +
                "COUNT(DISTINCT v.id_pedido) as num_pedidos, " +
                "SUM(p.Total)/DAY(LAST_DAY(v.Fecha)) as promedio_diario, " +
                "(SELECT CONCAT('Semana ', YEARWEEK(ve.Fecha)) FROM venta ve " +
                " JOIN pedido pe ON ve.id_pedido = pe.id_pedido " +
                " WHERE DATE_FORMAT(ve.Fecha, '%Y-%m') = DATE_FORMAT(v.Fecha, '%Y-%m') " +
                " GROUP BY YEARWEEK(ve.Fecha) ORDER BY SUM(pe.Total) DESC LIMIT 1) as semana_max_ventas, " +
                "(SELECT pr.Nombre FROM producto pr " +
                " JOIN venta ve ON pr.id_producto = ve.id_producto " +
                " WHERE DATE_FORMAT(ve.Fecha, '%Y-%m') = DATE_FORMAT(v.Fecha, '%Y-%m') " +
                " GROUP BY pr.id_producto ORDER BY SUM(ve.cantidad) DESC LIMIT 1) as producto_mas_vendido " +
                "FROM venta v " +
                "JOIN pedido p ON v.id_pedido = p.id_pedido " +
                "WHERE DATE_FORMAT(v.Fecha, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m') " +
                "GROUP BY DATE_FORMAT(v.Fecha, '%Y-%m')";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                Object[] fila = {
                        rs.getString("mes"),
                        rs.getDouble("total_ventas"),
                        rs.getInt("num_pedidos"),
                        rs.getDouble("promedio_diario"),
                        rs.getString("semana_max_ventas"),
                        rs.getString("producto_mas_vendido")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al generar reporte mensual: " + e.getMessage());
        }

        return modelo;
    }
}