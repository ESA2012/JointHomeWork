package com.logistic.impl.visual;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.post.PostOffice;
import com.logistic.api.model.transport.Transit;
import com.logistic.impl.model.transport.DeliveryTransportImproved;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by SnakE on 06.11.2015.
 */
public class Graph extends JPanel{
    private List<PostOffice> postOffices;
    private List<DeliveryTransportImproved> deliveries;
    private List<Transit> transits;
    private Transit transit;
    private Package aPackage;
    private PostOfficeInfo officeInfo = PostOfficeInfo.INDEX;
    private boolean directions = false;
    private boolean showAllTransits = true;


    private PostOffice lastKnownPostOffice;
    public void setLastKnownPostOffice(PostOffice lastKnownPostOffice) {
        this.lastKnownPostOffice = lastKnownPostOffice;
    }

    public void setShowAllTransits(boolean show) {
        showAllTransits = show;
    }


    public void setDirections(boolean d) {
        directions = d;
        this.repaint();
    }


    public void setOfficeInfo(PostOfficeInfo i) {
        officeInfo = i;
        this.repaint();
    }


    public void setPackage(Package aPackage) {
        this.aPackage = aPackage;
    }


    public void setPostOffices (List<PostOffice> n, List<DeliveryTransportImproved> e) {
        postOffices = n;
        deliveries = e;
        this.repaint();
    }

    public void setTransits (List<Transit> t) {
        transits = t;
    }

    public void setTransit(Transit transit) {
        this.transit = transit;
    }


    public Graph(Dimension dimension) {
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        this.setBackground(Color.white);
    }



    private void drawNode(Graphics2D g2d, PostOffice postOffice, Color color) {
        drawNode(g2d, postOffice, color, Color.WHITE, officeInfo);
    }



    private void drawNodes(Graphics2D g2d, final List<PostOffice> postOfficeList) {
        PostOffice a = null;
        PostOffice b = null;
        if (transit != null) {
            a = transit.getTransitOffices().get(0);
            b = transit.getTransitOffices().get(transit.getTransitOffices().size()-1);
        }
        for(PostOffice p: postOfficeList) {
            Color nodeColor = Color.DARK_GRAY;
            Color textColor = Color.YELLOW;
            if (p == a) {
                nodeColor = new Color(0,0,200);
                textColor = Color.WHITE;
            } else {
                if (p == b) {
                    nodeColor = new Color(0, 150, 0);
                    textColor = Color.WHITE;
                }
            }
            drawNode(g2d, p, nodeColor, textColor, officeInfo);
        }
    }

    private void drawNode(Graphics2D g2d, PostOffice postOffice, Color nodeColor, Color textColor, PostOfficeInfo info) {
        Point point = postOffice.getGeolocation();
        g2d.setColor(nodeColor);
        g2d.fillOval(point.x - 15, point.y - 15, 30, 30);

        switch (info) {
            case INDEX: drawIndex(g2d, postOffice, textColor);
                break;
            case PACKAGE: drawPTypes(g2d, postOffice);
                break;
        }
    }


    public enum PostOfficeInfo {INDEX, PACKAGE}


    private void drawPTypes (Graphics2D g2d, PostOffice postOffice) {
        Point point = postOffice.getGeolocation();
        g2d.setFont(new Font("Liberation Sans", 0, 8));
        g2d.setColor(new Color(220, 150, 0));
        int yshift = -15;
        for (Package.Type pt: postOffice.getAcceptablePackageTypes()) {
            g2d.drawString(pt.toString(), point.x-9, point.y+(yshift+=10));
        }
    }



    private void drawIndex(Graphics2D g2d, PostOffice postOffice, Color textColor) {
        Point point = postOffice.getGeolocation();
        g2d.setFont(new Font("Liberation Sans", 0, 9));
        g2d.setColor(textColor);
        String index = String.valueOf(postOffice.getAddress().getCode());
        g2d.drawString(index, point.x - 12, point.y + 4);
    }


    private void drawEdges(Graphics2D g2d, List<DeliveryTransportImproved> links, boolean drawDirection) {
        Color[] colors = {
                new Color(120, 160, 200),
                new Color(150, 200, 200),
                new Color(150, 200, 150),
                new Color(200, 150, 150)
        };
        for (DeliveryTransportImproved d: links) {
            Point point1 = d.getStartPostOffice().getGeolocation();
            Point point2 = d.getDestinationPostOffice().getGeolocation();
            g2d.setColor(colors[d.getType().ordinal()]);

            if (drawDirection) {
                g2d.draw(new Line2D.Double(point1, point2));
                Point middle = new Point();
                middle.setLocation((point1.x + point2.x) / 2d, (point1.y + point2.y) / 2d);
                drawArrowHead(g2d, middle, point1);
            } else {
                g2d.drawLine(point1.x, point1.y, point2.x, point2.y);
            }
        }
    }



    private void drawArrowHead(Graphics2D g2, Point tip, Point tail)
    {
        double phi = Math.toRadians(30);
        int barb = 10;
        double dy = tip.y - tail.y;
        double dx = tip.x - tail.x;
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + phi;
        for(int j = 0; j < 2; j++)
        {
            x = tip.x - barb * Math.cos(rho);
            y = tip.y - barb * Math.sin(rho);
            g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
            rho = theta - phi;
        }
    }



    private void drawWay(Graphics2D g2d, Transit transit, Color color, int interval, int thickness, float[] dash, float offset) {
        BasicStroke path = new BasicStroke(
                thickness,
                BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_BEVEL,
                interval,
                dash,
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


    private void drawWays(Graphics2D g2d, final List<Transit> transits) {
        int thickness = 6;
        Color[] c = {
                new Color(0, 250, 0),
                new Color(100, 200, 200),
                new Color(0, 100, 200),
                new Color(150, 250, 150),
                new Color(150, 200, 0),
                new Color(150, 100, 250)};
        int i = 0;
        float offset = 0;
        for (Transit t: transits) {
            drawWay(g2d, t, c[i++], 10, thickness, new float[] {15,20}, offset += 5);
        }
    }


    private void drawPostOffices(Graphics2D g) {
        if (postOffices != null) {
            drawNodes(g, postOffices);
        }
    }


    private void drawDeliveries(Graphics2D g) {
        if (deliveries != null) {
            drawEdges(g, deliveries, directions);
        }
    }



    private void drawTransit(Graphics2D g) {
        if (transit != null) {
            drawWay(g, transit, Color.RED, 0, 3, new float[] {5,0}, 0);
        }
    }


    private void drawTransits(Graphics2D g) {
        if (transits != null) {
            if (showAllTransits) {
               drawWays(g, transits);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawTransits(g2d);
        drawDeliveries(g2d);
        drawTransit(g2d);
        drawPostOffices(g2d);
        if (lastKnownPostOffice != null) {
            drawNode(g2d, lastKnownPostOffice, Color.MAGENTA);
        }
    }



    public boolean saveGraphImage(String filename) {
        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        paintComponent(g);
        try {
            File outF = new File(filename);
            ImageIO.write(img, "PNG", outF);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
