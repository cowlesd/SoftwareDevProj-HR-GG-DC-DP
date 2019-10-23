package MapApp.Classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class DisplayPath extends JPanel {
    private static String filePath="";
    private ArrayList<Integer[]> coordinateList = new ArrayList<>();

    //@Override
    public void DrawPanel(String filepath1) {
        filePath = filepath1;
        setBackground(Color.WHITE);
        repaint();
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

            for (int i = 0; i < coordinateList.size(); i++) {
                int x1 = (int)coordinateList.get(i)[0];
                int y1 = coordinateList.get(i)[1] -3;//modification of coordinates to provide accurate location
                int x2 = coordinateList.get(i)[2];
                int y2 = coordinateList.get(i)[3] -3;
                gDraw.drawLine(x1, y1, x2, y2);

            }

        }catch (IOException e){
            System.out.print(filePath);
        }
    }


    public static void runProg(String filePath1, ArrayList<Integer[]> coordinates) {
        filePath = filePath1;
        JFrame frame = new JFrame();
        frame.add(new DrawPanel(filePath1));


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.setSize(400, 400);
        frame.setVisible(true);
    }

}
