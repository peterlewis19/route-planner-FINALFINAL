import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //ArrayList<Node> unconnectedMapOfCoords = FileHandler.readNodesFromFile("\\\\BEX-FILE-01\\studenthome$\\19\\19lewis_p\\COMP SCI IA\\mapOfCoords500.txt");
        //int[][] edgeRelationMatrix = FileHandler.readMatrixFromFile("\\\\BEX-FILE-01\\studenthome$\\19\\19lewis_p\\COMP SCI IA\\adjacencyMatrix500.txt", unconnectedMapOfCoords.size());

        ArrayList<Node> unconnectedMapOfCoords = FileHandler.readNodesFromFile("welling_road_names.csv");
        int[][] edgeRelationMatrix = FileHandler.readMatrixFromFile("welling_adj_matrix.csv", unconnectedMapOfCoords.size());

        GUI graphics = new GUI(unconnectedMapOfCoords, edgeRelationMatrix);
    }

}