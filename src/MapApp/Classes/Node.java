package MapApp.Classes;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
public class Node {

    /**
     * ArrayList containing the nodes adjacent to 'this' node
     */
    private ArrayList<Node> adjacent = new ArrayList<>();

    /**
     * ArrayList containing the weight of this node when compared to adjacent nodes
     */
    private ArrayList<Integer> weight = new ArrayList<>();

    /**
     * ArrayList containing points
     */
    private ArrayList<Point> points = new ArrayList<>();

    /**
     * The location of 'this' node as (x, y)
     */
    private Point location;

    /**
     * Unique ID of 'this' node
     */
    private String nodeID;

    /**
     * Default constructor taking location and ID
     *
     * @param currentLoc (x, y) at which to create 'this' node
     * @param name Unique ID to give 'this' node
     */
    public Node(Point currentLoc, String name) {
        location = currentLoc;
        nodeID = name;
    }

    /**
     * Inserts a new node to the ArrayList holding nodes adjacent to 'this' node
     *
     * @param node Node to insert as adjacent (node that is adjacent to 'this')
     * @param newWeight Weight (distance) between the adjacent node and 'this' node
     */
    public void addAdjacent(Node node, int newWeight) {
        adjacent.add(node);
        weight.add(newWeight);
    }

    /**
     * Getter for the x-coordinate of 'this' node
     *
     * @return x coordinate of 'this' node
     */
    public int getLocX() {
        return location.x;
    }

    /**
     * Getter for the y-coordinate of 'this' node
     *
     * @return y coordinate of 'this' node
     */
    public int getLocY() {
        return location.y;
    }

    /**
     * Getter for the ID of 'this' node
     *
     * @return The ID of 'this' node
     */
    public String getID() {
        return nodeID;
    }

    /**
     *Prints the adjacent node's ID's in the console
     */
    public void printAdjacent() {
        for (Node node: adjacent) {
            System.out.print("  " + node.nodeID);
        }
        System.out.println();
    }

    /**
     * Getter for ArrayList of nodes adjacent to 'this' node
     *
     * @return An ArrayList of nodes adjacent to 'this' node
     */
    public ArrayList<Node> returnAdjacent() {
        return adjacent;
    }

    /**
     * Getter for the ArrayList of weights between 'this' node and adjacent nodes
     *
     * @return An ArrayList of integers representing the distance between 'this' node and adjacent nodes
     */
    public ArrayList<Integer> returnweight() {
        return weight;
    }




}
