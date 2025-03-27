package Imagenes;

import javax.swing.*;
import java.awt.*;

public class Imagenes extends JLabel {

    private int x, y;
    private final String path;

    public Imagenes (JPanel panel, String path) {
        this.path = path;
        this.x = panel.getWidth();
        this.y = panel.getHeight();
        this.setSize(x, y);
    }
    @Override
    public void paint (Graphics g){
        ImageIcon img = new ImageIcon (getClass().getResource (path));
        g.drawImage (img.getImage(), 0,0,x,y,null);


    }
}
