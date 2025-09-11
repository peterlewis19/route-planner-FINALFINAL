import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //ArrayList<Node> unconnectedMapOfCoords = FileHandler.readNodesFromFile("\\\\BEX-FILE-01\\studenthome$\\19\\19lewis_p\\COMP SCI IA\\mapOfCoords500.txt");
        //int[][] edgeRelationMatrix = FileHandler.readMatrixFromFile("\\\\BEX-FILE-01\\studenthome$\\19\\19lewis_p\\COMP SCI IA\\adjacencyMatrix500.txt", unconnectedMapOfCoords.size());

        ArrayList<Node> unconnectedMapOfCoords = FileHandler.readNodesFromFile("C:\\Users\\Peter Lewis\\Code\\IA Research\\route-planner-FINALFINAL\\500mapOfCoords.txt");
        int[][] edgeRelationMatrix = FileHandler.readMatrixFromFile("C:\\Users\\Peter Lewis\\Code\\IA Research\\route-planner-FINALFINAL\\500edgeRelations.txt", unconnectedMapOfCoords.size());



        //MainLoopOfGA newGA = new MainLoopOfGA();
        GUI graphics = new GUI(unconnectedMapOfCoords, edgeRelationMatrix);
    }

}