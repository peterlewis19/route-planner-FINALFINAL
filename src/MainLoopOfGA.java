import java.util.*;

public class MainLoopOfGA {
    private ArrayList<Integer> bestRoute;
    public MainLoopOfGA(int START_INDEX, int END_INDEX){
        //ArrayList<Node> unconnectedMapOfCoords = FileHandler.readNodesFromFile("\\\\BEX-FILE-01\\studenthome$\\19\\19lewis_p\\COMP SCI IA\\mapOfCoords500.txt");
        //int[][] edgeRelationMatrix = FileHandler.readMatrixFromFile("\\\\BEX-FILE-01\\studenthome$\\19\\19lewis_p\\COMP SCI IA\\adjacencyMatrix500.txt", unconnectedMapOfCoords.size());

        ArrayList<Node> unconnectedMapOfCoords = FileHandler.readNodesFromFile("C:\\Users\\Peter Lewis\\Code\\IA Research\\route-planner-FINALFINAL\\500mapOfCoords.txt");
        int[][] edgeRelationMatrix = FileHandler.readMatrixFromFile("C:\\Users\\Peter Lewis\\Code\\IA Research\\route-planner-FINALFINAL\\500edgeRelations.txt", unconnectedMapOfCoords.size());

        GeneticAlgorithm ga = new GeneticAlgorithm(unconnectedMapOfCoords, edgeRelationMatrix);

        int routesPerGeneration = 100;
        int bestNofGeneration = 15;
        int nOfGenerations = 25;

        ArrayList<ArrayList<Integer>> allRoutesInThisGeneration = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> allRoutesInThisGenerationWithNoOneOffLoops = new ArrayList<ArrayList<Integer>>();

        //initialises routes
        for (int i = 0; i < routesPerGeneration; i++) {
            //System.out.println("RANDOMISING A ROUTE...");
            ArrayList<Integer> randomRoute = ga.createRandomRoute3(START_INDEX, END_INDEX);

            HashSet<Integer> setOfRandomRoute = new HashSet<>(randomRoute);

            //now perfect
            ArrayList<Integer> routeWithNoLoops = ga.removeRedundantLoops(randomRoute);


            allRoutesInThisGeneration.add(routeWithNoLoops);
        }

        allRoutesInThisGeneration.sort(Comparator.comparingDouble(ga::evaluateFitness));

        //shortest N of the generation
        ArrayList<ArrayList<Integer>> bestRoutesInGeneration = ArrayListHelp.sliceArrayList(0, bestNofGeneration, allRoutesInThisGeneration);

        Random rand = new Random();


        //Main loop of the Genetic Algorithm
        for (int i = 0; i < nOfGenerations; i++) {
            //take best N
            // combine them into say 100 routes, take N best and repeat until small

            ArrayList<ArrayList<Integer>> currentGenerationOfRoutes = new ArrayList<ArrayList<Integer>>();

            //crosses over every single possible route between all routes in array
            for (int route1 = 0; route1 < bestNofGeneration; route1++) {
                for (int route2 = 0; route2 < bestNofGeneration; route2++) {
                    ArrayList<Integer> route = ga.crossOver3(bestRoutesInGeneration.get(route1), bestRoutesInGeneration.get(route2));

                    //mutate maybe 3% of routes
                    if (rand.nextInt(100) <= 4) {
                        route = ga.mutate2(route);
                    }

                    route = ga.removeRedundantMoves(route);
                    currentGenerationOfRoutes.add(route);
                }
            }
            Collections.sort(currentGenerationOfRoutes, Comparator.comparingDouble(ga::evaluateFitness));
            bestRoutesInGeneration = ArrayListHelp.sliceArrayList(0, bestNofGeneration, currentGenerationOfRoutes);
        }
        bestRoute = bestRoutesInGeneration.get(0);
    }


    public ArrayList<Integer> getBestRoute(){
        return bestRoute;
    }
}
