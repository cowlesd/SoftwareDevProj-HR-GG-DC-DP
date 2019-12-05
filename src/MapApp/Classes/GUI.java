package MapApp.Classes;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

/**
 *
 * Special JFrame child class used to display main mapping application and offer choice of options
 * including
 */
public class GUI extends JFrame {
    private JPanel topPanel;
    private JPanel btnPanel;
    JTable table;
    private JScrollPane scrollPane;
    DefaultTableModel modl = new DefaultTableModel();
    private DijkstraProcesser dijkstraProcesser;

    /**
     *
     * Special JFrame child class used to display main mapping application and offer choice of options
     * including
     */
    public GUI(){
        JFileChooser fc = new JFileChooser();
        setTitle("MapApp Main Menu");
        setSize(400,200);
        setBackground(Color.black);

        table = new JTable(modl);

        modl.addColumn("Col1");
        fillTable();



        topPanel = new JPanel();
        btnPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel);
        getContentPane().add(btnPanel);
        scrollPane = new JScrollPane(table);
        topPanel.add(scrollPane,BorderLayout.CENTER);

        JButton newMap = new JButton("New Map");
        JButton refresh = new JButton("Refresh");
        JButton findPath = new JButton("Find Path");
        JButton quit = new JButton("Quit");

        btnPanel.add(newMap);
        btnPanel.add(refresh);
        btnPanel.add(findPath);
        btnPanel.add(quit);

        findPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = table.getSelectedRow();
                if (currentRow != -1) {


                    String mapName = (String) table.getValueAt(currentRow, 0);
                    try {
                        String startPoint = JOptionPane.showInputDialog("What is the name of your starting point?");
                        String endPoint = JOptionPane.showInputDialog("What is the name of your destination point?");
                        try {
                            dijkstraProcesser = new DijkstraProcesser(startPoint, endPoint, "src/main/resources/MapFiles/" +
                                    mapName + "/NodeSource.txt");
                            dijkstraProcesser.loadAdjacencyMatrix();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } catch (NumberFormatException num) {

                    }
                }

                /****************************************************/
            }
        });
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillTable();
            }
        });
        newMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayfile();
            }
        });
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });



        add(topPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);



    }

    /**
     *
     *Method to fill the table of buildings available to navigate
     *
     */
    public void fillTable() {
        File path = new File("src/main/resources/MapFiles");

        File [] files = path.listFiles();
        System.out.println(modl.getRowCount());
        modl.setRowCount(0);

        for (int i = 0; i < files.length; i++)
            modl.addRow(new Object[]{files[i].toString().substring(28)});

    }
    public void displayfile(){
//        FileDialog dialog = new FileDialog(this, "Select File to Open");
//        dialog.setMode(FileDialog.LOAD);
//        dialog.setVisible(true);
//        String file = dialog.getFile();
//        String directory = dialog.getDirectory();
//        System.out.println(file + " chosen.");
//        // pass filepath instead of calling inputdialog box
//        JOptionPane.showMessageDialog(this, "Press \"x\" " +
//                "to swap between node types, the default type is a waypoint node, " +
//                "the second type is a destination node, and the third is a map " +
//                "connection node . Press s to submit");
//        DrawPanel.runProg(directory + file);
        SuperiorWindow neew = new SuperiorWindow();
        neew.setVisible(true);
    }


}