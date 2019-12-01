package MapApp.Classes;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The class used to represent and contain the information of/at a particular point of the building.
 * Contains all information pertaining to 'this' location.
 */
public class Node implements Comparable<Node> {

    /**
     * ArrayList containing the nodes adjac ent to 'this' node
     */
    public ArrayList<Node> adjacent = new ArrayList<>();

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

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    private Node previous;

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    private double minDistance = Double.POSITIVE_INFINITY;

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
    public void setLocX(int x) {location.x = x;}
    public void setLocY(int y) {location.y = y;}
    /**
     * Getter for the ID of 'this' node
     *
     * @return The ID of 'this' node
     */
    public String getID() { return nodeID; }

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
    public ArrayList<Node> getAdjacent() {
        return adjacent;
    }
    public Node getAdjacent(int i) {
        return adjacent.get(i);
    }

    /**
     * Getter for the ArrayList of weights between 'this' node and adjacent nodes
     *
     * @return An ArrayList of integers representing the distance between 'this' node and adjacent nodes
     */
    public ArrayList<Integer> getWeight() {
        return weight;
    }
    public int getWeight(int i) {
        return weight.get(i);
    }
    public int compareTo(Node other)
    {
        return Double.compare(minDistance, other.minDistance);
    }




}
