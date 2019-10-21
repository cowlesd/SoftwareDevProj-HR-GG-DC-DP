package MapApp.Classes;
import java.util.*;

class Building {
  private String[][] nodes;
//add instance variables including: array of nodes and edges (as dictionary?)
//add methods to scan in nodes/edges, find path, compare weights, 
    
    public static String[][] scan(String filename) {
        String[][] nodeArray = new String[0][0];
        try {
            int i = 0;
            File file = new File(filename);
            Scanner numLinesScanner = new Scanner(file);
            Scanner nodeScanner = new Scanner(file);
            String edge;
            String weight;
            while (numLinesScanner.hasNextLine()) {
                numLinesScanner.nextLine();
                ++i;
            }
            nodeArray = new String[i][i];
            i = 0;
            while (nodeScanner.hasNextLine()) {
                String[] tokens = nodeScanner.nextLine().split(",");
                edge = tokens[0];
                weight = tokens[1];
                nodeArray[i][0] = edge;
                nodeArray[i][1] = weight;
                ++i;
            }
        } catch (Exception ex) {
        }
        return nodeArray;
    }
}
