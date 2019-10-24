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

public class GUI extends JFrame {
    private JPanel topPanel;
    private JPanel btnPanel;
    JTable table;
    private JScrollPane scrollPane;
    DefaultTableModel modl = new DefaultTableModel();
    public GUI(){

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
                String str = (String)table.getValueAt(currentRow, 0);
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
                DrawPanel.runProg(JOptionPane.showInputDialog("Enter file path for image"));
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
    public void fillTable() {
        File path = new File("src/MapApp/Assets/MapFiles");

        File [] files = path.listFiles();
        System.out.println(modl.getRowCount());
        modl.setRowCount(0);

        for (int i = 0; i < files.length; i++)
            modl.addRow(new Object[]{files[i].toString().substring(27)});

    }


}