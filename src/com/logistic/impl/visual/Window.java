package com.logistic.impl.visual;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;



/**
 * Created by SnakE on 06.11.2015.
 */
public class Window {
    private JFrame window;
    private Canvas canvas;

    public Window() {
        window = new JFrame("Граф");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas();
        window.add(canvas);
        window.setVisible(true);
    }

    private BufferedImage img;

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public void update() {
        canvas.repaint();
    }

    private class Canvas extends JComponent {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (img != null) {
                g2d.drawImage(img, 0,0, null);
            }
        }
    }


}
