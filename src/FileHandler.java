import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    //adds all Nodes from the input file
    public static ArrayList<Node> readNodesFromFile(String fileName){
        ArrayList<Node> allNodes = new ArrayList<>();
        String line = "";

        try (
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr);
        ) {
            line = br.readLine();

            while (line != null){
                //cuts off line to only include first Node (home node)
                //becomes "[xx,yy]"
                //String homeNode = line.split("-")[0];

                //System.out.println(homeNode);

                //becomes "xx", "yy"
                //String[] formattedNode = homeNode.substring(1,homeNode.length()-1).split(",");
                //System.out.println(formattedNode[0] + ", "+ formattedNode[1]);

                //separate x and y from original String
                //double x = Double.parseDouble(formattedNode[0]);
                //double y = Double.parseDouble(formattedNode[1]);

                String rawCoord = line; // no brackets in current data
                //.substring(0,line.length()-2); // cuts off the brackets at start and end
                String rawX = rawCoord.split(",")[0];
                String rawY = rawCoord.split(",")[1];

                double x = Double.parseDouble(rawX);
                double y = Double.parseDouble(rawY);


                Node newNode = new Node(new double[]{x,y});
                allNodes.add(newNode);

                line=br.readLine();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return allNodes;
    }

    public static int[][] readMatrixFromFile(String fileName, int size){
        int[][] adjMat = new int[size][size];
        String line = "";

        try (
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr);
        ) {
            line = br.readLine();

            int count = 0;
            while (line != null){
                String currentNodeConnections = line;

                //gets each part of matrix individually
                for (int i=0; i < currentNodeConnections.length()-2; i++){
                    adjMat[count][i] = Integer.parseInt(currentNodeConnections.substring(i,i+1));
                }

                line=br.readLine();
                count++;
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return adjMat;
    }
}

