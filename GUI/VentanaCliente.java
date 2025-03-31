package GUI;

import Clases.Cliente;
import DAO.ClienteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class VentanaCliente {
    private JPanel main;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton button1;
    private JButton button2;
    private JButton button4;
    private ClienteDAO clienteDAO = new ClienteDAO();
    int filaSeleccionada;

    public VentanaCliente() {
        obtenerClientes();

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarCliente();
                mostrarUltimoCliente();
            }
        });





        //button3.addActionListener(e -> { InicioPrincipal.main(null);});


        table1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                filaSeleccionada = table1.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    textField1.setText(table1.getValueAt(filaSeleccionada, 1).toString());
                    textField2.setText(table1.getValueAt(filaSeleccionada, 2).toString());
                    textField3.setText(table1.getValueAt(filaSeleccionada, 3).toString());
                    textField4.setText(table1.getValueAt(filaSeleccionada, 4).toString());
                }
            }
        });
    }

    public void obtenerClientes() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Cedula");
        modelo.addColumn("Correo");
        modelo.addColumn("Teléfono");

        table1.setModel(modelo);

    }
    public void mostrarUltimoCliente() {
        Cliente cliente = clienteDAO.obtenerUltimoCliente();
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();

        modelo.setRowCount(0);

        if (cliente != null) {
            modelo.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getCedula(),
                    cliente.getEmail(),
                    cliente.getTelefono()
            });
        }
    }


    public void agregarCliente() {
        Cliente nuevoCliente = new Cliente(0, textField1.getText(), textField2.getText(), textField3.getText(), textField4.getText());
        if (clienteDAO.agregarCliente(nuevoCliente)) {
            JOptionPane.showMessageDialog(null, "Registro Exitoso.");
            mostrarUltimoCliente();
        }

    }
    public static void main(String[] args) {
        VentanaCliente ventana = new VentanaCliente();
        JFrame frame = new JFrame("Registro de CLiente");
        frame.setContentPane(ventana.main);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setResizable(true);
        frame.setVisible(true);
        ventana.obtenerClientes();
    }
}

