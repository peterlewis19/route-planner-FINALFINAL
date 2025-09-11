import java.util.*;

public class GeneticAlgorithm {
    private final ArrayList<ArrayList<Node>> initialPopulation;
    //private final String mapFileName;
    private final int[][] edgeRelationshipMatrix;
    private final ArrayList<Node> unconnectedGraph;
    //private final Node STARTNODE;
    //private final Node FINALNODE;

    public GeneticAlgorithm(ArrayList<Node> unconnectedGraph, int[][] edgeRelationshipMatrix){
        //STARTNODE = startNode;
        //FINALNODE = finalNode;
        //this.mapFileName = mapFileName;
        this.edgeRelationshipMatrix = edgeRelationshipMatrix;
        this.unconnectedGraph = unconnectedGraph;

        initialPopulation = new ArrayList<>();
    }

    /*public ArrayList<ArrayList<Node>> generateInitialPopulation(int sizeOfPopulation){
        Node destination;
        Node current;

        ArrayList<Integer> neighbourIDsOfANode;// = new ArrayList<>();

        //initialises the map of coordinate data
        //Map graph = new Map(FileHandler.readNodesFromFile(fileName));
        /*for (int i=0; i < graph.size(); i++){
            System.out.println(graph.get(i));
        }

        destination = graph.get(graph.size()-2);
        current = graph.get(0);

        //System.out.println(current +" to "+ destination);

        //adds the neighbours to each node
        for (int i=0; i< graph.size(); i++){
            Node node = graph.get(i);
            ArrayList<Node> neighboursOfThisNode = FileHandler.readNeighboursFromNode(node, fileName);
            //System.out.println(node + ": ");

            for (Node neighbour: neighboursOfThisNode){
                //gets the ID for the Node in the graph which shares the coords with the neighbour Node in neighoursOfThisNode
                node.addNeighbour(graph.getIDByCoords(neighbour.getCoords()));

                //System.out.print(Arrays.toString(neighbour.getCoords()));
            }
            //System.out.println();
            //System.out.println();
        }

        int nextNodeID;
        Random random = new Random();
        //from currentNode, checks neighbours and makes decision based off that
        //current basis is randomness for ease

        ArrayList<Node> finalRoute = new ArrayList<>();

        //creates n routes for the initial pop, testing phase
        for (int i=0; i < sizeOfPopulation; i++) {
            finalRoute = new ArrayList<>();
            //resets the route being stored every time, starting from the start
            current = graph.get(0);
            finalRoute.add(current);
            //System.out.println("CURRENT: "+ current);

            while (!current.equals(destination)) {
                neighbourIDsOfANode = current.getNeighbours();

                /*System.out.println("IDS OF NEIGHBOURS: ");
                for (int ID : neighbourIDsOfANode) {
                    System.out.print(ID + ",");
                }
                System.out.println();

                //current has become the next Node
                //chooses a random neighbour ID, then moves to it
                // TODO:
                //   - THIS IS RETURNING -1: FIX IT!!!
                nextNodeID = neighbourIDsOfANode.get(random.nextInt(0, neighbourIDsOfANode.size()));
                //the ID found for the chosen neighbour isn't currently found in the graph
                current = graph.getNodeByID(nextNodeID);

                //adds this stop to the route
                finalRoute.add(current);
            }

            initialPopulation.add(finalRoute);

        }
        return initialPopulation;
    }*/

    public double evaluateFitness(ArrayList<Integer> route){
        double totalDistance = 0;

        for (int i=0; i< route.size()-1; i++){
            totalDistance = totalDistance + unconnectedGraph.get(route.get(i)).distanceTo(unconnectedGraph.get(route.get(i+1)));
        }

        return totalDistance;
    }
    //just returns route1 atm
    /*public ArrayList<Node> crossOver(ArrayList<Node> route1, ArrayList<Node> route2){
        ArrayList<Node> finalRoute = new ArrayList<>();
        List<Node> finalRoutePart1;
        List<Node> finalRoutePart2;
        int count=1;
        boolean found = false;
        int route1MutualNodeIndex = 0;
        int route2MutualNodeIndex = 0;

        boolean containsOtherRouteNode = false;

        //checks if the 2 routes have a mutual node
        //i = 1 and  length -1 because teh start and bc start and end will be the same (START AND DESTINATION)
        for (int i=1; i < route1.size()-1; i++){
            for (int j=1;  j < route2.size()-1; j++){
                if (route1.get(i) == route2.get(j)){
                    containsOtherRouteNode = true;
                    route1MutualNodeIndex = i;
                    route2MutualNodeIndex = j;
                }
            }
        }

        //split them in half and  connect there
        //1. based on them connecting
        //2. if not, based on the neighbour

        //based on them having a mutual Node, apart from first and last
        if (containsOtherRouteNode) {
            //found = true;
            //final Route is firstly route1, secondly route2
            finalRoutePart1 = route1.subList(0, route1MutualNodeIndex);
            finalRoutePart2 = route2.subList(route2MutualNodeIndex, route2.size());
            finalRoute.addAll(finalRoutePart1);
            finalRoute.addAll(finalRoutePart2);

        } else{
            //based on the neighbours
            //goes through all of route1, gets the neighbour of each Node
            // checks if this neighbour is in route2
            /*
            * ORRRRR IMPLEMENT PRIMS
            * LOOK AT ALL POINTS ON ROUTE, then DFS TO CLOSEST NODE,
            * evaluating distance, by going through all neighbours
            *  and then comparing their distance to all points
            *
            * */

