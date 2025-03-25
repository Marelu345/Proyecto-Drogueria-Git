package GUI;

import javax.swing.*;

public class VentanaVenta {
    private JComboBox comboBox1;
    private JButton pagarButton;
    private JLabel labelCliente;
    private JLabel labelSubtotal;
    private JLabel labeliva;
    private JLabel labelPrecio;
    private JPanel main;


    public static void main() {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(new VentanaVenta().main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setVisible(true);

    }
}
