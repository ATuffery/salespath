package fr.iutrodez.salespath.utils.pathFinder;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class BrutForceThread {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    private static void checkValidPoints(Double[] startingPoint, List<Double[]> points) throws IllegalArgumentException {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("Points cannot be null and must have at least two elements");
        }
        if (startingPoint == null) {
            throw new IllegalArgumentException("Starting point can't be null");
        }
        if (points.contains(startingPoint)) {
            throw new IllegalArgumentException("Starting point cannot be contained in the list of points");
        }
    }

    private static <T> List<List<T>> generatePermutations(List<T> list) {
        List<List<T>> permutations = new ArrayList<>();
        if (list.size() == 1) {
            permutations.add(new ArrayList<>(list));
        } else {
            for (int i = 0; i < list.size(); i++) {
                T current = list.get(i);
                List<T> remaining = new ArrayList<>(list);
                remaining.remove(i);

                List<List<T>> remainingPermutations = generatePermutations(remaining);
                for (List<T> perm : remainingPermutations) {
                    perm.add(0, current);
                    permutations.add(perm);
                }
            }
        }
        return permutations;
    }

    public String[] brutForce(String[] idClients, Long idUser) throws InterruptedException, ExecutionException {
        Double[] startingPoint = accountService.getCoordPerson(idUser);
        List<Double[]> clientCoords = new ArrayList<>();

        for (String idClient : idClients) {
            clientCoords.add(clientService.getCoordById(idClient));
        }

        checkValidPoints(startingPoint, clientCoords);

        List<List<Double[]>> permutations = generatePermutations(clientCoords);

        // Créer un cache de distances
        double[][] distanceCache = new double[clientCoords.size() + 1][clientCoords.size() + 1];
        for (int i = 0; i <= clientCoords.size(); i++) {
            for (int j = i + 1; j <= clientCoords.size(); j++) {
                double distance = calcDistance(i == 0 ? startingPoint : clientCoords.get(i - 1), j == 0 ? startingPoint : clientCoords.get(j - 1));
                distanceCache[i][j] = distanceCache[j][i] = distance;  // La distance entre i et j est la même dans les deux sens
            }
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        List<Future<PathResult>> futures = new ArrayList<>();

        // Diviser les permutations en groupes pour ne pas créer trop de threads
        int groupSize = permutations.size() / availableProcessors;
        for (int i = 0; i < availableProcessors; i++) {
            int start = i * groupSize;
            int end = (i == availableProcessors - 1) ? permutations.size() : (i + 1) * groupSize;
            List<List<Double[]>> subList = permutations.subList(start, end);

            Callable<PathResult> task = () -> {
                PathResult bestLocalResult = new PathResult(null, Double.MAX_VALUE);
                for (List<Double[]> path : subList) {
                    List<Double[]> fullPath = new ArrayList<>();
                    fullPath.add(startingPoint);
                    fullPath.addAll(path);
                    fullPath.add(startingPoint);

                    double distance = calcTotalDistance(fullPath, clientCoords, distanceCache);
                    if (distance < bestLocalResult.distance) {
                        bestLocalResult = new PathResult(fullPath, distance);
                    }
                }
                return bestLocalResult;
            };
            futures.add(executor.submit(task));
        }

        executor.shutdown();
        PathResult bestResult = new PathResult(null, Double.MAX_VALUE);

        for (Future<PathResult> future : futures) {
            PathResult result = future.get();
            if (result.distance < bestResult.distance) {
                bestResult = result;
            }
        }

        List<Double[]> bestPath = bestResult.path;
        String[] optimalRoute = new String[idClients.length];
        for (int i = 0; i < bestPath.size() - 2; i++) {
            optimalRoute[i] = idClients[clientCoords.indexOf(bestPath.get(i + 1))];
        }

        return optimalRoute;
    }

    private double calcTotalDistance(List<Double[]> path, List<Double[]> clientCoords, double[][] distanceCache) {
        double totalDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int fromIndex = (i == 0) ? 0 : clientCoords.indexOf(path.get(i)) + 1;
            int toIndex = (i + 1 == path.size() - 1) ? 0 : clientCoords.indexOf(path.get(i + 1)) + 1;
            totalDistance += distanceCache[fromIndex][toIndex];
        }
        return totalDistance;
    }

    private double calcDistance(Double[] pointA, Double[] pointB) {
        return CalculDistance.distanceCalculBirdFly(pointA[0], pointA[1], pointB[0], pointB[1]);
    }

    private static class PathResult {
        List<Double[]> path;
        double distance;

        PathResult(List<Double[]> path, double distance) {
            this.path = path;
            this.distance = distance;
        }
    }
}
