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

public class GUI extends JFrame implements ActionListener{
    private JPanel topPanel;
    private JPanel btnPanel;
    private JScrollPane scrollPane;

    public GUI(){
        setTitle("MapApp Main Menu");
        setSize(300,200);
        setBackground(Color.black);
        DefaultTableModel modl = new DefaultTableModel();
        JTable table = new JTable(modl);

        modl.addColumn("Col1");

        File path = new File("src/MapApp/Assets/MapFiles");
        String str = "";
        File [] files = path.listFiles();
        System.out.println(files.length);
        for (int i = 0; i < files.length; i++)
                modl.addRow(new Object[]{files[i].toString().substring(27)});



        topPanel = new JPanel();
        btnPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel);
        getContentPane().add(btnPanel);
        scrollPane = new JScrollPane(table);
        topPanel.add(scrollPane,BorderLayout.CENTER);

        JButton newMap = new JButton("New Map");
        JButton findPath = new JButton("Find Shortest Path");
        JButton quit = new JButton("Quit");

        btnPanel.add(newMap);
        btnPanel.add(findPath);
        btnPanel.add(quit);



        add(topPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

    }

}