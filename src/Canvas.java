import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas extends JComponent {

    private ArrayList<Node> allNodes;
    private int[][] adjMatrix;
    private double HORIZONTAL_SCALE_FACTOR = 0.9;
    private double VERTICAL_SCALE_FACTOR = 0.8;
    private ArrayList<Integer> generatedRoute;

    public Canvas(ArrayList<Node> allNodes, int[][] adjMatrix, ArrayList<Integer> generatedRoute){
        this.adjMatrix = adjMatrix;
        this.allNodes = allNodes;
        this.generatedRoute = generatedRoute;
    }

    //adjacency matrix, for drawing the connections of nodes????

    /*
    0000000
    0001000
    0000000
    0100000
    0000000
    0000000
    0000000

    this shows that the only edge is between Nodes 1 and 3
    notice the line of symmetry along the diagonal
    so only need to read on diagonal half of it

    for (int i = 0; i < adjMatrix; i++){
        for (int j = i; j < adjMatrix; j++){
            //this traverses only the part above the line of symmetry
        }
    }
    *
    * */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set rendering hints for smooth lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set line color and thickness
        g2d.setColor(Color.BLACK);
        g2d.setBackground(Color.pink);
        g2d.setStroke(new BasicStroke(5)); // Line thickness = 2

        //just drawing dots, so start and end X and Y will be the same
        for (int i=0; i < allNodes.size(); i++){

            int startY = (int) ((VERTICAL_SCALE_FACTOR*allNodes.get(i).getX()));
            int startX = (int) ((HORIZONTAL_SCALE_FACTOR*allNodes.get(i).getY()));

            int endY = startY ;
            int endX = startX;

            g.drawLine(startX, startY, endX, endY);
            g.drawString(String.valueOf(i),startX,startY);
        }


        //draws a line between teh 2 closest points

        //draws lines based on an adjacency matrix
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        //this traverses only the part above the line of symmetry
        for (int i = 0; i < adjMatrix.length; i++){
            for (int j = i; j < adjMatrix[0].length; j++){
                //if the row and column share an edge
                if (adjMatrix[i][j] == 1){
                    int startY = (int) (VERTICAL_SCALE_FACTOR*allNodes.get(i).getX()) ;
                    int startX = (int) (HORIZONTAL_SCALE_FACTOR*allNodes.get(i).getY());

                    int endY = (int) (VERTICAL_SCALE_FACTOR*allNodes.get(j).getX());
                    int endX = (int) (HORIZONTAL_SCALE_FACTOR*allNodes.get(j).getY());

                    g.drawLine(startX, startY, endX, endY);
                }
            }
        }
    }

    public static BufferedImage getScreenShot(
            Component component) {

        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        // call the Component's paint method, using
        // the Graphics object of the image.
        component.paint( image.getGraphics() ); // alternately use .printAll(..)
        return image;
    }
}

