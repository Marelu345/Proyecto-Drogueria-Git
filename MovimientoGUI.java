import javax.swing.*;

public void actualizarMovimiento() {
    int filaSeleccionada = table1.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un movimiento para actualizar.");
        return;
    }

    int id = (int) table1.getValueAt(filaSeleccionada, 0);
    String tipo = tipoField.getText();
    String categoria = categoriaField.getText();
    double monto = Double.parseDouble(montoField.getText());

    Movimiento movimiento = new Movimiento(id, tipo, categoria, monto, new java.util.Date());

    if (movimientosDAO.actualizarMovimiento(movimiento)) {
        JOptionPane.showMessageDialog(null, "Movimiento actualizado correctamente.");
        cargarMovimientos();
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

    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro que desea eliminar este movimiento?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirmacion == JOptionPane.YES_OPTION) {
        if (movimientosDAO.eliminarMovimiento(id)) {
            JOptionPane.showMessageDialog(null, "Movimiento eliminado correctamente.");
            cargarMovimientos();
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar el movimiento.");
        }
    }
}