    //PRIMS ALGORITHM
            /* 1. iterate through route1, find the point where it is closest to
            * any Node of route2
            * 2. from that point, evaluate the neighbours of that node
            * 3. go to the neighbour closest to any Node of route2
            * 4. repeat until they reach route2
            *
            //find the node at which route 1 and 2 are closest.
            // as long as it is more than 0
            double shortestDistance = 10000;
            int route1ClosestNodeIndex = 0;
            int route2ClosestNodeIndex = 0;

            double currentDistance;
            //to ensure that the closest nodes aren't side by side, as they are at the beginning and end
            int NODES_BEFORE_CHECKING_DISTANCE = 2;

            for (int i = NODES_BEFORE_CHECKING_DISTANCE; i < route1.size()-NODES_BEFORE_CHECKING_DISTANCE; i++){

                //would it be worth saving some loops??
                // as the closest NOde is likely to have a similar index in both routes,
                // could I only check +or- 2 of the current index???
                // maybe but well see how slow this is.
                for (int j = NODES_BEFORE_CHECKING_DISTANCE; i < route2.size() - NODES_BEFORE_CHECKING_DISTANCE; j++){
                    currentDistance = route1.get(i).distanceTo(route2.get(j));

                    if (currentDistance < shortestDistance){
                        //points which are closest together are found
                        shortestDistance = currentDistance;
                        route1ClosestNodeIndex = i;
                        route2ClosestNodeIndex = j;
                    }
                }
            }

            Node currentNode;
            Node targetNode;

            currentNode = route1.get(route1ClosestNodeIndex);
            targetNode = route2.get(route2ClosestNodeIndex);

            ArrayList<Node> connectingPart = new ArrayList<>();

            //shortest distance is used now for each Node to target Node
            //shortestDistance = 10000;
            //currentDistance is now from each Node to target Node
            //currentDistance = 0;

            //now uve got the 2 closest points, work out route between, using DFS
            while (currentNode.equals(route2.get(route2ClosestNodeIndex))){
                ArrayList<Integer> IDsOfNeighboursOfCurrentNode = currentNode.getNeighbours();
                Node tempNode;
                shortestDistance = 10000;
                currentDistance = 0;
                int potentialNodeIDIndex = 0;

                //for picking next node
                for (int i=0; i < IDsOfNeighboursOfCurrentNode.size(); i++){
                    tempNode = unconnectedGraph.get(IDsOfNeighboursOfCurrentNode.get(i));
                    currentDistance = tempNode.distanceTo(targetNode);

                    //found new closest current Node
                    if (currentDistance < shortestDistance){
                        shortestDistance = currentDistance;
                        potentialNodeIDIndex = i;
                    }
                }

                currentNode = unconnectedGraph.get(potentialNodeIDIndex);
                connectingPart.add(currentNode);
            }

            finalRoutePart1 = route1.subList(0, route1MutualNodeIndex);
            finalRoutePart2 = route2.subList(route2MutualNodeIndex, route2.size());

            finalRoute.addAll(finalRoutePart1);
            finalRoute.addAll(connectingPart);
            finalRoute.addAll(finalRoutePart2);
        }

        return finalRoute;
    }*/

