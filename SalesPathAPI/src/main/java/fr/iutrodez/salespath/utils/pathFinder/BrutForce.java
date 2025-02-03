package fr.iutrodez.salespath.utils.pathFinder;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrutForce {


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

    /**
     * Génère toutes les permutations possibles d'une liste.
     *
     * @param list Liste d'éléments à permuter
     * @return Liste contenant toutes les permutations
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
     * Algorithme brute force pour trouver le chemin optimal.
     *
     * @param idClients Liste des IDs des clients
     * @param idUser ID de l'utilisateur (point de départ)
     * @return Liste des IDs clients dans l'ordre optimal
     */
    public String[] brutForce(String[] idClients, Long idUser) {
        // Récupération des coordonnées du point de départ
        Double[] startingPoint = accountService.getCoordPerson(idUser);
        List<Double[]> clientCoords = new ArrayList<>();

        // Récupération des coordonnées des clients
        for (String idClient : idClients) {
            clientCoords.add(clientService.getCoordById(idClient));
        }

        checkValidPoints(startingPoint, clientCoords);

        // Génération des permutations des clients
        List<List<Double[]>> permutations = generatePermutations(clientCoords);

        List<Double[]> bestPath = null;
        double minDistance = Double.MAX_VALUE;

        for (List<Double[]> path : permutations) {
            List<Double[]> fullPath = new ArrayList<>();
            fullPath.add(startingPoint); // Ajout du point de départ
            fullPath.addAll(path);
            fullPath.add(startingPoint); // Retour au point de départ

            double distance = calcTotalDistance(fullPath);
            if (distance < minDistance) {
                minDistance = distance;
                bestPath = new ArrayList<>(fullPath);
            }
        }

        // Convertir la liste des coordonnées en liste d'IDs clients
        String[] optimalRoute = new String[idClients.length];
        for (int i = 0; i < bestPath.size() - 2; i++) {
            optimalRoute[i] = idClients[clientCoords.indexOf(bestPath.get(i + 1))]; // On exclut le point de départ
        }

        return optimalRoute;
    }

    /**
     * Calcule la distance totale d'un chemin donné.
     *
     * @param path Liste de points ordonnés
     * @return Distance totale
     */
    private double calcTotalDistance(List<Double[]> path) {
        double totalDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalDistance += CalculDistance.distanceCalcul(
                    path.get(i)[0], path.get(i)[1],
                    path.get(i + 1)[0], path.get(i + 1)[1]
            );
        }
        return totalDistance;
    }

}
