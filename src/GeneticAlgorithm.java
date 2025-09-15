import java.util.*;

public class GeneticAlgorithm {
    private final ArrayList<ArrayList<Node>> initialPopulation;
    private final int[][] edgeRelationshipMatrix;
    private final ArrayList<Node> unconnectedGraph;

    public GeneticAlgorithm(ArrayList<Node> unconnectedGraph, int[][] edgeRelationshipMatrix){
        this.edgeRelationshipMatrix = edgeRelationshipMatrix;
        this.unconnectedGraph = unconnectedGraph;

        initialPopulation = new ArrayList<>();
    }

    public double evaluateFitness(ArrayList<Integer> route){
        double totalDistance = 0;

        for (int i=0; i< route.size()-1; i++){
            totalDistance = totalDistance + unconnectedGraph.get(route.get(i)).distanceTo(unconnectedGraph.get(route.get(i+1)));


            //CANT FIX BUG OF LEAVING PATHS, so heavily penalise it
            if (edgeRelationshipMatrix[route.get(i)][route.get(i+1)]!= 1){
                totalDistance = totalDistance + 10000000;
            }
        }

        return totalDistance;
    }

    public ArrayList<Integer> crossOver3(ArrayList<Integer> route1, ArrayList<Integer> route2) {
        ArrayList<Integer> finalRoute = new ArrayList<>();
        double shortestDistance = Double.MAX_VALUE;
        int currentClosestRoute1 = 0;
        int currentClosestRoute2 = 0;

        // Find closest nodes between routes (excluding endpoints)
        for (int i = 2; i < route1.size() ; i++) {
            for (int j = 2; j < route2.size() ; j++) {
                Node node1 = unconnectedGraph.get(route1.get(i));
                Node node2 = unconnectedGraph.get(route2.get(j));
                double testDistance = node1.distanceTo(node2);

                if (testDistance < shortestDistance && testDistance > 0) {
                    shortestDistance = testDistance;
                    currentClosestRoute1 = i;
                    currentClosestRoute2 = j;
                }
            }
        }

        // Extract route segments without overlapping nodes
        List<Integer> finalRoutePart1 = route1.subList(0, currentClosestRoute1); // Excludes connection point
        List<Integer> finalRoutePart2 = route2.subList(currentClosestRoute2 + 1, route2.size()); // Excludes connection point

        // Get node indices for connection points
        int nodeA = route1.get(currentClosestRoute1);
        int nodeB = route2.get(currentClosestRoute2);

        // Generate shortest path between nodes
        List<Integer> path = createRandomRoute3(nodeA, nodeB);

        // Combine segments
        finalRoute.addAll(finalRoutePart1);
        finalRoute.addAll(path);
        finalRoute.addAll(finalRoutePart2);

        return finalRoute;
    }

    public ArrayList<Integer> getValidNextNodes(int currentNode, ArrayList<Integer> visitedNodes){
        ArrayList<Integer> validNextNodes = new ArrayList<>();

        for (int i=0; i < edgeRelationshipMatrix.length; i++){
            if (edgeRelationshipMatrix[currentNode][i] == 1 && !visitedNodes.contains(i)){
                validNextNodes.add(i);
            }
        }

        return validNextNodes;

    }

    public ArrayList<Integer> mutate2(ArrayList<Integer> route){
        ArrayList<Integer> finalRoute = new ArrayList<Integer>();

        //turn an index of route into an index of unconnectedGraph

        Random rand = new Random();
        //pick 2 indexes, draw a path between them
        int a = rand.nextInt(0,route.size());
        int b = rand.nextInt(0,route.size());

        //find value of route[index], look for that value in unconnectedGraph, get that index#
        int valueOfRouteGetA = route.get(a); //this is index of value in unconnected graph
        int valueOfRouteGetB = route.get(b);

        finalRoute.addAll(ArrayListHelp.sliceArrayListInteger(0, a,route));

        ArrayList<Integer> mutatedInbetweenRoute = createRandomRoute3(valueOfRouteGetA,valueOfRouteGetB);
        finalRoute.addAll(mutatedInbetweenRoute);
        finalRoute.addAll(ArrayListHelp.sliceArrayListInteger(b+1,route.size(), route));

        return finalRoute;
    }

    public boolean contains(ArrayList<Integer> route, Integer target){
        boolean found = false;
        int count = 0;

        while (count < route.size() && !found){
            if (route.get(count).equals(target)){
                found = true;
            }

            count++;
        }

        return found;
    }

    public int getIndex(ArrayList<Integer> route, Integer target){
        boolean found = false;
        int count = 0;

        while (count < route.size() && !found){
            if (route.get(count).equals(target)){
                found = true;
            } else {

                count++;
            }
        }

        return count;
    }

