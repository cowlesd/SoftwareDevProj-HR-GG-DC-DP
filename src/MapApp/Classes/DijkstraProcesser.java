package MapApp.Classes;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Calculator for the shortest path
 */
public class DijkstraProcesser {
    /**
     * String representing the file path for the map image
     */
    String mapPath;

    /**
     * String respresentation of the starting node
     */
    String startPoint;

    /**
     * int representation of the starting node
     */
    int startIndex;

    /**
     * String representation of the ending node
     */
    String endPoint;

    /**
     * int representation of the ending node
     */
    int endIndex;

    /**
     * Total number of vertices on the map
     */
    int numVertices;

    /**
     * Arraylist of all nodes
     */
    ArrayList<Node> nodeList;

    /**
     * Arraylist containing final path through nodes
     */
    ArrayList<Node> shortestPathNodes;

    /**
     * Representation of final path as coordinates
     */
    ArrayList<Integer[]> shortestPath;

    /**
     * Representation of graph as ints
     */
    int graph[][];


    /**
     * Default constructor for DijkstraProcessor class
     *
     * @param startPoint the starting node
     * @param endPoint   the ending node
     * @param mapName    the name of the map
     */
    public DijkstraProcesser(String startPoint, String endPoint, String mapName) {
        nodeList = new ArrayList<>();
        shortestPath = new ArrayList<>();
        shortestPathNodes = new ArrayList<>();
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.mapPath = mapName;
    }

    /**
     * Fills nodes ArrayList with data
     *
     * @throws IOException
     */
    public void loadAdjacencyMatrix() throws IOException {
        File file = new File(mapPath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int source = 0;

        String str = "";
        str = br.readLine();
        mapPath = str;

        String name = "";
        int x;
        int y;
        while ((str = br.readLine()) != null) {
            name = str.substring(0, str.indexOf(" "));
            str = str.substring(str.indexOf(" ") + 1);
            x = Integer.parseInt(str.substring(0, str.indexOf(" ")));
            str = str.substring(str.indexOf(" ") + 1);
            y = Integer.parseInt(str);

            boolean hasNode = false;
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).getID().equals(name)) {
                    nodeList.get(i).setLocX(x);
                    nodeList.get(i).setLocY(y);
                    hasNode = true;
                    break;
                }
            }
            if (!hasNode) {
                nodeList.add(new Node(new Point(x, y), name));
            }

            str = br.readLine();

            String adjName = "";
            int adjWeight = 0;
            int currentIndex = 0;
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).getID().equals(name))
                    currentIndex = i;
            }
            while (str != "") {
                boolean hasAdj = false;
                adjName = str.substring(0, str.indexOf(" "));
                str = str.substring(str.indexOf(" ") + 1);
                if (str.contains(" ")) {
                    adjWeight = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                    str = str.substring(str.indexOf(" ") + 1);
                } else {
                    adjWeight = Integer.parseInt(str);
                    str = "";
                }

                for (int i = 0; i < nodeList.size(); i++) {
                    if (nodeList.get(i).getID().equals(adjName)) {
                        if(hasNode)
                            nodeList.get(currentIndex).addAdjacent(nodeList.get(i), adjWeight);
                        else
                            nodeList.get(currentIndex).addAdjacent(nodeList.get(nodeList.size() - 1), adjWeight);
                        hasAdj = true;
                    }
                }
                if (!hasAdj) {
                    nodeList.add(new Node(new Point(0, 0), adjName));
                    nodeList.get(currentIndex).addAdjacent(nodeList.get(nodeList.size() - 1), adjWeight);
                }

                if (str.equals(""))
                    break;
            }
        }
        numVertices = nodeList.size();
        loadArray();
        markStartEnd();
        dijkstra(graph, startIndex);
    }

    /**
     * Loads the arrayList into an easy-to-manage format
     */
    public void loadArray() {
        graph = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            ArrayList<Node> adjacentList = nodeList.get(i).getAdjacent();
            ArrayList<Integer> weightList = nodeList.get(i).getWeight();
            for (int j = 0; j < adjacentList.size(); j++) {
                for (int k = 0; k < numVertices; k++) {
                    if (adjacentList.get(j).getID().equals(nodeList.get(k).getID())) {
                        graph[i][k] = weightList.get(j);
                    }
                }
            }
        }
    }

    /**
     * Marks the start and end points, given the string representation
     */
    public void markStartEnd() {
        for (int i = 0; i < numVertices; i++) {
            if (nodeList.get(i).getID().equals(startPoint))
                startIndex = i;
            if (nodeList.get(i).getID().equals(endPoint))
                endIndex = i;
        }
    }

    /**
     * Prints all node names and distance from start
     * Used for testing
     *
     * @param dist Array of distances from start for each node
     */
    void printSolution(int dist[]) {
        System.out.println("Vertex \t\t Distance from Source");
        for (int i = 0; i < numVertices; i++)
            System.out.println(nodeList.get(i).getID() + " " + i + " \t\t " + dist[i]);
    }

    /**
     * Finds the node with the minimum distance from all possible nodes
     *
     * @param dist   current distances of each node
     * @param sptSet contains true if node has been traversed, false if not
     * @return The index of the node with the minimum distance
     */
    int minDistance(int dist[], Boolean sptSet[]) {
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < numVertices; v++)
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }

    /**
     * The dijkstra algorithm, which finds the shortest path through all points. Terminates at the end point
     *
     * @param graph nodes to search
     * @param source   Index of the starting node
     */
    void dijkstra(int graph[][], int source) {

        nodeList.get(source).setMinDistance(0.);
        PriorityQueue<Node> nodeQueue = new PriorityQueue<Node>();
        nodeQueue.add(nodeList.get(source));

        while (!nodeQueue.isEmpty()) {
            Node u = nodeQueue.poll();

            // Visit each node connected to u
            for (Node v : u.adjacent) {
                int i = 0;
                while((i < v.adjacent.size() - 1) && v.getAdjacent(i) != u){
                    i++;
                }
                double weight = v.getWeight(i);
                double distanceThroughU = u.getMinDistance() + weight;
                if (distanceThroughU < v.getMinDistance()) {
                    nodeQueue.remove(v);

                    v.setMinDistance(distanceThroughU);
                    v.setPrevious(u);
                    nodeQueue.add(v);
                }
            }
        }

        int i;
        Node endNode = nodeList.get(0);
        for(i = 0; i < nodeList.size(); i++){
            if(nodeList.get(i).getID().equals(endPoint))
                endNode = nodeList.get(i);
        }
        getShortestPathTo(endNode);
        DisplayPath display = new DisplayPath();
        display.runProg(mapPath, shortestPath);
        display.repaint();
    }

    public List<Integer[]> getShortestPathTo(Node target)
    {
        for (Node vertex = target; vertex != null; vertex = vertex.getPrevious())
            shortestPath.add(new Integer[]{new Integer(vertex.getLocX()), new Integer(vertex.getLocY())});

        Collections.reverse(shortestPath);
        return shortestPath;
    }
}

