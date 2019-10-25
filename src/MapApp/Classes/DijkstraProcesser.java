package MapApp.Classes;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Calculator for the shortest path
 */
public class DijkstraProcesser {
    String mapPath;
    String startPoint;
    int startIndex;
    String endPoint;
    int endIndex;
    int numVertices;
    HashMap<String, Integer> vertexMap;
    ArrayList<Node> nodeList;
    ArrayList<Node> shortestPathNodes;
    ArrayList<Integer[]> shortestPath;
    int graph[][];


    public DijkstraProcesser(String startPoint, String endPoint, String mapName) {
        vertexMap = new HashMap<>();
        nodeList = new ArrayList<>();
        shortestPath = new ArrayList<>();
        shortestPathNodes = new ArrayList<>();
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.mapPath = mapName;
    }

    //Fills nodes ArrayList with data
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
        while ((str = br.readLine()) != null){
            name = str.substring(0, str.indexOf(" "));
            str = str.substring(str.indexOf(" ") + 1);
            x = Integer.parseInt(str.substring(0, str.indexOf(" ")));
            str = str.substring(str.indexOf(" ") + 1);
            y = Integer.parseInt(str);

            boolean hasAdj = false;
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).getID().equals(name)){
                    nodeList.get(i).setLocX(x);
                    nodeList.get(i).setLocY(y);
                    hasAdj = true;
                }
            }
            if(!hasAdj){
                nodeList.add(new Node(new Point(x,y), name));
            }

            str = br.readLine();

            String adjName = "";
            int adjWeight = 0;
            int currentIndex = 0;
            hasAdj = false;
            for(int i = 0; i < nodeList.size(); i++){
                if(nodeList.get(i).getID().equals(name))
                    currentIndex = i;
            }
            while (str != "") {
                    adjName = str.substring(0, str.indexOf(" "));
                str = str.substring(str.indexOf(" ") + 1);
                if(str.contains(" ")) {
                    adjWeight = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                    str = str.substring(str.indexOf(" ") + 1);
                }
                else{
                    adjWeight = Integer.parseInt(str);
                    str = "";
                }

                for (int i = 0; i < nodeList.size(); i++) {
                    if (nodeList.get(i).getID().equals(adjName)){
                        nodeList.get(currentIndex).addAdjacent(nodeList.get(i), adjWeight);
                        nodeList.get(currentIndex).addAdjacent(nodeList.get(nodeList.size() - 1), adjWeight);
                        hasAdj = true;
                    }
                }
                if(!hasAdj) {
                    nodeList.add(new Node(new Point(0, 0), adjName));
                    nodeList.get(currentIndex).addAdjacent(nodeList.get(nodeList.size() - 1), adjWeight);
                }

                if(str.equals(""))
                    break;
            }
        }
        numVertices = nodeList.size();
        loadArray();
        markStartEnd();
        dijkstra(graph, startIndex);
    }

    //Loads the arraylist into an easy-to-manage format
    public void loadArray(){
        graph = new int[numVertices][numVertices];
        for(int i = 0; i < numVertices; i++){
            ArrayList<Node> adjacentList = nodeList.get(i).getAdjacent();
            ArrayList<Integer> weightList = nodeList.get(i).getWeight();
            for(int j = 0; j < adjacentList.size(); j++){
                for(int k = 0; k < numVertices; k++){
                    if(adjacentList.get(j).getID().equals(nodeList.get(k).getID())){
                        graph[i][k] = weightList.get(j);
                    }
                }
            }
        }
    }

    public void markStartEnd(){
        for(int i = 0; i < numVertices; i++){
            if(nodeList.get(i).getID().equals(startPoint))
                startIndex = i;
            if(nodeList.get(i).getID().equals(endPoint))
                endIndex = i;
        }
    }

    void findSolution() {
        int i = shortestPathNodes.size() - 1;
        while(i > 0){
            if(!shortestPathNodes.get(i).getAdjacent().contains(shortestPathNodes.get(i-1))){
                shortestPathNodes.remove(i-1);
                shortestPath.remove(i-1);
            }
            i--;
        }
    }

    void printSolution(int dist[])
    {
        System.out.println("Vertex \t\t Distance from Source");
        for (int i = 0; i < numVertices; i++)
            System.out.println(nodeList.get(i).getID() + " " + i + " \t\t " + dist[i]);
    }

    int minDistance(int dist[], Boolean sptSet[])
    {
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < numVertices; v++)
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }

    void dijkstra(int graph[][], int src)
    {
        int dist[] = new int[numVertices];

        Boolean sptSet[] = new Boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        dist[src] = 0;

        for (int count = 0; count < numVertices - 1; count++) {

            int u = minDistance(dist, sptSet);

            sptSet[u] = true;
            if(u == endIndex)
                break;

            for (int v = 0; v < numVertices; v++)

                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    Integer[] coords = new Integer[2];
                    coords[0] = nodeList.get(u).getLocX();
                    coords[1] = nodeList.get(u).getLocY();
                    shortestPath.add(coords);
                    shortestPathNodes.add(nodeList.get(u));
                }
        }

        printSolution(dist);
        findSolution();
        DisplayPath display = new DisplayPath();
        display.runProg(mapPath, shortestPath);
        display.repaint();
    }
}