    public ArrayList<Integer> removeRedundantMoves(ArrayList<Integer> route){
        // go through route, find the number of times that a route goes through a point
        // if it goes through a node twice, cut it in between those
        // finds the biggest redundant part and cuts it.
        ArrayList<Integer> nodesVisited = new ArrayList<>();
        int startIndex =0;
        int endIndex=0;

        int finalStartIndex = 0;
        int finalEndIndex = 0;

        for (int i=0; i < route.size(); i++){
            if (contains(nodesVisited, route.get(i))){
                startIndex = getIndex(nodesVisited, route.get(i));
                endIndex = i;

                if (endIndex - startIndex >= finalEndIndex - finalStartIndex){
                    finalStartIndex = startIndex;
                    finalEndIndex = endIndex;
                }
            } else{
                nodesVisited.add(route.get(i));
            }
        }

        ArrayList<Integer> routeBeforeCutOff = ArrayListHelp.sliceArrayListInteger(0, finalStartIndex, route);// route.subList(0, finalStartIndex+1);
        ArrayList<Integer> routeAfterCutOff = ArrayListHelp.sliceArrayListInteger(finalEndIndex, route.size(), route);//.subList(finalEndIndex+1, route.size());

        ArrayList<Integer> finalRoute = new ArrayList<>();
        finalRoute.addAll(routeBeforeCutOff);
        finalRoute.addAll(routeAfterCutOff);

        return finalRoute;
    }

    //cuts a route when a node occurs twice
    public ArrayList<Integer> removeRedundantLoops(ArrayList<Integer> route) {
        boolean loopFound = true;
        ArrayList<Integer> currentRoute = route;

        while (loopFound) {
            ArrayList<Integer> nodesVisited = new ArrayList<>();
            int startIndex = -1;
            int endIndex = -1;

            int count = 0;
            int node = currentRoute.get(count);

            while (nodesVisited.contains(node)){
                if (nodesVisited.contains(node)) {
                    startIndex = nodesVisited.indexOf(node);
                    endIndex = count;
                                    }
                nodesVisited.add(node);
            }

            //indexes have been found
            if (startIndex != -1 && endIndex != -1) {
                //keep everything before startIndex+1 and after endIndex
                ArrayList<Integer> newRoute = new ArrayList<>();
                newRoute.addAll(currentRoute.subList(0, startIndex+1));
                newRoute.addAll(currentRoute.subList(endIndex+1, currentRoute.size()));
                currentRoute = newRoute;
            } else {
                loopFound = false; // No more loops
            }
        }
        return currentRoute;
    }



    //for initial routes for first generation
    // and for crossover, combining effective routes
    // random walk is too slow for 40 nodes, even 1 takes ages
    public ArrayList<Integer> createRandomRoute(int nodeA, int nodeB){
        //random walk assumes that the points chosen are already connected
        // no explicit changes to adjacencyMatrix are made
        ArrayList<Integer> routeWalked = new ArrayList<>();
        ArrayList<Integer> visitedNodes = new ArrayList<>();

        //start at a constant index
        routeWalked.add(nodeA);

        int currentIndex = 0;

        //for each step, look at adjacency Matrix, go to first available
        //for (int i=0; i < steps; i++){
        int count = 1;
        while (currentIndex != nodeB){
            //look in the row for first 1

            //adds first available edge
            for (int j=0; j < edgeRelationshipMatrix.length && !visitedNodes.contains(j); j++){
                if (edgeRelationshipMatrix[currentIndex][j] == 1){
                    routeWalked.add(edgeRelationshipMatrix[count][j]);
                    currentIndex = j;

                    visitedNodes.add(currentIndex);
                }
            }

            count++;
        }

        return routeWalked;
    }

    public ArrayList<Integer> createRandomRoute2(int nodeA, int nodeB) {
        ArrayList<Integer> routeWalked = new ArrayList<>();
        HashSet<Integer> visitedNodes = new HashSet<>();
        Random rand = new Random();

        int currentNode = nodeA;
        routeWalked.add(currentNode);
        visitedNodes.add(currentNode);
        int count = 0;

        while (currentNode != nodeB) {
            ArrayList<Integer> neighbours = new ArrayList<>();

            // Collect unvisited neighbors
            for (int j = 0; j < edgeRelationshipMatrix.length; j++) {
                //can visit previously visited nodes if thats the only option
                if (edgeRelationshipMatrix[currentNode][j] == 1 && !visitedNodes.contains(j)) {
                    neighbours.add(j);
                }
            }

            //if no neighbours available, especially if its because its already been visited
            if (neighbours.isEmpty()) {
                //System.out.println("is at a dead end");
                for (int j = 0; j < edgeRelationshipMatrix.length; j++) {
                    if (edgeRelationshipMatrix[currentNode][j] == 1 && j!=currentNode){
                        neighbours.add(j);
                    }
                }

                //go to previous node and make a different decision
                //int nextNode =

                // No more paths to explore
                //System.out.println("No path found to nodeB.");
                //break;
            }

            // Pick a random unvisited neighbor
            int nextNode = neighbours.get(rand.nextInt(neighbours.size()));
            routeWalked.add(nextNode);
            visitedNodes.add(nextNode);
            currentNode = nextNode;

            count++;
        }

        return routeWalked;
    }

