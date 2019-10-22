package MapApp.Classes;

import java.awt.*;
import java.util.ArrayList;
public class Node {
    private ArrayList<Node> adjacent = new ArrayList<>();
    private ArrayList<Integer> weight = new ArrayList<>();

    private ArrayList<Point> points = new ArrayList<>();

    private Point location;
    private String nodeID;

    public Node(Point currentLoc, String name) {
        location = currentLoc;
        nodeID = name;
    }

    public void addAdjacent(Node node, int newWeight) {
        adjacent.add(node);
        weight.add(newWeight);
    }

    public int getLocX() {
        return location.x;
    }
    public int getLocY() {
        return location.y;
    }
    public String getID() {
        return nodeID;
    }

    public void printAdjacent() {
        for (Node node: adjacent) {
            System.out.print("  " + node.nodeID);
        }
        System.out.println();
    }




}
