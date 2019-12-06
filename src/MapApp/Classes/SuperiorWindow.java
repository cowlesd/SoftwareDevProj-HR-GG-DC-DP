package MapApp.Classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * A superior version of the GUI for the mapping program. Sleek, stylish, new, what can't it do???
 */
public class SuperiorWindow extends JFrame implements ActionListener {
    JButton wayPointBtn = new JButton("Waypoints");
    JButton destinationPointBtn = new JButton("Destination Points");
    JButton linkPointsBtn = new JButton("LinkPoints");
    JButton drawPathBtn = new JButton("Draw Path");
    JButton erasePointBtn = new JButton("Erase Nodes");
    JButton erasePathBtn = new JButton("Erase Path");
    JButton newFloorBtn = new JButton("Manage Floors");
    JButton checkID = new JButton("Check ID");
    JLabel currentState = new JLabel("    Waypoints");

    JButton btns [] = {wayPointBtn, destinationPointBtn,
            linkPointsBtn, drawPathBtn, erasePointBtn, erasePathBtn, newFloorBtn, checkID};

    JPanel btnSet;

    JMenuBar mb = new JMenuBar();
    JMenuItem Save = new JMenuItem("Save");
    JMenuItem newProj = new JMenuItem("New Project");
    JMenuItem getCodeRep = new JMenuItem("Generate \n Graph Code");
    JCheckBoxMenuItem customWeightitem= new JCheckBoxMenuItem("Custom Weight");
    JMenu menu1;

    ArrayList<Point> ContextPoints = new ArrayList<>();
    ArrayList<Point> ChangePoints = new ArrayList<>();
    ArrayList<Point> DestinationPoints = new ArrayList<>();
    private ArrayList<ArrayList<Point>> edges = new ArrayList<>();
    private static ArrayList<Node> nodes = new ArrayList<>();

    private int contextGenCounter = 0;
    private boolean customWeight = false;
    private int status = 10;

    String filePath = new String();

    /**** Insets ***/
    int InsetX = 0;
    int InsetY = 0;

    int weight = 0;

