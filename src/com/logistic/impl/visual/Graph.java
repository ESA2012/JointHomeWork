package com.logistic.impl.visual;

import com.logistic.api.model.post.PostOffice;
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

    public Graph(Rectangle rect) {
        int width = rect.width;
        int height = rect.height;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2d = img.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
    }

    public void drawNode(PostOffice postOffice, Color color) {
        drawNode(postOffice, color, Color.WHITE);
    }

    public void drawNodes(final List<PostOffice> postOfficeList) {
        for(PostOffice p: postOfficeList) {
            drawNode(p, Color.DARK_GRAY, Color.YELLOW);
        }
    }

    private void drawNode(PostOffice postOffice, Color nodeColor, Color textColor) {
        Point point = postOffice.getGeolocation();
        g2d.setColor(nodeColor);
        g2d.fillOval(point.x - 15, point.y - 15, 30, 30);
        g2d.setFont(new Font("Liberation Sans", 0, 9));
        g2d.setColor(textColor);
        String index = String.valueOf(postOffice.getAddress().getCode());
        g2d.drawString(index, point.x - 12, point.y + 4);
    }


    public void drawRoutes(final RouteMatrix matrix, final List<PostOffice> postOfficeList, Color color) {
        g2d.setColor(color);
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
