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
import javax.xml.stream.Location;

/**
 * Display class (JPanel) used for overarching design and display (GUI) when making maps
 */
public class DrawPanel extends JPanel  {
    //private static final long serialVersionUID = 1L;
    /**
     * ArrayList of 'straight' points
     */
    private ArrayList<Point> ContextPoints = new ArrayList<>();

    /**
     * ArrayList of rooms, stairways, etc (Points)
     */
    private ArrayList<Point> DestinationPoints = new ArrayList<>();

    /**
     * ArrayList of changepoints
     */
    private ArrayList<Point> ChangePoints = new ArrayList<>();

    /**
     * ArrayList of connectionless points for 'straight' edges
     */
    private ArrayList<ArrayList<Point>> edges = new ArrayList<>();

    /**
     * Variable to determine which type of point is being added
     */
    private static int status = 0;

    /**
     * Address of file being 'drawn' upon
     */
    private static String filePath = "";

    /**
     * Node counter
     */
    private int contextGenCounter = 0;

    /**
     * Contains a list of nodes, which contains location and adjacent nodes
     */
    private static ArrayList<Node> nodes = new ArrayList<>();

    /**
     * Default constructor to create panel
     *
     * @param filepath1 address of file to 'draw' upon
     */
    public DrawPanel(String filepath1) {
        filePath = filepath1;
         ArrayList<Point> pair = new ArrayList<>();
         ArrayList<Node> nodePair = new ArrayList<>();
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                 if (status == 0) {
                    ContextPoints.add(new Point(e.getX(), e.getY()));
//                    nodes.add(new Node(new Point(e.getX(), e.getY()),
//                            JOptionPane.showInputDialog("Enter NodeID")));

                     nodes.add(new Node(new Point(e.getX(), e.getY()), "A" + contextGenCounter++));
                } else if (status == 1) {
                    DestinationPoints.add(new Point(e.getX(), e.getY()));
                    nodes.add(new Node(new Point(e.getX(), e.getY()),
                            JOptionPane.showInputDialog("Enter NodeID")));

                } else if (status == 3) {
                    if (null != getClosestNode(e.getX(), e.getY())) {

                        pair.add(new Point(getClosestNode(e.getX(), e.getY()).getLocX(),
                                getClosestNode(e.getX(), e.getY()).getLocY()));

                        nodePair.add(getClosestNode(e.getX(), e.getY()));

                    }
                    if (pair.size() == 2) {
                        edges.add(new ArrayList<>(pair));

                        nodePair.get(0).addAdjacent(nodePair.get(1), getWeight(nodePair));
                        nodePair.get(1).addAdjacent(nodePair.get(0), getWeight(nodePair));

                        nodePair.clear();

                        pair.clear();

                    }
                } else if(status == 2) {
                     ChangePoints.add(new Point(e.getX(), e.getY()));
                     nodes.add(new Node(new Point(e.getX(), e.getY()),
                             JOptionPane.showInputDialog("Enter NodeID")));
                 /********************************************************************************/
                 }

                repaint();
            }
        });
    }
//    private boolean isDuplicate(Node n) {
//        for (Destination)
//    }

    /**
     * Method to return the node closest
     *
     * @param x the x coordinate to search from
     * @param y the y coordinate to search from
     * @return the Node closest to (x, y)
     */
    private Node getClosestNode(int x, int y) {
        for (Node node : nodes)
            if (Math.abs(x - node.getLocX()) < 10 && Math.abs(y - node.getLocY()) < 10)
                return node;


        return null;
    }

    /**
     * Default getter for Nodes
     *
     * @return The arraylist of all Nodes
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * Finds the weight between a given pair of of Nodes using their x and y coordinates
     *
     * @param nodePair The pair of two nodes to evaluate for weight (distance)
     * @return The weight (distance) between two nodes as an integer
     */
    private int getWeight(ArrayList<Node> nodePair) {
        int xDif = Math.abs(nodePair.get(0).getLocX() - nodePair.get(1).getLocX());
        int yDif = Math.abs(nodePair.get(0).getLocY() - nodePair.get(1).getLocY());
        int finalDif = (int)Math.pow(xDif,2) + (int)Math.pow(yDif,2);

        return (int) Math.sqrt((double)finalDif);
    }

    /**
     *Method used to 'paint' the components as the user creates them.
     *
     * @param g Graphics object containing desirable state
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gDraw = (Graphics2D) g;
        gDraw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gDraw.setColor(Color.blue);
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            g.drawImage(image, 0, 0,getWidth(), getHeight(), null);
            for (Point point : ContextPoints) {
                gDraw.fillOval(point.x-5, point.y-9, 10, 10);
            }
            gDraw.setColor(Color.red);
            for (Point point : DestinationPoints) {
                gDraw.fillOval(point.x-5, point.y-9, 10, 10);
            }
            gDraw.setColor(Color.MAGENTA);
            for (Point point : ChangePoints) {
                gDraw.fillOval(point.x-5, point.y-9, 10, 10);
            }
            gDraw.setColor(Color.green);
            gDraw.setStroke(new BasicStroke(4));

            for (int i = 0; i < edges.size(); i++) {
                int x1 = (int)edges.get(i).get(0).getX();
                int y1 = edges.get(i).get(0).y -3;
                int x2 = edges.get(i).get(1).x;
                int y2 = edges.get(i).get(1).y -3;
                gDraw.drawLine(x1, y1, x2, y2);

            }
//
        }catch (IOException e){
            System.out.print(filePath);
        }
    }

    /**
     *Method to control/run the map-drawing portion of the app
     *
     * @param filePath1 File to 'draw' upon
     */
    public static void runProg(String filePath1) {

        JFrame frame = new JFrame();
        frame.add(new DrawPanel(filePath1));
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() =='x') {
                    status++;
                    if (status== 4) {
                        status = 0;
                    }
                }
                if (e.getKeyChar() =='s') {
                    saveData(nodes, filePath1, JOptionPane.showInputDialog("Save Name"));
                    frame.setVisible(false);
                    frame.dispose();
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

    /**
     *Method to save information created in the map-drawing program
     *
     * @param nodes ArrayList of nodes to save
     * @param filePath Address to save to (in NodeSource file)
     * @param name name of original map file
     */
    private static void saveData(ArrayList<Node> nodes, String filePath, String name) {

        Path storageDir = Paths.get("src/main/resources/MapFiles/" + name);
        try {
            Files.createDirectories(storageDir);

            PrintWriter writer = new PrintWriter("src/main/resources/MapFiles/" +
                    name+ "/NodeSource.txt", "UTF-8");
            writer.println(filePath);

            for(Node node: nodes) {
                writer.println(node.getID() + " " + node.getLocX() + " " + node.getLocY());
                for (int i =0;i<node.getAdjacent().size();i++) {
                    writer.print(node.getAdjacent().get(i).getID() + " ");
                    writer.print(node.getWeight().get(i)+ " ");
                }
                writer.println();
            }

            writer.close();


        }catch (IOException e) {
            System.out.println("Fail");
        }


    }

}