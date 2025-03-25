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
    private JButton button2;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private MovimientoDAO movimientoDAO;

    public VentanaMovimientos() {
        movimientoDAO = new MovimientoDAO();
        cargarMovimientos();

        comboBox1.addItem("Ingreso");
        comboBox1.addItem("Egreso");

        comboBox2.addItem("Venta");
        comboBox2.addItem("Compra de insumos");

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarMovimiento();
            }

        });


        eliminarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMovimiento();

            }
        });
    }
    public void registrarMovimiento() {
        String tipo = comboBox1.getSelectedItem().toString();
        String categoria = comboBox2.getSelectedItem().toString();
        double monto = 0;

        try {
            monto = Double.parseDouble(textField1.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido.");
            return;
        }
        Movimiento movimiento = new Movimiento(0, tipo, categoria, monto, null);

        if (movimientoDAO.registrarMovimiento(movimiento)) {
            JOptionPane.showMessageDialog(this, "Movimiento registrado exitosamente.");
            cargarMovimientos();
        }else {
            JOptionPane.showMessageDialog(this, "Error al registrar el movimiento.");
        }

    }
    public void cargarMovimientos() {
        List<Movimiento> movimientos = movimientoDAO.obtenerMovimientos();
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
            if (movimientoDAO.eliminarMovimiento(id)) {
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

