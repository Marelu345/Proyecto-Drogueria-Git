package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioPrincipal {
    private JButton iniciarSesionButton;
    private JButton registrarseButton;
    private JButton ingresarButton;
    private JPanel main;

    public InicioPrincipal() {
        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI.main(null);
            }
        });
        registrarseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaCliente.main(null);
            }
        });
        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminGUI.main(null);
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Farmacia");
        frame.setContentPane(new InicioPrincipal().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 500);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
