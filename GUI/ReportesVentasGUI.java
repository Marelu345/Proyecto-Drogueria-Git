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
    private Color baseColor = new Color(80, 160, 220); // Color más claro (80,160,220)

    public ReportesVentasGUI() {
        conexionDB = new ConexionDB();
        initialize();
    }

    public void initialize() {
        frame = new JFrame("Reportes de Ventas");
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(baseColor);

        // Panel superior con controles
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelSuperior.setBackground(baseColor);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Componentes con estilo
        tipoReporteCombo = new JComboBox<>(new String[]{"Diario", "Semanal", "Mensual", "Stock Minimo"});
        estiloComponente(tipoReporteCombo);

        generarButton = new JButton("Generar Reporte");
        estiloBoton(generarButton);

        // Etiqueta con estilo
        JLabel etiqueta = new JLabel("Tipo de Reporte:");
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelSuperior.add(etiqueta);
        panelSuperior.add(tipoReporteCombo);
        panelSuperior.add(generarButton);

        // Tabla con estilo
        tablaReportes = new JTable();
        estiloTabla(tablaReportes);

        JScrollPane scrollPane = new JScrollPane(tablaReportes);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        frame.add(panelSuperior, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        generarButton.addActionListener(e -> generarReporte());

        frame.setVisible(true);
    }

    // Métodos de estilo reutilizables
    public void estiloComponente(JComponent componente) {
        componente.setBackground(Color.WHITE);
        componente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        componente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(baseColor.darker(), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    public void estiloBoton(JButton boton) {
        boton.setBackground(baseColor.brighter());
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        boton.setFocusPainted(false);

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(baseColor.brighter().brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(baseColor.brighter());
            }
        });
    }

    public void estiloTabla(JTable tabla) {
        tabla.setBackground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setGridColor(baseColor.darker());
        tabla.setSelectionBackground(baseColor.brighter());
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setRowHeight(25);

        // Encabezado de tabla
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(baseColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

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
        estiloTabla(tablaReportes); // Asegurar que se mantengan los estilos
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