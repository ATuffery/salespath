package fr.iutrodez.salespath.utils.pathFinder;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientCoordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Classe permettant de trouver le chemin optimal par force brute en utilisant plusieurs threads.
 */
@Component
public class BrutForceThread {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientCoordService clientCoordService;

    /**
     * Vérifie la validité des points donnés.
     *
     * @param startingPoint Le point de départ.
     * @param points La liste des points à visiter.
     * @throws IllegalArgumentException si les points sont invalides.
     */
    private static void checkValidPoints(Double[] startingPoint, List<Double[]> points) throws IllegalArgumentException {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("Points ne peut pas être null ou contenir moins de 2 éléments");
        }
        if (startingPoint == null) {
            throw new IllegalArgumentException("Le point de départ ne peut pas être null");
        }
        if (points.contains(startingPoint)) {
            throw new IllegalArgumentException("Le point de départ ne peut pas être un point à visiter");
        }
    }

    /**
     * Génère toutes les permutations possibles d'une liste.
     *
     * @param list La liste des éléments.
     * @param <T>  Le type des éléments de la liste.
     * @return Une liste contenant toutes les permutations possibles.
     */
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

    /**
     * Trouve le chemin optimal entre les clients en utilisant la méthode de force brute multi-threadée.
     *
     * @param idClients Liste des identifiants des clients.
     * @param idUser    Identifiant de l'utilisateur.
     * @return Un tableau de chaînes représentant l'ordre optimal des clients.
     * @throws InterruptedException si l'exécution des threads est interrompue.
     */
    public String[] brutForce(String[] idClients, Long idUser) throws InterruptedException {
        Double[] startingPoint = accountService.getCoordPerson(idUser);
        List<Double[]> clientCoords = new ArrayList<>();

        for (String idClient : idClients) {
            clientCoords.add(clientCoordService.getCoordById(idClient));
        }

        checkValidPoints(startingPoint, clientCoords);
        List<List<Double[]>> permutations = generatePermutations(clientCoords);

        // Création d'un cache de distances pour éviter les recalculs inutiles
        double[][] distanceCache = new double[clientCoords.size() + 1][clientCoords.size() + 1];
        for (int i = 0; i <= clientCoords.size(); i++) {
            for (int j = i + 1; j <= clientCoords.size(); j++) {
                double distance = calcDistance(i == 0 ? startingPoint : clientCoords.get(i - 1),
                        j == 0 ? startingPoint : clientCoords.get(j - 1));
                distanceCache[i][j] = distanceCache[j][i] = distance;
            }
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        ConcurrentLinkedQueue<PathResult> results = new ConcurrentLinkedQueue<>();

        int groupSize = permutations.size() / availableProcessors;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < availableProcessors; i++) {
            int start = i * groupSize;
            int end = (i == availableProcessors - 1) ? permutations.size() : (i + 1) * groupSize;
            List<List<Double[]>> subList = permutations.subList(start, end);

            Runnable task = () -> {
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
                results.add(bestLocalResult);
            };

            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Recherche du meilleur chemin parmi les résultats des threads
        PathResult bestResult = new PathResult(null, Double.MAX_VALUE);
        for (PathResult result : results) {
            if (result.distance < bestResult.distance) {
                bestResult = result;
            }
        }

        // Conversion du meilleur chemin en identifiants de clients
        List<Double[]> bestPath = bestResult.path;
        String[] optimalRoute = new String[idClients.length];
        for (int i = 0; i < bestPath.size() - 2; i++) {
            optimalRoute[i] = idClients[clientCoords.indexOf(bestPath.get(i + 1))];
        }

        return optimalRoute;
    }

    /**
     * Calcule la distance totale d'un chemin donné.
     */
    private double calcTotalDistance(List<Double[]> path, List<Double[]> clientCoords, double[][] distanceCache) {
        double totalDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int fromIndex = (i == 0) ? 0 : clientCoords.indexOf(path.get(i)) + 1;
            int toIndex = (i + 1 == path.size() - 1) ? 0 : clientCoords.indexOf(path.get(i + 1)) + 1;
            totalDistance += distanceCache[fromIndex][toIndex];
        }
        return totalDistance;
    }

    /**
     * Calcule la distance entre deux points.
     */
    private double calcDistance(Double[] pointA, Double[] pointB) {
        return CalculDistance.distanceCalculBirdFly(pointA[0], pointA[1], pointB[0], pointB[1]);
    }

    /**
     * Classe interne pour stocker un chemin et sa distance associée.
     */
    private static class PathResult {
        List<Double[]> path;
        double distance;

        PathResult(List<Double[]> path, double distance) {
            this.path = path;
            this.distance = distance;
        }
    }
}