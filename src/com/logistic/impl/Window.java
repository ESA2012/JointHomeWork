package com.logistic.impl;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;



/**
 * Created by SnakE on 06.11.2015.
 */
public class Window {

    private BufferedImage img;

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    Canvas canvas;

    public void show() {
        JFrame window = new JFrame("Граф");
        window.setSize(640, 480);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas();
        window.add(canvas);
        window.setVisible(true);
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
