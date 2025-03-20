package GUI;

import DAO.CajaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaCaja {
    public JPanel main;
    public JTable table1;
    private JButton button1;
    private CajaDAO cajaDAO;

    public VentanaCaja() {
        cajaDAO = new CajaDAO();
        button1.addActionListener(e -> ((JFrame) SwingUtilities.getWindowAncestor(main)).dispose());
        cargarSaldo();
    }

    public void cargarSaldo() {
        double saldo = cajaDAO.obtenerSaldo();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Caja");
        modelo.addColumn("Saldo Actual");
        modelo.addRow(new Object[]{1, saldo});
        table1.setModel(modelo);
    }

    public static void main(String[] args) {
        VentanaCaja ventana = new VentanaCaja();
        JFrame frame = new JFrame("Caja");
        frame.setContentPane(ventana.main);
       // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);
        frame.setResizable(true);
        frame.setVisible(true);

    }
}
