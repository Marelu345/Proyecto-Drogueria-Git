package GUI;
import DAO.ClienteDAO;
import Clases.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaClientes {
    public JTable table1;
    public JTextField textField1, textField2, textField3, textField4;
    public JButton button1;
    private JPanel main;
    public ClienteDAO clienteDAO = new ClienteDAO();

    public VentanaClientes() {
        obtenerClientes();

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarCliente();
                obtenerClientes();
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

    public void ejecutar() {
        JFrame frame = new JFrame("Clientes");
        frame.setContentPane(this.main);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
