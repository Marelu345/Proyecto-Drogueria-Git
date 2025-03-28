package GUI;

import Clases.Pedido;
import DAO.PedidoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class PedidoAdmin {
    private JTable table;
    private JButton button2;
    private JPanel main;
    private JButton eliminarEstadoButton;
    private JButton enviarServerButton;
    private JTextField textField1;
    private JTextArea AreaServer;
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructor de la clase PedidoAdmin, pues aquí se llama al metodo para obtener los pedidos.
     * También se asigna la acción al botón para cambiar el estado de un pedido seleccionado en la tabla.
     */
    public PedidoAdmin() {
        obtenerDatos();
        iniciarServidor();
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
                    obtenerDatos();

                    String nuevoEstado = pedidoDAO.obtenerEstado(id_pedido);

                    enviarMensajeCliente("Su pedido #" + id_pedido + " ha sido: " + nuevoEstado);

                    JOptionPane.showMessageDialog(null, "Estado cambiado correctamente.");
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
        enviarServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (out != null && !textField1.getText().isEmpty()) {
                    String sendMessage = textField1.getText();
                    out.println(sendMessage);
                    AreaServer.append("Servidor: " + sendMessage + "\n");
                    textField1.setText("");
                    if (sendMessage.equalsIgnoreCase("salir")) {
                        cerrarConnections();
                    }
                }
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




    /**
     * Método para eliminar un pedido seleccionado de la tabla.
     * Verifica si hay un pedido seleccionado antes de proceder con la eliminación.
     * Muestra mensajes al usuario según el resultado del proceso.
     */
    public void eliminarPedido() {

        try {
            /**
             * Pues aquí obtenemos la fila que el usuario seleccionó en la tabla.
             * Si no seleccionó nada, devuelve -1.
             */
            int filaSeleccionada = table.getSelectedRow();
            /**
             * Entonces verificamos si no hay ninguna fila seleccionada.
             * Si es así, mostramos un mensaje diciendo que debe seleccionar un pedido.
             */
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un pedido para eliminar.");
                return;
            }
            /**
             * Aquí obtenemos el ID del pedido que está en la fila seleccionada.
             * Como el ID está en la primera columna (índice 0), lo tomamos de ahí.
             */

            int id_pedido = (int) table.getValueAt(filaSeleccionada, 0);
            /**
             * Pues entonces llamamos al método eliminar del pedidoDAO.
             * Esto intenta eliminar el pedido de la base de datos.
             */
            boolean eliminado = pedidoDAO.eliminar(id_pedido);

            /**
             * Aquí verificamos si el pedido se eliminó o no.
             * Si se eliminó, mostramos un mensaje de éxito y actualizamos la tabla.
             * Si no, mostramos un mensaje diciendo que hubo un error.
             */

            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Pedido eliminado correctamente.");
                obtenerDatos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el pedido.");
            }

            /**
             * Si pasa un error inesperado, mostramos un mensaje de error al usuario.
             * También imprimimos el error en la consola para que se pueda revisar qué pasó.
             */

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public void iniciarServidor() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(12345);
                AreaServer.append("Servidor iniciado. Esperando cliente...\n");
                clientSocket = serverSocket.accept();
                AreaServer.append("Cliente conectado.\n");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                String receiveMessage;
                while ((receiveMessage = in.readLine()) != null) {
                    if (receiveMessage.equalsIgnoreCase("salir")) {
                        AreaServer.append("Cliente ha salido del chat.\n");
                        break;
                    }
                    AreaServer.append("Cliente: " + receiveMessage + "\n");
                }
            } catch (IOException e) {
                AreaServer.append("Error en el servidor: " + e.getMessage() + "\n");
            } finally {
                cerrarConnections();
            }
        }).start();
    }
    public void enviarMensajeCliente(String mensaje) {
        if (out != null) {
            out.println(mensaje);
            AreaServer.append("Notificación enviada al cliente: " + mensaje + "\n");
        } else {
            AreaServer.append("No hay clientes conectados para recibir la notificación.\n");
        }
    }
    public void cerrarConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
            AreaServer.append("Conexiones cerradas.\n");
        } catch (IOException e) {
            AreaServer.append("Error al cerrar conexiones: " + e.getMessage() + "\n");
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(new PedidoAdmin().main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setVisible(true);

    }

}
