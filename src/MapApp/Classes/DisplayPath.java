package MapApp.Classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *Class to display the path (shortest) after it is found. Child of JPanel.
 */
public class DisplayPath extends JPanel {
    /**
     * Address of the file to be used/drawn upon
     */
    private static String filePath="";
    /**
     * Coordinates between which to operate on / draw edges
     */
    private ArrayList<Integer[]> coordinateList = new ArrayList<>();

    /**
     * Class to actually.. you know... draw the panel.
     *
     * @param filepath1 the file path of the file to 'draw'
     *
     */
    //@Override
    public void DrawPanel(String filepath1) {
        filePath = filepath1;
        setBackground(Color.WHITE);
        repaint();
    }

    /**
     *Method to draw edges between nodes (as lines)
     *
     * @param g graphic object representing the desired states being drawn
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

            for (int i = 0; i < coordinateList.size() - 1; i++) {
                int x1 = (int)coordinateList.get(i)[0];
                int y1 = coordinateList.get(i)[1] -3;//modification of coordinates to provide accurate location
                int x2 = coordinateList.get(i+1)[0];
                int y2 = coordinateList.get(i+1)[1] -3;
                gDraw.drawLine(x1, y1, x2, y2);

            }

        }catch (IOException e){
            System.out.print(filePath);
        }
    }

    /**
     *Method to run the DisplayPath subroutine
     *
     * @param filePath1 Contains the filepath of the map to be used
     * @param coordinates ArrayList of coordinates
     */
    public void runProg(String filePath1, ArrayList<Integer[]> coordinates) {
        filePath = filePath1;
        coordinateList = coordinates;
        JFrame frame = new JFrame();
        frame.add(this);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.setSize(400, 600);
        frame.setVisible(true);
        repaint();
    }

}
