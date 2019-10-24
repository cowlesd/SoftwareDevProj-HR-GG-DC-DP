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

public class DrawPanel extends JPanel  {
    //private static final long serialVersionUID = 1L;
    private ArrayList<Point> ContextPoints = new ArrayList<>();
    private ArrayList<Point> DestinationPoints = new ArrayList<>();
    private ArrayList<Point> ChangePoints = new ArrayList<>();
    private ArrayList<ArrayList<Point>> edges = new ArrayList<>();
    private static int status = 0;
    private static String filePath = "";
    private int contextGenCounter = 0;

    //Contains a list of nodes, which contains location and adjacent nodes
    private static ArrayList<Node> nodes = new ArrayList<>();

    public DrawPanel(String filepath1) {
        filePath = filepath1;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = screenSize.getHeight();
        ArrayList<Point> pair = new ArrayList<>();
        ArrayList<Node> nodePair = new ArrayList<>();
        BufferedImage image;
        setBackground(Color.WHITE);

        try {
            image = ImageIO.read(new File(filePath));
            JLabel picLabel = new JLabel(new ImageIcon(image.getScaledInstance(-1, (int)height, BufferedImage.SCALE_SMOOTH)));
            add(picLabel);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "The filepath you entered does not exist or was otherwise invalid.");
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                 if (status == 0) {
                    ContextPoints.add(new Point(e.getX(), e.getY()));
                    nodes.add(new Node(new Point(e.getX(), e.getY()),
                            JOptionPane.showInputDialog("Enter NodeID")));

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

    private Node getClosestNode(int x, int y) {
        for (Node node : nodes)
            if (Math.abs(x - node.getLocX()) < 10 && Math.abs(y - node.getLocY()) < 10)
                return node;


        return null;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    private int getWeight(ArrayList<Node> nodePair) {
        int xDif = Math.abs(nodePair.get(0).getLocX() - nodePair.get(1).getLocX());
        int yDif = Math.abs(nodePair.get(0).getLocY() - nodePair.get(1).getLocY());
        int finalDif = (int)Math.pow(xDif,2) + (int)Math.pow(yDif,2);

        return (int) Math.sqrt((double)finalDif);
    }

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
    private static void saveData(ArrayList<Node> nodes, String filePath, String name) {

        Path storageDir = Paths.get("src/MapApp/Assets/MapFiles/" + name);
        try {
            Files.createDirectories(storageDir);

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
            System.out.println("Fail");
        }


    }

}