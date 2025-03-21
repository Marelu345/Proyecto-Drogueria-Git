package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal {
    private JButton button1;
    private JButton button2;
    private JPanel main;
    private JButton button3;
    private JButton ventanaPedidoButton;
    private JButton ventanaMovimientoButton;

    public VentanaPrincipal() {
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaClientes ventanaClientes = new VentanaClientes();
                ventanaClientes.main(null);
            }
        });


        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaProducto ventanaProducto = new VentanaProducto();
                ventanaProducto.main(null);
            }
        });

        ventanaPedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaPedido ventanaPedido = new VentanaPedido();
                ventanaPedido.main();
            }
        });
        ventanaMovimientoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaMovimientos ventanaMovimientos = new VentanaMovimientos();
                ventanaMovimientos.main(null);


            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Farmacia");
        frame.setContentPane(new VentanaPrincipal().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 500);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
