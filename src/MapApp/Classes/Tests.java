package MapApp.Classes;

import org.junit.*;

import java.io.IOException;

public class Tests {
    @Test
    public void testPaintComponent() {

        DisplayPath display = new DisplayPath();
        display.DrawPanel("src/main/resources/MappingLayoutNaked-page-0.png");
        DijkstraProcesser dijkstraProcessor;
        try {
            dijkstraProcessor = new DijkstraProcesser("A1", "A26", "src/main/resources/MapFiles/testDijkstra/NodeSource.txt");
            dijkstraProcessor.loadAdjacencyMatrix();
    } catch(IOException ex) {
            ex.printStackTrace();
        }
        display.repaint();

    }

    @Test
    public void testDrawPanel() {

        DrawPanel.runProg("src/main/resources/MapFiles/testDijkstra/NodeSource.txt");
        DrawPanel drawPanel;
        drawPanel = new DrawPanel("src/main/resources/MappingLayoutNaked-page-0.png");
        drawPanel.repaint();
        drawPanel.getNodes();

    }

    @Test (expected = NullPointerException.class)
    public void testFail() {

       DijkstraProcesser dijkstraProcessor = new DijkstraProcesser("A1", "A26", "src/main/resources/MapFiles/testDijkstra/NodeSources.txt");

       try {
           dijkstraProcessor.loadAdjacencyMatrix();
       } catch(IOException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void clearCoverage(){
    }

}
