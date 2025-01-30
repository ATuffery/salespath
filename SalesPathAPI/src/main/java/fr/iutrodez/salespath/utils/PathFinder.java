package fr.iutrodez.salespath.utils;


import fr.iutrodez.salespath.account.model.SalesPerson;
import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PathFinder {

    private ClientService clientService;

    private AccountService accountService;

    private SalesPerson person;

    @Autowired
    public PathFinder(ClientService clientService, AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
    }

    /* Rayon de la Terre en km */
    private static final double R = 6371;

    /**
     * Permet de calculer la distance entre 2 points (en ligne droite)
     * en utilisant la formule de Haversine
     *
     * @param lat1 la latitude du point 1
     * @param lon1 la longitude du point 1
     * @param lat2 la latitude du point 2
     * @param lon2 la longitude du point 2
     * @return la distance en km entre les 2 points
     */
    public static double distanceCalcul(double lat1, double lon1, double lat2, double lon2) {

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Permet de trouver l'ordre des clients à visiter
     *
     * @param idClients les ID des clients
     * @param idUser    l'ID de l'utilisateur
     * @return l'ordre des clients à visiter
     */
    public String[] itineraryOrder(String[] idClients, Long idUser) {

        // Créer une liste de points clients
        Double[][] clients = new Double[idClients.length + 1][2];
        clients[0] = accountService.getCoordPerson(idUser);
        // Recupérer les coordonnées de chaque point client
        for (int i = 0; i < idClients.length; i++) {
            clients[i + 1] = clientService.getCoordById(idClients[i]);
        }

        // Créer matrice de distance entre chaque point client
        Double[][] distances = new Double[clients.length][clients.length];
        for (int i = 0; i < clients.length; i++) {
            for (int j = 0; j < clients.length; j++) {
                distances[i][j] = distanceCalcul(clients[i][0], clients[i][1], clients[j][0], clients[j][1]);
            }
        }

        // Chercher la plus petite route possible
        String[] route = new String[idClients.length];
        ArrayList<Integer> uti = new ArrayList<Integer>();
        uti.add(0);
        int indice = distanceMin(distances[0], uti);
        uti.add(indice);
        route[0] = idClients[indice - 1];

        for (int i = 1; i < distances.length - 1; i++) {
            indice = distanceMin(distances[indice], uti);
            route[i] = idClients[indice - 1];
            uti.add(indice);
        }

        // Retourner la route avec l'ordre des ID clients
        return route;
    }

    /**
     * Permet de trouver la distance minimale dans une liste
     *
     * @param list la liste de distances
     * @param uti  la liste des indices déjà utilisés
     * @return l'indice de la distance minimale
     */
    public static int distanceMin(Double[] list, ArrayList<Integer> uti) {
        double distMin = 1000000000;
        int indice = 0;
        boolean isPresent = false;
        for (int i = 1; i < list.length; i++) {
            isPresent = uti.contains(i);
            if (distMin > list[i] && !isPresent) {
                distMin = list[i];
                indice = i;
            }
        }
        return indice;
    }

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
            totalDistance += distanceCalcul(
                    path.get(i)[0], path.get(i)[1],
                    path.get(i + 1)[0], path.get(i + 1)[1]
            );
        }
        return totalDistance;
    }
}

