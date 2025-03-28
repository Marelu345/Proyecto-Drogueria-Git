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
    private JButton buttonCaja;
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

        buttonCaja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaCaja ventanaCaja = new VentanaCaja();
                ventanaCaja.main(null);
            }
        });
        /**
         * Agrega un ActionListener al boton de actualizar.
         * Cuando el usuario presiona el botón, se ejecuta el método actualizarMovimiento().
         */
        actualizarButton.addActionListener(new ActionListener() {
            /**
             * Método que se ejecuta cuando se hace clic en el botón de actualizar.
             * Llama a actualizarMovimiento() para aplicar los cambios.
             *
             * @param e Evento de acción generado por el botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarMovimiento();

            }
        });


        /**
         * Agrega un ActionListener al botón de eliminar.
         * Cuando el usuario presiona el botón, se ejecuta el método eliminarMovimiento().
         */

        eliminarButton.addActionListener(new ActionListener() {

            /**
             * Método que se ejecuta cuando se hace clic en el botón de eliminar.
             * Llama a eliminarMovimiento() para borrar el movimiento seleccionado.
             *
             * @param e Evento de acción generado por el botón.
             */

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
            monto = Double.parseDouble(textField1.getText().trim());
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.");
                return;
            }
            Movimiento movimiento = new Movimiento(0, tipo, categoria, monto, null);

            if (movimientoDAO.registrarMovimiento(movimiento)) {
                JOptionPane.showMessageDialog(this, "Movimiento registrado exitosamente.");
                cargarMovimientos();
            }else {
                JOptionPane.showMessageDialog(this, "Error al registrar el movimiento.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido.");
            return;
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
        /**
         * Se obtiene la fila seleccionada de la tabla.
         * Si no se selecciona ninguna, se muestra un mensaje de advertencia.
         */
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un movimiento para actualizar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        /**
         * Se obtiene el ID del movimiento a partir de la fila seleccionada.
         */
        int id = (int) table1.getValueAt(filaSeleccionada, 0);

        /**
         * Se obtiene el monto ingresado en el campo de texto y se verifica que no esté vacío.
         */
        String montoTexto = textField1.getText().trim();
        if (montoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        /**
         * Se convierte el monto de texto a un valor numérico.
         */
        double monto = Double.parseDouble(montoTexto);

        /**
         * Se obtiene el tipo de movimiento y la categoría desde los ComboBox.
         */
        String tipo = (String) comboBox1.getSelectedItem();
        String categoria = (String) comboBox2.getSelectedItem();

        /**
         * Se crea un objeto Movimiento con los datos actualizados.
         */
        Movimiento movimiento = new Movimiento(id, tipo, categoria, monto, new java.util.Date());

        /**
         * Se envía el objeto Movimiento a la base de datos para actualizarlo.
         * Si la actualización es exitosa, se muestra un mensaje y se recarga la tabla.
         */
        if (MovimientoDAO.actualizarMovimiento(movimiento)) {
            JOptionPane.showMessageDialog(null, "Movimiento actualizado correctamente.");
            cargarMovimientos(); // Recarga la tabla
            CajaDAO.actualizarSaldoAutomatico(movimiento.getMonto(), movimiento.getTipo());
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar el movimiento.");
        }
    }



    /**
     * Método para eliminar un movimiento seleccionado de la tabla.
     * Primero verifica si se ha seleccionado una fila, luego obtiene el ID
     * del movimiento y lo envía a la base de datos para eliminarlo.
     */

    public void eliminarMovimiento() {

        /**
         * Se obtiene la fila seleccionada de la tabla.
         * Si no se selecciona ninguna, se muestra un mensaje de advertencia.
         */
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un movimiento para eliminar.");
            return;
        }

        /**
         * Se obtiene el ID del movimiento a partir de la fila seleccionada.
         */

        int id = (int) table1.getValueAt(filaSeleccionada, 0);

        /**
         * Se envía el ID del movimiento a la base de datos para eliminarlo.
         * Si la eliminación es exitosa, se muestra un mensaje y se recarga la tabla.
         */

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

