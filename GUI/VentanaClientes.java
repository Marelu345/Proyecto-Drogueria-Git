package GUI;
import DAO.ClienteDAO;
import Clases.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaClientes {
    private JTable table1;
    private JTextField textField1, textField2, textField3, textField4;
    private JButton button1;
    private JPanel main;
    private JButton button2;
    private JButton button3;
    private ClienteDAO clienteDAO = new ClienteDAO();
    int filaSeleccionada;

    public VentanaClientes() {
        obtenerClientes();

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarCliente();
                obtenerClientes();
            }
        });
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarCliente();
                obtenerClientes();
            }

        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();

            }


        });


        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Correo");
        modelo.addColumn("Teléfono");

        table1.setModel(modelo);

        List<Cliente> clientes = clienteDAO.obtenerClientes();
        if (!clientes.isEmpty()) {
            Cliente cliente = clientes.get(0);
            modelo.addRow(new Object[]{cliente.getId(), cliente.getCedula(), cliente.getNombre(), cliente.getCorreo(), cliente.getTelefono()});
            clientes.remove(0);
        }
    }

    public void agregarCliente() {
        Cliente nuevoCliente = new Cliente(0, textField2.getText(), textField1.getText(), textField3.getText(), textField4.getText());
        if (clienteDAO.agregarCliente(nuevoCliente)) {
            JOptionPane.showMessageDialog(null, "Cliente agregado.");
        }
    }


    public void actualizarCliente() {
        if (filaSeleccionada >= 0) {
            int id = (int) table1.getValueAt(filaSeleccionada, 0);
            Cliente clienteActualizado = new Cliente(id, textField2.getText(), textField1.getText(), textField3.getText(), textField4.getText());
            if (clienteDAO.actualizarCliente(clienteActualizado)) {
                JOptionPane.showMessageDialog(null, "Cliente actualizado.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente de la tabla.");
        }
    }
    public void eliminarCliente() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente para eliminar.");
            return;
        }

        String id = table1.getValueAt(selectedRow, 0).toString();
        if (clienteDAO.eliminarCliente(id)) {
            JOptionPane.showMessageDialog(null, "Cliente eliminado exitosamente.");
            obtenerClientes();
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente.");
        }
    }






    public void ejecutar() {
        JFrame frame = new JFrame("Clientes");
        frame.setContentPane(this.main);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
