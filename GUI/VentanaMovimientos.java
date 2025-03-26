package GUI;


import Clases.Movimiento;
import DAO.CajaDAO;
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
    private JButton actualizarButton;
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
        actualizarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarMovimiento();

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

    public void actualizarMovimiento() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un movimiento para actualizar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table1.getValueAt(filaSeleccionada, 0);
        String montoTexto = textField1.getText().trim();

        if (montoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double monto = Double.parseDouble(montoTexto);
        String tipo = (String) comboBox1.getSelectedItem();
        String categoria = (String) comboBox2.getSelectedItem();

        Movimiento movimiento = new Movimiento(id, tipo, categoria, monto, new java.util.Date());

        if (MovimientoDAO.actualizarMovimiento(movimiento)) {
            JOptionPane.showMessageDialog(null, "Movimiento actualizado correctamente.");
            cargarMovimientos(); // Recarga la tabla
            CajaDAO.actualizarSaldoAutomatico(movimiento.getMonto(), movimiento.getTipo());

        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar el movimiento.");
        }
    }



    public void eliminarMovimiento() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un movimiento para eliminar.");
            return;
        }

        int id = (int) table1.getValueAt(filaSeleccionada, 0);

        if (movimientoDAO.eliminarMovimiento(id)) {
            JOptionPane.showMessageDialog(null, "Movimiento eliminado exitosamente.");
            cargarMovimientos();
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar el movimiento.");
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

