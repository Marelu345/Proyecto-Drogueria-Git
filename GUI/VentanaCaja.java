package GUI;

import DAO.CajaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaCaja {
    private JPanel main;
    private JTable table1;
    private CajaDAO cajaDAO;

    public VentanaCaja() {
        cajaDAO = new CajaDAO();
        cargarSaldo();
    }

    public void cargarSaldo() {
        Object[] datos = cajaDAO.obtenerSaldo();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Caja");
        modelo.addColumn("Saldo Actual");
        modelo.addRow(datos);
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
