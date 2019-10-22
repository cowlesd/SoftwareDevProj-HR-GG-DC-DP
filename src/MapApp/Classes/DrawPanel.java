package MapApp.Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.Point;
import java.nio.file.*;


import javax.swing.JFrame;

public class DrawPanel extends JPanel  {
    private static final long serialVersionUID = 1L;
    private ArrayList<Point> ContextPoints = new ArrayList<>();
    private ArrayList<Point> DestinationPoints = new ArrayList<>();;
    private ArrayList<ArrayList<Point>> edges = new ArrayList<>();;
    private static int status = 0;
    static String filePath = "";

    JMenuBar menubar = new JMenuBar();
    JMenu menu = new JMenu("size");
    JMenuItem size = new JMenuItem("size");

    //Contains a list of nodes, which contains location and adjacent nodes
    private static ArrayList<Node> nodes = new ArrayList<>();

    public DrawPanel(String filepath1) {
        filePath = filepath1;
         ArrayList<Point> pair = new ArrayList<>();
         ArrayList<Node> nodePair = new ArrayList<>();
        int tempx = 0;
        int tempy = 0;
        ContextPoints = new ArrayList<Point>();
        setBackground(Color.WHITE);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                 if (status == 0) {
                    ContextPoints.add(new Point(e.getX(), e.getY()));

                    nodes.add(new Node(new Point(e.getX(), e.getY()),
                            JOptionPane.showInputDialog("Enter NodeID")));

                } else if (status == 1) {
                    DestinationPoints.add(new Point(e.getX(), e.getY()));
                    nodes.add(new Node(new Point(e.getX(), e.getY()),
                            JOptionPane.showInputDialog("Enter NodeID")));

                } else if (status == 2) {

                    if (null != getClosestNode(e.getX(), e.getY())) {

                        pair.add(new Point(getClosestNode(e.getX(), e.getY()).getLocX(),
                                getClosestNode(e.getX(), e.getY()).getLocY()));

                        //
                        nodePair.add(getClosestNode(e.getX(), e.getY()));



                    }
                    if (pair.size() == 2) {
                        edges.add(new ArrayList<>(pair));

                        nodePair.get(0).addAdjacent(nodePair.get(1), getWeight(nodePair));
                        nodePair.get(1).addAdjacent(nodePair.get(0), getWeight(nodePair));

                        nodePair.clear();


                        pair.clear();

                    }
                }

                repaint();
            }
        });
    }

    private Node getClosestNode(int x, int y) {
        for (Node node : nodes)
            if (Math.abs(x - node.getLocX()) < 10 && Math.abs(y - node.getLocY()) < 10)
                return node;


        return null;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void printNodes() {
        for(Node node: nodes) {
            System.out.print(node.getID() + ": ");
            node.printAdjacent();
        }
    }

    private int getWeight(ArrayList<Node> nodePair) {
        int xdif = Math.abs(nodePair.get(0).getLocX() - nodePair.get(1).getLocX());
        int ydif = Math.abs(nodePair.get(0).getLocY() - nodePair.get(1).getLocY());
        int finaldif = (int)Math.pow(xdif,2) + (int)Math.pow(ydif,2);

        return (int) Math.sqrt((double)finaldif);
    }

    @Override
    public void paintComponent(Graphics g) {


        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.blue);
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            //BufferedImage image = ImageIO.read(file);
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
            System.out.print(filePath);
        }
    }


    public static void runProg(String filePath1) {

                JFrame frame = new JFrame();
        frame.add(new DrawPanel(filePath1));
                frame.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() =='x') {
                            status++;
                                if (status== 3) {
                                    status = 0;
                                }
                        }
                        if (e.getKeyChar() =='s') {
                            saveData(nodes, filePath1, JOptionPane.showInputDialog("Save Name"));
                            System.exit(0);
                            /*************************************************/
                        }
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });



                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                frame.setUndecorated(true);

                frame.setSize(400, 400);
                frame.setVisible(true);
    }
    private static void saveData(ArrayList<Node> nodes, String filePath, String name) {
        //new File("src/MapApp/MapFiles/" + name).mkdirs();

        Path source = Paths.get(filePath); //original file
        Path targetDir = Paths.get("src/MapApp/Assets/MapFiles/" + name);
        try {
            Files.createDirectories(targetDir);//in case target directory didn't exist
            System.out.println("saved here");
            Path target = targetDir.resolve(name);// create new path ending with `name` content

            PrintWriter writer = new PrintWriter("src/MapApp/Assets/MapFiles/" +
                    name+ "/NodeSource.txt", "UTF-8");
            writer.println(filePath);

            for(Node node: nodes) {
                writer.println(node.getID() + " " + node.getLocX() + " " + node.getLocY());
                for (int i =0;i<node.returnAdjacent().size();i++) {
                    writer.print(node.returnAdjacent().get(i).getID() + " ");
                    writer.print(node.returnweight().get(i)+ " ");
                }
                writer.println();
            }

            writer.close();

        }catch (IOException e) {

        }


    }

}