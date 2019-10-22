package MapApp.Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.Point;


import javax.swing.JFrame;

public class DrawPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private ArrayList<Point> ContextPoints = new ArrayList<>();
    private ArrayList<Point> DestinationPoints = new ArrayList<>();;
    private ArrayList<ArrayList<Point>> edges = new ArrayList<>();;
    private int status = 0;

    public DrawPanel() {
         ArrayList<Point> pair = new ArrayList<>();
        int tempx = 0;
        int tempy = 0;
        ContextPoints = new ArrayList<Point>();
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    status++;
                    if (status== 3)
                        status = 0;

                }else if (status == 0) {
                    ContextPoints.add(new Point(e.getX(), e.getY()));

                } else if (status == 1) {
                    DestinationPoints.add(new Point(e.getX(), e.getY()));

                } else if (status == 2) {

                    if (null != getClosestPoint(e.getX(), e.getY())) {
                        pair.add(getClosestPoint(e.getX(), e.getY()));

                    }
                    if (pair.size() == 2) {
                        edges.add(new ArrayList<>(pair));

                        pair.clear();

                    }
                }
                repaint();
            }
        });
    }

    private Point getClosestPoint(int x, int y) {
        for (Point point : ContextPoints)
            if (Math.abs(x - point.x) < 10 && Math.abs(y - point.y) < 10)
                return point;

        for (Point point : DestinationPoints)
            if (Math.abs(x - point.x) < 10 && Math.abs(y - point.y) < 10)
                return point;

        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.blue);
        try {
            BufferedImage image = ImageIO.read(new File("src/MapApp/Assets/test.png"));
            g.drawImage(image, 0, 0,getWidth(), getHeight(), null);
            for (Point point : ContextPoints) {
                g2.fillOval(point.x-5, point.y-9, 10, 10);
            }
            g2.setColor(Color.red);
            for (Point point : DestinationPoints) {
                g2.fillOval(point.x-5, point.y-9, 10, 10);
            }
            g2.setColor(Color.green);
            g2.setStroke(new BasicStroke(4));

            for (int i = 0; i < edges.size(); i++) {
                int x1 = (int)edges.get(i).get(0).getX();
                int y1 = edges.get(i).get(0).y -3;
                int x2 = edges.get(i).get(1).x;
                int y2 = edges.get(i).get(1).y -3;
                g2.drawLine(x1, y1, x2, y2);

            }
//
        }catch (IOException e){

        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new DrawPanel());

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                frame.setUndecorated(true);

                frame.setSize(400, 400);
                frame.setVisible(true);
            }
        });
    }
}