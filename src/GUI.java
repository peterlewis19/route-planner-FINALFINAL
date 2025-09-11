import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GUI extends JFrame {
    private JLabel label;
    private JTextField dataEntry;
    private ArrayList<Node> allNodes;
    private int[][] adjMatrix;
    private ArrayList<Integer> generatedRoute;
    private static int minIndex;// = 0;
    private static int maxIndex;

    public GUI(ArrayList<Node> allNodes, int[][] adjMatrix) {
        setTitle("Route Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(null);
        setBounds(150, 50, 800, 600);

        maxIndex = allNodes.size() - 1;
        minIndex = 0;


        JLabel fieldCaption = new JLabel("Enter your start location: ");
        fieldCaption.setBounds(10, 10, 200, 30);

        JTextField startEntry = new JTextField();
        startEntry.setBounds(160, 10, 200, 30);
        //startEntry.setInputVerifier(new ValidateInputIndex());

        JLabel destinationCaption = new JLabel("Enter your destination: ");
        destinationCaption.setBounds(370, 10, 200, 30);

        JTextField destinationEntry = new JTextField();
        destinationEntry.setBounds(510, 10, 200, 30);
        //startEntry.setInputVerifier(new ValidateInputIndex());


        JButton goButton = new JButton("GO");
        goButton.setBounds(720, 10, 55, 30);


        JPanel panel = new JPanel();
        panel.setBackground(Color.lightGray);
        panel.setBounds(0, 0, 800, 50);


        //Dont add the canvas until after
        //Draw Nodes then draw route once it has been entered
        Canvas backgroundMap = new Canvas(allNodes, adjMatrix, generatedRoute);
        backgroundMap.setBounds(10, 70, 800, 560);
        backgroundMap.setOpaque(false);

        RouteCanvas routeDrawing = new RouteCanvas(generatedRoute, allNodes);
        routeDrawing.setBounds(10, 70, 800, 560);
        routeDrawing.setBackground(Color.white);
        routeDrawing.setOpaque(false);

        //add(layeredPane);
        add(fieldCaption);
        add(startEntry);
        add(destinationCaption);
        add(destinationEntry);
        add(backgroundMap);
        add(routeDrawing);
        add(panel);
        add(goButton);
        backgroundMap.setVisible(true);
        routeDrawing.setVisible(false);

        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //this takes the input from the textfields and turns them to a digit
                System.out.println(startEntry.getText() + "," + destinationEntry.getText());

                //validate input is an integer
                boolean isInt = false;
                int startIndex = 0;
                int endIndex = 1;

                //if (!isInt){
                try {
                    startIndex = Integer.parseInt(startEntry.getText());
                    endIndex = Integer.parseInt(destinationEntry.getText());
                    isInt = true;
                } catch (NumberFormatException error) {
                    isInt = false;
                    //createWindow();
                }//}

                if (isInt) {
                    //if input is invalid, give error message
                    if (startIndex < 0 || startIndex > maxIndex - 2 || endIndex < 0 || endIndex > maxIndex - 2) {
                        System.out.println("ERROR IN INPUT");
                        createWindow();
                    } else {
                        MainLoopOfGA newGA = new MainLoopOfGA(Integer.parseInt(startEntry.getText()), Integer.parseInt(destinationEntry.getText()));
                        ArrayList<Integer> bestRoute = newGA.getBestRoute();

                        generatedRoute = bestRoute;

                        backgroundMap.setVisible(false);

                        routeDrawing.setRoute(bestRoute);
                        routeDrawing.setVisible(true);
                        routeDrawing.repaint();

                        backgroundMap.repaint();
                        backgroundMap.setVisible(true);

                        System.out.println("displaying map...");


                        try {
                            BufferedImage screenshot = getScreenshotOfBothCanvases(backgroundMap, routeDrawing);
                            // write the image as a PNG
                            ImageIO.write(
                                    screenshot,
                                    "png",
                                    new File("C:\\Users\\Peter Lewis\\Code\\IA Research\\ga-route-planner\\screenshot.png"));
                        } catch(Exception err) {
                            err.printStackTrace();
                        }

                    }
                } else {
                    createNoneIntWindow();
                }


                //}
            }
        });


        getContentPane().setComponentZOrder(goButton, 0);
        setVisible(true);
    }

    private static void createWindow() {
        JFrame frame = new JFrame("Input out of range");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI(frame);
        frame.setSize(300, 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createNoneIntWindow() {
        JFrame frame = new JFrame("Input is not an integer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createNoneIntUI(frame);
        frame.setSize(300, 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createUI(final JFrame frame) {
        JPanel panel = new JPanel();
        //LayoutManager layout = new FlowLayout();
        //panel.setLayout(layout);
        //JButton button = new JButton("Click Me!");
        JLabel warningMessage = new JLabel("Input has to be between " + minIndex + " and " + (maxIndex - 2));
        warningMessage.setHorizontalAlignment(JLabel.CENTER);
        frame.add(warningMessage);

        frame.setDefaultCloseOperation(HIDE_ON_CLOSE);

    }

    private static void createNoneIntUI(final JFrame frame) {
        JPanel panel = new JPanel();
        //LayoutManager layout = new FlowLayout();
        //panel.setLayout(layout);
        //JButton button = new JButton("Click Me!");
        JLabel warningMessage = new JLabel("Input must be a number, such as 1 or 23");
        warningMessage.setHorizontalAlignment(JLabel.CENTER);
        frame.add(warningMessage);

        frame.setDefaultCloseOperation(frame.HIDE_ON_CLOSE);

    }

    public BufferedImage getScreenShot(Component component) {
        //RouteCanvas routeDrawing = new RouteCanvas(generatedRoute, allNodes);
        //component.getRoute();
        //component.setBackground(Color.white);

        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        // call the Component's paint method, using
        // the Graphics object of the image.
        component.printAll(image.getGraphics());
                //paint(image.getGraphics()); // alternately use .printAll(..)
        return image;

    }

    public BufferedImage getScreenshotOfBothCanvases(Component component1, Component component2) {
        int width = Math.max(component1.getWidth(), component2.getWidth());
        int height = Math.max(component1.getHeight(), component2.getHeight());

        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = combined.createGraphics();

        // Set white background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);


        // Paint route on top (if it exists and is visible)
        //System.out.println("BOTH HOSUDL BE TRUE");
        //System.out.println("SHOULD BE TRUE"+(generatedRoute != null));
        //System.out.println(!generatedRoute.isEmpty());
        if (generatedRoute != null) {
            component2.printAll(g2d);
            //System.out.println("BEING ADDED");
        }

        // Paint background map first
        component1.printAll(g2d);

        g2d.dispose();
        return combined;
    }
}
