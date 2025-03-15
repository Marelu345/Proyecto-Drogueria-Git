package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal {
    public JButton button1;
    public JButton button2;
    private JPanel main;

    public VentanaPrincipal() {
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaClientes ventanaClientes = new VentanaClientes();
                ventanaClientes.ejecutar();
            }
        });


        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public void ejecutar() {
        JFrame frame = new JFrame("Sistema de Farmacia");
        frame.setContentPane(this.main);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.ejecutar();
    }
}