    /**
     * Constructor for the ol' sexy SuperiorWindow class. Gets you all set up with
     * a SuperiorWindow of your own, as seen on TV.
     */
    public SuperiorWindow() {
       JPanel container = new JPanel();
       container.setLayout(new BorderLayout());


       /******** Creates, formats and populates list of buttons *******/
       btnSet = new JPanel();
       btnSet.setLayout(new GridLayout(9, 1));
       btnSet.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
       btnSet.setBackground(Color.orange);

       //currentState.setBorder(BorderFactory.createLineBorder(Color.BLACK, 20));
       btnSet.add(currentState);

       for(JButton b : btns) {
           b.addActionListener(this);
           //b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
           btnSet.add(b);
           b.setEnabled(false);
       }


        /******** Creates and Adds A new ImagePanel *****/
       loadFile();
       JPanel imagePanel = paintImage();
       imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));



       menu1 = new JMenu("Other Options");
       menu1.add(Save);
       menu1.add(newProj);
       menu1.add(customWeightitem);
       menu1.add(getCodeRep);
       mb.add(menu1);


       /*********** Adds Menus, Panels to JFrame and finalizes frame creation *******/
       container.add(btnSet, BorderLayout.WEST);
       container.add(imagePanel);

       this.setJMenuBar(mb);
       this.add(container);

       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setExtendedState(Frame.MAXIMIZED_BOTH);

       this.setSize(400, 400);
       this.setVisible(true);
       this.validate();
       this.repaint();

       this.setResizable(false);

       drawOnPanel();
   }

    /**
     * Simple method that notifies the user of the necessity to calibrate the Graphmaker,
     * as well as how to do so. This is a public method that is called externally so that it does not hamper
     * the more aesthetic portions of SupWindow creation, as something like, say, title assignment
     * would not continue until the JOptionPane was closed.
     */
   public void notifyOfCalibration(){
       JOptionPane.showMessageDialog(this,
               "In order to calibrate the Graphmaker, please place the tip of \n" +
                       "your cursor in the white box at the upper right hand corner of \n" +
                       "the image and click once before doing any other actions.");
   }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

       for (JButton b : btns) {
           if (actionEvent.getSource() == b) {
               resetButtonGraphics(btns);
               setSelected(b);
               currentState.setText("    " + b.getText());

           }
       }
       if (actionEvent.getSource() == wayPointBtn) {
           status = 0;
       } else if (actionEvent.getSource() == destinationPointBtn) {
           status = 1;
       } else if (actionEvent.getSource() == linkPointsBtn) {
           status = 2;
       } else if (actionEvent.getSource() == drawPathBtn) {
           status = 3;
       } else if (actionEvent.getSource() == erasePathBtn) {
           status = 4;
       } else if (actionEvent.getSource() == erasePointBtn) {
           status = 5;
       } else if(actionEvent.getSource() == checkID) {
           status = 6;
       }else if(actionEvent.getSource() == Save){
           saveData(nodes, filePath, JOptionPane.showInputDialog("Please enter a name for your save.") );
       }


    }

    /**
     * Method to reset the visuals of buttons.
     *
     * @param btns The array of buttons to be reset
     */
    private void resetButtonGraphics(JButton btns[]) {
       for (JButton b : btns) {
           b.setForeground(Color.BLACK);
           b.setOpaque(false);
       }
    }

    /**
     * Sets the visual of a button to a visually unique state to signify that
     * it is currently selected
     *
     * @param btn the specific button to be selected
     */
    private void setSelected(JButton btn) {
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.BLUE);
        btn.setOpaque(true);
    }

    /**
     * Method to create an image that combines the image of the map and the present nodes and edges
     *
     * @return a newly 'drawn upon' JPanel combining the graphics of nodes/edges and the map itself
     */
    public JPanel paintImage() {

        JPanel Pane = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D gDraw = (Graphics2D) g;
                gDraw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gDraw.setColor(Color.blue);
                try {
                    BufferedImage image = ImageIO.read(new File(filePath));
                    g.drawImage(image, 0, 0,getWidth(), getHeight(), null);
                    if (status== 10) {
                        gDraw.setColor(Color.RED);
                        gDraw.fillRect(0, 0, 20, 20);
                        gDraw.setColor(Color.WHITE);
                        gDraw.fillRect(0, 0, 10, 10);
                    }

                    gDraw.setColor(Color.green);
                    gDraw.setStroke(new BasicStroke(4));

                    for (int i = 0; i < edges.size(); i++) {
                        int x1 = (int)edges.get(i).get(0).getX();
                        int y1 = edges.get(i).get(0).y;
                        int x2 = edges.get(i).get(1).x;
                        int y2 = edges.get(i).get(1).y;
                        gDraw.drawLine(x1 - InsetX+3, y1- InsetY+3,
                                x2- InsetX+3, y2- InsetY+3);

                    }

                    gDraw.setColor(Color.blue);
                    for (Point point : ContextPoints) {
                        gDraw.fillOval(point.x-InsetX, point.y-InsetY, 10, 10);
                    }
                    gDraw.setColor(Color.red);
                    for (Point point : DestinationPoints) {
                        gDraw.fillOval(point.x-InsetX, point.y-InsetY, 10, 10);
                    }
                    gDraw.setColor(Color.MAGENTA);
                    for (Point point : ChangePoints) {
                        gDraw.fillOval(point.x-InsetX, point.y-InsetY, 10, 10);
                    }

//
           }catch (IOException e){
                    System.out.print(filePath);
                }
            }
        };
        return Pane;
    }

    /**
     * Method that performs the graphic operations requested by the user (adding nodes of various types and edges)
     */
    public void drawOnPanel() {
        //filePath = filepath1;
        ArrayList<Point> pair = new ArrayList<>();
        ArrayList<Node> nodePair = new ArrayList<>();
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(customWeightitem.isSelected()) {
                    customWeight = true;
                } else {
                    customWeight = false;
                }
                if (status == 10) {
                    status = 0;
                    calibrate(e.getX(), e.getY());

                }else if (status == 0) {
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

                        weight = getWeight(nodePair);

                        nodePair.get(0).addAdjacent(nodePair.get(1), weight);
                        nodePair.get(1).addAdjacent(nodePair.get(0), weight);

                        nodePair.clear();

                        pair.clear();

                    }
                } else if(status == 2) {
                    ChangePoints.add(new Point(e.getX(), e.getY()));
                    nodes.add(new Node(new Point(e.getX(), e.getY()),
                            JOptionPane.showInputDialog("Enter NodeID")));
                    /********************************************************************************/
                }else if (status == 4) {
                    if (null != getClosestNode(e.getX(), e.getY())) {

                        pair.add(new Point(getClosestNode(e.getX(), e.getY()).getLocX(),
                                getClosestNode(e.getX(), e.getY()).getLocY()));

                        nodePair.add(getClosestNode(e.getX(), e.getY()));

                    }
                    if (pair.size() == 2) {
                        removeEdge(pair);
                        pair.clear();
                    }
                } else if (status == 5) {
                    if (null != getClosestNode(e.getX(), e.getY())) {
                        removeNode(getClosestNode(e.getX(), e.getY()).getLocX(),
                                getClosestNode(e.getX(), e.getY()).getLocY()   );
                        System.out.println("NodeSize: " + nodes.size());
                        nodes.remove(getClosestNode(e.getX(), e.getY()));
                        System.out.println("NodeSize: " + nodes.size());
                    }
                }else  if ( status == 6){
                    try {
                        JOptionPane.showMessageDialog(null, "Node ID: " +
                                getClosestNode(e.getX(), e.getY()).getID());
                    }catch (NullPointerException nul) {

                    }
                }

                repaint();
            }
        });
    }

    /**
     * Method to query for a file, create a string out of the filepath to the chosen file,
     * and then modifies the filePath member to this string
     */
    public void loadFile() {
        FileDialog dialog = new FileDialog(this, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getFile();
        String directory = dialog.getDirectory();

        filePath = directory + file;
    }

    /**
     * Method to grab and return the node closest to the given (x, y) point
     *
     * @param x the x-coordinate to search from
     * @param y the y-coordiante to search from
     * @return the node closest to (x, y).
     */
    private Node getClosestNode(int x, int y) {
        for (Node node : nodes)
            if (Math.abs(x - node.getLocX()) < 10 && Math.abs(y - node.getLocY()) < 10)
                return node;
        return null;
    }

    /**
     * Method to calculate the pythagorean distance between two nodes
     *
     * @param nodePair the two nodes for which distance will be found
     * @return the pythagorean distance between the pair of nodes
     */
    private int getWeight(ArrayList<Node> nodePair) {
       int weight = -2;
       if(!customWeight) {
           int xDif = Math.abs(nodePair.get(0).getLocX() - nodePair.get(1).getLocX());
           int yDif = Math.abs(nodePair.get(0).getLocY() - nodePair.get(1).getLocY());
           int finalDif = (int) Math.pow(xDif, 2) + (int) Math.pow(yDif, 2);

           weight = (int) Math.sqrt((double) finalDif);
       } else {
           try {
               weight = Integer.parseInt(JOptionPane.showInputDialog("Please enter a line weight"));
           }catch (Exception e) {
               while (weight == -2) {
                   weight = Integer.parseInt(JOptionPane.showInputDialog("Please enter a valid line weight"));
               }
           }
       }
       return weight;

    }

    /**
     * Method to compare two points and return whether they exist in the same space or not
     *
     * @param n1 the first point to compare
     * @param n2 the second point to compare to
     * @return true if the points possess the same coordinates, false otherwise
     */
    private boolean comparePoints(Point n1, Point n2) {
       if (n1.getX() == n2.getX() && n1.getY() == n2.getY()) {
           return true;
       }
       return false;
    }

    /**
     * Method to remove an edge between two points
     *
     * @param pair a pair containing the two points between which the edge will be removed
     */
    public void removeEdge(ArrayList<Point> pair) {
        int toRm = -1;
        int counter = 0;
        for (ArrayList<Point> edge: edges) {
            counter++;
            for (Point p1 : edge) {
                for (Point p2 : pair) {
                    if (comparePoints(p1, p2) && comparePoints(p2, p1)) {
                        toRm = counter -1;
                        break;
                    }
                }
            }
        }
        if (toRm != -1)
            edges.remove(toRm );

    }

    /**
     * Method to remove a node at a mouse click lcoation
     *
     * @param x x coordinate of mouse click
     * @param y y coordinate of mouse click
     */
    public void removeNode(int x, int y) {
       ArrayList<ArrayList<Point>> rmedges = new ArrayList<>();
       boolean rm = false;
        for (ArrayList<Point> edge: edges) {
            for (Point p1 : edge) {
                if (p1.getX() == x && p1.getY() == y) {
                    rmedges.add(edge);
                }

            }
        }
        for(ArrayList<Point> edge : rmedges) {
            edges.remove(edge);
        }

        Point toRm = new Point();
        for(Point p : ContextPoints) {
            if (p.getX() == x && p.getY() == y) {
                rm = true;
                toRm = p;
                break;
            }
        }
        ContextPoints.remove(toRm);
        for(Point p : ChangePoints) {
            if (p.getX() == x && p.getY() == y) {
                rm = true;
                toRm = p;
                break;
            }
        }
        ChangePoints.remove(toRm);

        for (Point p : DestinationPoints) {
            if (p.getX() == x && p.getY() == y) {
                rm = true;
                toRm = p;
                break;
            }
        }
        if (rm)
            DestinationPoints.remove(toRm);
    }

    /**
     * Method to calibrate the x and y coordination of the image via mouse clicks
     *
     * @param X x location of mouse click
     * @param Y y location of mouse click
     */
    public void calibrate(int X, int Y) {
       for (JButton b : btns) {
           b.setEnabled(true);
       }
       wayPointBtn.setBackground(Color.BLACK);
       wayPointBtn.setForeground(Color.BLUE);
       wayPointBtn.setOpaque(true);

       InsetX = X -1;
       InsetY = Y -1;


    }

    /**
     * Method that saves the map information to a file
     *
     * @param nodes corresponding nodes to save
     * @param filePath file path location at which to save
     * @param name the name of the file to be saved to
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
