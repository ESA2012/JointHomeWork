package com.logistic.impl.visual;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.model.post.PostOfficeImproved;
import com.logistic.impl.model.transport.DeliveryTransportImpl;
import com.logistic.impl.model.transport.DeliveryTransportImproved;
import com.logistic.impl.model.transport.TransitImproved;
import com.logistic.impl.service.esa.routes.RouteMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public void drawNode(PostOfficeImproved postOffice, Color color) {
        drawNode(postOffice, color, Color.WHITE);
    }

    public void drawNodes(final List<PostOfficeImproved> postOfficeList) {
        for(PostOfficeImproved p: postOfficeList) {
            drawNode(p, Color.DARK_GRAY, Color.YELLOW);
        }
    }

    private void drawNode(PostOfficeImproved postOffice, Color nodeColor, Color textColor) {
        Point point = postOffice.getGeolocation();
        g2d.setColor(nodeColor);
        g2d.fillOval(point.x - 15, point.y - 15, 30, 30);
        drawIndex(postOffice, textColor);
        //drawPTypes(postOffice);
    }

    private void drawPTypes (PostOfficeImproved postOffice) {
        Point point = postOffice.getGeolocation();
        g2d.setFont(new Font("Liberation Sans", 0, 8));
        g2d.setColor(new Color(220, 150, 0));
        int yshift = -15;
        for (Package.Type pt: postOffice.getAcceptablePackageTypes()) {
            g2d.drawString(pt.toString(), point.x-9, point.y+(yshift+=10));
        }
    }

    private void drawIndex(PostOfficeImproved postOffice, Color textColor) {
        Point point = postOffice.getGeolocation();
        g2d.setFont(new Font("Liberation Sans", 0, 9));
        g2d.setColor(textColor);
        String index = String.valueOf(postOffice.getAddress().getCode());
        g2d.drawString(index, point.x - 12, point.y + 4);
    }


    public void drawRoutes(List<DeliveryTransportImproved> links) {
        Color[] colors = {
                new Color(0, 100, 250),
                new Color(0, 200, 250),
                new Color(150, 160, 150),
                new Color(200, 100, 200)
        };
        for (DeliveryTransportImproved d: links) {
            Point point1 = d.getStartPostOffice().getGeolocation();
            Point point2 = d.getDestinationPostOffice().getGeolocation();
            g2d.setColor(colors[d.getType().ordinal()]);
            g2d.drawLine(point1.x, point1.y, point2.x, point2.y);
        }
    }





    public void drawTransit(Transit transit, Color color, int thickness, float offset) {
        if (transit == null) {
            return;
        }
        if (transit.getTransitOffices().size()<2) {
            return;
        }
        BasicStroke path = new BasicStroke(
                thickness,
                BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_BEVEL,
                10,
                new float[] {15,20},
                offset);
        g2d.setColor(color);
        g2d.setStroke(path);
        for (int i = 0; i < transit.getTransitOffices().size() - 1; i++) {
            Point p1 = transit.getTransitOffices().get(i).getGeolocation();
            Point p2 = transit.getTransitOffices().get(i+1).getGeolocation();
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        g2d.setStroke(new BasicStroke(1));
    }


    public void drawTransits(final List<TransitImproved> transits) {
        if (transits == null) return;
        int thickness = 6;
        Color[] c = {
                new Color(100, 250, 100),
                new Color(100, 200, 200),
                new Color(0, 100, 200),
                new Color(150, 250, 150),
                new Color(150, 200, 0),
                new Color(150, 100, 250)};
        int i = 0;
        float offset = 0;
        for (Transit t: transits) {
            drawTransit(t, c[i++], thickness, offset += 5);
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
