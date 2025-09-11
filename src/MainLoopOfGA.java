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
        int nOfGenerations = 15;

        //ga.initialiseFrame();

        //int START_INDEX = 3;
        //int END_INDEX = 35;

        ArrayList<ArrayList<Integer>> allRoutesInThisGeneration = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> allRoutesInThisGenerationWithNoOneOffLoops = new ArrayList<ArrayList<Integer>>();

        //THIS LOOP IS FULLY WORKING
        for (int i = 0; i < routesPerGeneration; i++) {
            //System.out.println("RANDOMISING A ROUTE...");
            ArrayList<Integer> randomRoute = ga.createRandomRoute3(START_INDEX, END_INDEX);

            HashSet<Integer> setOfRandomRoute = new HashSet<>(randomRoute);
            /// NEW IDEA: generate allowing it to overlap, just remove redundant loops

            //now perfect
            ArrayList<Integer> routeWithNoLoops = ga.removeRedundantLoops(randomRoute);


            //allRoutesInThisGenerationWithNoOneOffLoops.add(ga.removeAllOneOffLoops(routeWithNoLoops));
            allRoutesInThisGeneration.add(routeWithNoLoops);
        }

        allRoutesInThisGeneration.sort(Comparator.comparingDouble(ga::evaluateFitness));
        //allRoutesInThisGenerationWithNoOneOffLoops.sort(Comparator.comparingDouble(ga::evaluateFitness));
        //System.out.println("Shortest Route"+ allRoutesInThisGeneration.get(0));

        //System.out.println("ERROR HERE"+ga.evaluateFitness(allRoutesInThisGenerationWithNoOneOffLoops.get(0)));

        //shortest N of the generation
        ArrayList<ArrayList<Integer>> bestRoutesInGeneration = ArrayListHelp.sliceArrayList(0, bestNofGeneration, allRoutesInThisGeneration);
        //ArrayList<ArrayList<Integer>> bestRoutesInGenerationNo1Offs = ArrayListHelp.sliceArrayList(0, bestNofGeneration, allRoutesInThisGenerationWithNoOneOffLoops);


        /*System.out.println("LENGTH OF INITIAL ROUTES");
        for (int i = 0; i < bestRoutesInGeneration.size(); i++) {
            //System.out.println(ga.evaluateFitness(bestRoutesInGeneration.get(i)));
            ga.display(bestRoutesInGeneration.get(i));
            //System.out.println(bestRoutesInGenerationNo1Offs.get(i));
            //ga.display(bestRoutesInGenerationNo1Offs.get(i));
        }*/

        Random rand = new Random();


        //ERROR SOMEWHERE IN THIS LOOP
        for (int i = 0; i < nOfGenerations; i++) {
            //take best N
            // combine them into say 100 routes, take N best and repeat until small
            //allRoutesInThisGeneration = bestRoutesInGeneration;
            //allRoutesInThisGeneration.clear();

            ArrayList<ArrayList<Integer>> currentGenerationOfRoutes = new ArrayList<ArrayList<Integer>>();

            //crosses over every single possible route between all routes in array
            for (int route1 = 0; route1 < bestNofGeneration; route1++) {
                for (int route2 = 0; route2 < bestNofGeneration; route2++) {
                    ArrayList<Integer> route = ga.crossOver4(bestRoutesInGeneration.get(route1), bestRoutesInGeneration.get(route2));

                    //mutate maybe 3% of routes
                    if (rand.nextInt(100) <= 4) {
                        route = ga.mutate2(route);
                    }

                   // idea of a one off loop, a neighbour of a node is a later part of the same route.

                    /////route = ga.removeAllOneOffLoops(route);
                    route = ga.removeRedundantMoves(route);

                    currentGenerationOfRoutes.add(route);
                }

            }

            //evaluate length of routes
            /*for (int j = 0; j < currentGenerationOfRoutes.size(); j++) {
                for (int k = 0; k < currentGenerationOfRoutes.get(j).size(); k++) {
                    if (currentGenerationOfRoutes.get(j).get(k) > 40) {
                        System.out.println(currentGenerationOfRoutes.get(j));
                    }
                }
            }*/

            Collections.sort(currentGenerationOfRoutes, Comparator.comparingDouble(ga::evaluateFitness));

            bestRoutesInGeneration = ArrayListHelp.sliceArrayList(0, bestNofGeneration, currentGenerationOfRoutes);

            /*System.out.println("LENGTH OF GENERATION " + i +" ROUTES");
            for (int j=0; j<bestRoutesInGeneration.size(); j++){
                System.out.println(ga.evaluateFitness(bestRoutesInGeneration.get(j)));
            }

            //ga.display(bestRoutesInGeneration.get(0));
            System.out.println("BEST ROUTE OF GENERATION: "+ga.evaluateFitness(bestRoutesInGeneration.get(0)));
            for (int k=0; k < bestRoutesInGeneration.get(0).size(); k++){
                System.out.print(bestRoutesInGeneration.get(0).get(k)+", ");
            }
            System.out.println();

        }


            //
            //List<Node> newRoute = ga.removeRedundantMoves(unconnectedMapOfCoords.get(i));
            //ArrayList<Node> newALRoute = new ArrayList<>(newRoute);
            //List<Node> nextRoute = ga.removeRedundantMoves(unconnectedMapOfCoords.get(i + 1));
            //ArrayList<Node> nextALRoute = new ArrayList<>(newRoute);

            /*for (Node node : newRoute) {
                System.out.print(node + ", ");
            }
            System.out.println();
            for (Node node : nextRoute) {
                System.out.print(node + ", ");
            }
            System.out.println();*/

            //ArrayList<Node> crossedOver = ga.crossOver(newALRoute, nextALRoute);
            //finalAllRoutes.add(crossedOver);

            /*for (Node node : crossedOver) {
                System.out.print(node + ", ");
            }

            System.out.println();
            System.out.println("------------");
            System.out.println();*/
            //}

            //sorts the routes by total distance, takes the best few

            //////Frame newFrame = new Frame();
            //////newFrame.drawFrame(finalAllRoutes.get(0));

        }

        //ga.display(bestRoutesInGeneration.get(0));
        bestRoute = bestRoutesInGeneration.get(0);
        //System.out.println("ROUTE FOUND:" +bestRoute);
    }


    public ArrayList<Integer> getBestRoute(){
        return bestRoute;
    }
}
