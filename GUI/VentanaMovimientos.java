package GUI;


import Clases.Movimiento;
import DAO.MovimientoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaMovimientos extends JFrame {
    public JPanel main;
    public JTable table1;
    public JButton button1;
    private MovimientoDAO movimientoDAO;

    public VentanaMovimientos() {
        movimientoDAO = new MovimientoDAO();

        button1.addActionListener(e -> ((JFrame) SwingUtilities.getWindowAncestor(main)).dispose());

    }

    public void cargarMovimientos() {
        List<Movimiento> movimientos = movimientoDAO.obtenerMovimientos();

        if (movimientos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay movimientos registrados.");
            return;
        }

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Tipo");
        modelo.addColumn("Categoría");
        modelo.addColumn("Monto");
        modelo.addColumn("Fecha");

        table1.setModel(modelo);

        while (!movimientos.isEmpty()) {
            Movimiento movimiento = movimientos.remove(0);
            modelo.addRow(new Object[]{movimiento.getId(), movimiento.getTipo(), movimiento.getCategoria(), movimiento.getMonto(), movimiento.getFecha()});
        }

    }

    public static void main(String[] args) {
        VentanaMovimientos ventana = new VentanaMovimientos();
        JFrame frame = new JFrame("Movimientos");
        frame.setContentPane(ventana.main);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setResizable(true);
        frame.setVisible(true);
        ventana.cargarMovimientos();
    }
}