    public ArrayList<Integer> crossOver2(ArrayList<Integer> route1, ArrayList<Integer> route2){
        ArrayList<Integer> finalRoute = new ArrayList<>();
        List<Integer> finalRoutePart1;
        List<Integer> finalRoutePart2;
        List<Integer> routeInbetweenNodes;

        //option 1:
        // take where 2 routes are closest (excluding first 2 and last 2 nodes) and connect with random walk

        double shortestDistance = 10000;
        int currentClosestRoute1 = 0;
        int currentClosestRoute2 = 0;


        //routes being crossed over
        /*System.out.println("ROUTES BEING CROSSED OVER:");
        for (int i=0; i < route1.size(); i++){
            System.out.print(route1.get(i)+", ");
        }
        System.out.println();
        for (int i=0; i < route2.size(); i++){
            System.out.print(route2.get(i)+", ");
        }
        System.out.println();*/



        //display nodes of routes
        /*for (int i=0; i < route1.size(); i++){
            int nodeOfRoute1 = unconnectedGraph.indexOf(unconnectedGraph.get(route1.get(i)));

            System.out.print(unconnectedGraph.get(route1.get(i))+",");
            System.out.println(unconnectedGraph.get(nodeOfRoute1));
            System.out.println();

        }*/

        //finds the closest points of each route
        for (int i=2; i < route1.size()-2; i++){
            for (int j=2; j < route2.size()-2; j++){
                // gets index of ith of route1 in the unconnectedGraph
                int nodeOfRoute1 = unconnectedGraph.indexOf(unconnectedGraph.get(route1.get(i)));
                int nodeOfRoute2 = unconnectedGraph.indexOf(unconnectedGraph.get(route2.get(j)));

                double testDistance = unconnectedGraph.get(nodeOfRoute1).distanceTo(unconnectedGraph.get(nodeOfRoute2));
                if (testDistance < shortestDistance && unconnectedGraph.get(i).distanceTo(unconnectedGraph.get(j)) > 0){
                    shortestDistance = testDistance;
                    currentClosestRoute1 = i;
                    currentClosestRoute2 = j;
                }
            }
        }

        //having found the 2 closest nodes along each route, now connect between them
        finalRoutePart1 = route1.subList(0,currentClosestRoute1);
        finalRoutePart2 = route2.subList(currentClosestRoute2, route2.size());

        //now connect it
        routeInbetweenNodes = createRandomRoute3(currentClosestRoute1, currentClosestRoute2);

        //adding each segment to final route
        finalRoute.addAll(finalRoutePart1);
        finalRoute.addAll(routeInbetweenNodes);
        finalRoute.addAll(finalRoutePart2);

        return finalRoute;

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

    public ArrayList<Integer> crossOver4(ArrayList<Integer> route1, ArrayList<Integer> route2) {
        ArrayList<Integer> finalRoute = new ArrayList<>();
        double shortestDistance = Double.MAX_VALUE;
        int currentClosestRoute1 = -1;
        int currentClosestRoute2 = -1;

        // Find closest nodes between routes (excluding endpoints)
        for (int i = 1; i < route1.size() - 1; i++) {
            for (int j = 1; j < route2.size() - 1; j++) {
                Node node1 = unconnectedGraph.get(route1.get(i));
                Node node2 = unconnectedGraph.get(route2.get(j));

                // You may want to check connectivity first, not just distance
                double testDistance = node1.distanceTo(node2);

                if (testDistance < shortestDistance && testDistance > 0) {
                    shortestDistance = testDistance;
                    currentClosestRoute1 = i;
                    currentClosestRoute2 = j;
                }
            }
        }

        if (currentClosestRoute1 == -1 || currentClosestRoute2 == -1) {
            // no valid crossover found → return one of the parents unchanged
            return new ArrayList<>(route1);
        }

        // Get node indices for connection points
        int nodeA = route1.get(currentClosestRoute1);
        int nodeB = route2.get(currentClosestRoute2);

        // Generate path between nodes (must validate)
        List<Integer> path = createRandomRoute3(nodeA, nodeB);

        if (path == null || path.isEmpty()
                || path.get(0) != nodeA
                || path.get(path.size() - 1) != nodeB) {
            // fallback: no valid crossover
            return new ArrayList<>(route1);
        }

        // Extract route segments safely
        List<Integer> finalRoutePart1 = new ArrayList<>(route1.subList(0, currentClosestRoute1));
        List<Integer> finalRoutePart2 = new ArrayList<>(route2.subList(currentClosestRoute2 + 1, route2.size()));

        // Combine segments
        finalRoute.addAll(finalRoutePart1);
        finalRoute.addAll(path);
        finalRoute.addAll(finalRoutePart2);

        return finalRoute;
    }


    public ArrayList<Integer> getValidNextNodes(int currentNode, ArrayList<Integer> visitedNodes){
        ArrayList<Integer> validNextNodes = new ArrayList<>();// = new List<>();

        for (int i=0; i < edgeRelationshipMatrix.length; i++){
            if (edgeRelationshipMatrix[currentNode][i] == 1 && !visitedNodes.contains(i)){
                validNextNodes.add(i);
            }
        }

        return validNextNodes;

    }

    public ArrayList<Integer> mutate(ArrayList<Integer> route) {
        Random rand = new Random();

        // Choose a cut point
        int cutIndex = rand.nextInt(route.size() - 1); // avoid cutting at the very end

        // Keep the prefix as-is
        ArrayList<Integer> mutated = new ArrayList<>(route.subList(0, cutIndex + 1));
        int currentNode = mutated.get(mutated.size() - 1);

        // Now, complete the rest of the route via valid random walk
        while (mutated.size() < route.size()) {
            ArrayList<Integer> validNextNodes = getValidNextNodes(currentNode, mutated);
            //System.out.println("SIZE OF VALID NODES:"+validNextNodes.size());
            if (validNextNodes.isEmpty()) {
                // Dead end, restart mutation from scratch
                mutate(route);
                //return;
            }
            int nextNode = validNextNodes.get(rand.nextInt(validNextNodes.size()));
            mutated.add(nextNode);
            currentNode = nextNode;
        }

        // Replace old route with mutated one
        route.clear();
        route.addAll(mutated);

        return route;
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

        //int indexInUnconnectedGraphOfRouteGetA = unconunconnectedGraph.get(valueOfRouteGetA);
        // int indexInUnconnectedGraphOfRouteGetB = unconnectedGraph.get(valueOfRouteGetB);


        //int aInUnconnectedMap = unconnectedGraph.indexOf(unconnectedGrap);
        //int bInUnconnectedMap = unconnectedGraph.indexOf(b);

        finalRoute.addAll(ArrayListHelp.sliceArrayListInteger(0, a,route));

        ArrayList<Integer> mutatedInbetweenRoute = createRandomRoute3(valueOfRouteGetA,valueOfRouteGetB);
        finalRoute.addAll(mutatedInbetweenRoute);
        finalRoute.addAll(ArrayListHelp.sliceArrayListInteger(b+1,route.size(), route));

        return finalRoute;
    }
    public ArrayList<Integer> mutate3(ArrayList<Integer> route) {
        ArrayList<Integer> finalRoute = new ArrayList<>();
        Random rand = new Random();

        // pick 2 indexes, ensure a < b
        int a = rand.nextInt(route.size());
        int b = rand.nextInt(route.size());
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        if (a == b) return new ArrayList<>(route); // no mutation possible

        int startNode = route.get(a);
        int endNode = route.get(b);

        // prefix stays the same
        finalRoute.addAll(ArrayListHelp.sliceArrayListInteger(0, a, route));

        // try to mutate between a and b
        ArrayList<Integer> mutatedInbetweenRoute = createRandomRoute3(startNode, endNode);

        if (mutatedInbetweenRoute == null || mutatedInbetweenRoute.isEmpty()
                || mutatedInbetweenRoute.get(0) != startNode
                || mutatedInbetweenRoute.get(mutatedInbetweenRoute.size()-1) != endNode) {
            // fallback to original segment if no valid path found
            mutatedInbetweenRoute = ArrayListHelp.sliceArrayListInteger(a, b+1, route);
        }

        finalRoute.addAll(mutatedInbetweenRoute);

        // suffix stays the same
        finalRoute.addAll(ArrayListHelp.sliceArrayListInteger(b+1, route.size(), route));

        return finalRoute;
    }



    private List<Integer> findShortestPath(int startNode, int endNode) {
        // Implement Dijkstra's/A* here
        // Placeholder: Direct connection (replace with actual pathfinding)
        return Arrays.asList(startNode, endNode);
    }


    //if either one is a neighbour of the other
    /*public boolean isNeighbour(Node node1, Node node2){
        ArrayList<Integer> node1NeighboursIndex = node1.getNeighbours();
        boolean isNeighbour = false;

        for (int ID: node1NeighboursIndex){
            if (node2.getID() == ID){
                isNeighbour = true;
                return isNeighbour;
            }
        }

        return isNeighbour;
    }*/

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
    /* TODO: make it run until there are no remaining loops, not just removing one loop (while condition setLength != routeLength)
     * */
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

        /*System.out.println();
        System.out.println("Cuts between:");
        System.out.println(finalStartIndex +","+finalEndIndex);
        System.out.println(route.get(finalStartIndex) +", "+ route.get(finalEndIndex));*/

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
