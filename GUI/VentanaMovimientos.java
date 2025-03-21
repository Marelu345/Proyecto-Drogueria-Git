package GUI;


import Clases.Movimiento;
import DAO.MovimientoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaMovimientos extends JFrame {
    public JPanel main;
    public JTable table1;
    private JButton eliminarButton;
    private MovimientoDAO movimientoDAO;

    public VentanaMovimientos() {
        movimientoDAO = new MovimientoDAO();




        eliminarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMovimiento();



            }
        });
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

    public void eliminarMovimiento() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un movimiento para eliminar.");
            return;
        }

        int id = (int) table1.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro que desea eliminar este movimiento?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (MovimientoDAO.eliminarMovimiento(id)) {
                JOptionPane.showMessageDialog(null, "Movimiento eliminado correctamente.");
                cargarMovimientos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el movimiento.");
            }
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

