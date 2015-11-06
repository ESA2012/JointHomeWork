package com.logistic.impl.visual;

import com.logistic.api.model.post.PostOffice;
import com.logistic.impl.service.DataStorage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by SnakE on 06.11.2015.
 */
public class Graph {
    private BufferedImage img;
    private Graphics2D g2d;

    public Graph(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2d = img.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
    }

    public void drawNodes() {
        g2d.setColor(Color.DARK_GRAY);
        for(PostOffice p: DataStorage.getPostOffices()) {
            Point point = p.getGeolocation();
            g2d.fillOval(point.x - 10, point.y - 10, 20, 20);
            g2d.setFont(new Font("Arial Narrow", 0, 10));
            g2d.drawString(String.valueOf(p.getAddress().getCode()), point.x - 10, point.y + 20);
        }
    }


    public void drawRoutes() {
        g2d.setColor(Color.GRAY);
        boolean[][] m = DataStorage.getRouteMatrix().getMatrix();
        int size = m.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (m[i][j]) {
                    Point point = DataStorage.getPostOffices().get(i).getGeolocation();
                    Point point2 = DataStorage.getPostOffices().get(j).getGeolocation();
                    g2d.drawLine(point.x, point.y, point2.x, point2.y);
                }
            }
        }

    }

    public BufferedImage getImage() {
        return img;
    }

    public boolean saveGraphImage(String filename) {
        try {
            File outF = new File(filename);
            ImageIO.write(img, "PNG", outF);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