    public ArrayList<Integer> createRandomRoute3(int nodeA, int nodeB) {
        ArrayList<Integer> routeWalked = new ArrayList<>();
        HashSet<Integer> visitedNodes = new HashSet<>();
        Random rand = new Random();

        int currentNode = nodeA;
        routeWalked.add(currentNode);
        visitedNodes.add(currentNode);

        int maxSteps = edgeRelationshipMatrix.length * edgeRelationshipMatrix.length; // safety cutoff
        int steps = 0;

        while (currentNode != nodeB && steps < maxSteps) {
            ArrayList<Integer> neighbours = new ArrayList<>();

            // Collect unvisited neighbors
            for (int j = 0; j < edgeRelationshipMatrix.length; j++) {
                if (edgeRelationshipMatrix[currentNode][j] == 1 && !visitedNodes.contains(j)) {
                    neighbours.add(j);
                }
            }

            // If no unvisited neighbors → allow revisiting (backtracking)
            if (neighbours.isEmpty()) {
                for (int j = 0; j < edgeRelationshipMatrix.length; j++) {
                    if (edgeRelationshipMatrix[currentNode][j] == 1 && j != currentNode) {
                        neighbours.add(j);
                    }
                }

                // If still no neighbors, graph is disconnected
                if (neighbours.isEmpty()) {
                    System.out.println("No path found from " + nodeA + " to " + nodeB);
                    break;
                }
            }

            // Pick a random neighbor (visited or not)
            int nextNode = neighbours.get(rand.nextInt(neighbours.size()));

            routeWalked.add(nextNode);
            visitedNodes.add(nextNode); // we still mark it visited even if backtracking
            currentNode = nextNode;

            steps++;
        }

        if (currentNode != nodeB) {
            System.out.println("Random walk stopped without reaching " + nodeB);
        }

        return routeWalked;
    }


    public ArrayList<Integer> removeAllOneOffLoops(ArrayList<Integer> route){
        //go through route, check the neighbours of each node, if that neighbour is in the route and not adjacent to the node in the route, make that connection
        ArrayList<Integer> finalRoute = new ArrayList<>();
        boolean cutFound = false;

        for (int i=2; i < route.size()-1; i++){
            ArrayList<Integer> possibleNeighbours = getAllNeighbours(route.get(i));

            for (int j=0; j < possibleNeighbours.size(); j++){
                //if current neighbour is not adjacent to current node within the route
                if (!possibleNeighbours.get(j).equals(route.get(i+1)) && !possibleNeighbours.get(j).equals(route.get(i-1))){
                    //if route contains this neighbour further on in the route, make that connection
                    if (route.subList(i,route.size()).contains(j)){
                        //cut array list from i to indexOf(j)
                        ArrayList<Integer> connection = new ArrayList();
                        connection.add(i);
                        connection.add(route.indexOf(j));

                        finalRoute.addAll(route.subList(0,i));
                        finalRoute.addAll(connection);
                        finalRoute.addAll(route.subList(route.indexOf(j), route.size()));

                        System.out.println("stuff being removed: "+route.subList(i-1,route.indexOf(j)+1));
                        System.out.println("replaced with: "+connection);

                        cutFound = true;
                        break;
                    }
                }
            }

            if (cutFound){
                break;
            }
        }

        if (finalRoute.isEmpty()){
            return route;
        }else{
            return finalRoute;}
    }

    public void display(ArrayList<Integer> generatedRoute){
        //Frame newFrame = new Frame();
        //newFrame.drawFrame(unconnectedGraph, edgeRelationshipMatrix, generatedRoute);

        ////GUI graphics = new GUI(unconnectedGraph, edgeRelationshipMatrix, generatedRoute);
        GUI graphics = new GUI(unconnectedGraph, edgeRelationshipMatrix);
        //JPanel of the route displayed after entered, otherwise setVisible = false;
    }

    public void initialiseFrame(){
        GUI graphics = new GUI(unconnectedGraph, edgeRelationshipMatrix);
    }

    public ArrayList<Integer> getAllNeighbours(int node){
        ArrayList<Integer> neighbours = new ArrayList<>();

        for (int i=0; i < edgeRelationshipMatrix.length; i++){
            if (edgeRelationshipMatrix[node][i] == 1){
                neighbours.add(i);
            }
        }

        return neighbours;
    }

}
