package com.logistic.impl.visual;

import com.logistic.api.model.post.PostOffice;
import com.logistic.impl.service.DataStorage;
import com.logistic.impl.service.RouteMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

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

    public void drawNodes(final List<PostOffice> postOfficeList) {
        for(PostOffice p: postOfficeList) {
            Point point = p.getGeolocation();
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(point.x - 15, point.y - 15, 30, 30);

            g2d.setFont(new Font("Liberation Sans", 0, 9));
            g2d.setColor(Color.CYAN);
            g2d.drawString(String.valueOf(p.getAddress().getCode()), point.x - 12, point.y + 4);
        }
    }


    public void drawRoutes(final RouteMatrix matrix, final List<PostOffice> postOfficeList) {
        g2d.setColor(Color.GRAY);
        boolean[][] m = matrix.getMatrix();
        int size = m.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (m[i][j]) {
                    Point point = postOfficeList.get(i).getGeolocation();
                    Point point2 = postOfficeList.get(j).getGeolocation();
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
