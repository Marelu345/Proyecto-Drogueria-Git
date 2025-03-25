package GUI;

import Clases.Pedido;
import DAO.PedidoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PedidoAdmin {
    private JTable table;
    private JButton button2;
    private JPanel main;
    private JButton eliminarEstadoButton;
    private PedidoDAO pedidoDAO = new PedidoDAO();


    /**
     * Constructor de la clase PedidoAdmin, pues aquí se llama al metodo para obtener los pedidos.
     * También se asigna la acción al botón para cambiar el estado de un pedido seleccionado en la tabla.
     */
    public PedidoAdmin() {
        obtenerDatos();
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Se obtiene la fila que seleccionó el usuario.
                 * Si no seleccionó nada, pues muestra un mensaje y sale.
                 */
                int filaSeleccionada = table.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona un pedido para cambiar su estado.");
                    return;
                }

                /**
                 * Se obtiene el ID del pedido y su estado actual desde la tabla.
                 */
                int id_pedido = (int) table.getValueAt(filaSeleccionada, 0);
                String estadoActual = (String) table.getValueAt(filaSeleccionada, 4);

                /**
                 * Si el pedido ya está en "Entregado", pues ya no se puede cambiar.
                 */
                if ("Entregado".equals(estadoActual)) {
                    JOptionPane.showMessageDialog(null, "El pedido ya fue entregado y no puede cambiar de estado.");
                    return;
                }

                /**
                 * Se llama al metodo que se encarga de cambiar el estado en la base de datos.lo que hace es revisar el estado actual y lo actualiza si es posible.
                 */
                boolean actualizado = pedidoDAO.cambiarEstado(id_pedido, estadoActual);

                /**
                 * Si se actualizó correctamente, se vuelve a cargar la tabla
                 * para que se vea reflejado el cambio.
                 */
                if (actualizado) {
                    JOptionPane.showMessageDialog(null, "Estado cambiado correctamente.");
                    obtenerDatos();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al cambiar el estado.");
                }
            }
        });
        eliminarEstadoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPedido();

            }
        });
    }

    /**
     * Pues Aquí es para obtener como tal los pedidos de la base de datos y mostrarlos en la tabla.
     * Se usa un modelo de tabla (`DefaultTableModel`) para cargar los respectivos datos.
     */
    public void obtenerDatos() {
        List<Pedido> pedidos = pedidoDAO.obtenerPedidos();
        DefaultTableModel modelo = new DefaultTableModel();

        /**
         * Aquí se agregan las columnas que va a tener la tabla para mostrar los pedidos.
         * Cada columna representa un dato importante del pedido, como su ID, el total, el estado, etc.
         */
        modelo.addColumn("Id_pedido");
        modelo.addColumn("Id_venta");
        modelo.addColumn("subtotal");
        modelo.addColumn("Precio Total");
        modelo.addColumn("Estado");
        modelo.addColumn("Tipo de Unidad");


        /**
         * Se asigna el modelo a la tabla para que tenga las columnas y pueda mostrar los datos.
         * Sin esto, la tabla no sabría qué información debe contener.
         */
        table.setModel(modelo);

        /**
         * Se recorren los pedidos y se agregan uno por uno a la tabla.
         */
        while (!pedidos.isEmpty()) {
            Pedido pedido = pedidos.remove(0);
            modelo.addRow(new Object[]{pedido.getId_pedido(), pedido.getId_venta(), pedido.getSubtotal(), pedido.getTotal(), pedido.getEstado(),pedido.getTipoU()});
        }
    }

    public void eliminarPedido() {
        try {
            int filaSeleccionada = table.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un pedido para eliminar.");
                return;
            }

            int id_pedido = (int) table.getValueAt(filaSeleccionada, 0);
            boolean eliminado = pedidoDAO.eliminar(id_pedido);

            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Pedido eliminado correctamente.");
                obtenerDatos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el pedido.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(new PedidoAdmin().main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(true);
        frame.setVisible(true);

    }

}
