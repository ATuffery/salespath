package fr.iutrodez.salespath.utils;


import fr.iutrodez.salespath.model.SalesPerson;
import fr.iutrodez.salespath.service.AccountService;
import fr.iutrodez.salespath.service.ClientService;

import java.util.ArrayList;
import java.util.List;


public class PathFinder {

    private ClientService clientService;

    private AccountService accountService;

    private SalesPerson person;

    /* Rayon de la Terre en km */
    private static final double R = 6371;

    /**
     * Permet de calculer la distance entre 2 points (en ligne droite)
     * en utilisant la formule de Haversine
     * @param lat1 la latitude du point 1
     * @param lon1 la longitude du point 1
     * @param lat2 la latitude du point 2
     * @param lon2 la longitude du point 2
     * @return la distance en km entre les 2 points
     */
    public double DistanceCalcul(double lat1, double lon1, double lat2, double lon2){

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

    public String[] ItineraryOrder(String[] idClients, Long idUser) {

        // Créer une liste de points clients
        Double[][] clients = new Double[idClients.length + 1][2];
        clients[0] = accountService.getCoordPerson(idUser);
        // Recupérer les coordonnées de chaque point client
        for (int i = 1; i < idClients.length + 1; i++) {
            clients[i] = clientService.getCoordById(idClients[i]);
        }

        // Créer matrice de distance entre chaque point client
        Double[][] distances = new Double[clients.length][clients.length];
        for (int i = 0; i < clients.length; i++) {
            for (int j = 0; j < clients.length; j++) {
                distances[i][j] = DistanceCalcul(clients[i][1],clients[i][2], clients[j][1],clients[j][2]);
            }
        }

        // Chercher la plus petite route possible
        String[] route = new String[idClients.length];
        ArrayList<Integer> uti = new ArrayList<Integer>();
        uti.add(0);
        int indice = DistanceMin(distances[0], uti);
        uti.add(indice);
        route[0] = idClients[indice];

        for (int i = 1; i < distances.length - 1; i++) {
            indice = DistanceMin(distances[indice], uti);
            route[i] = idClients[indice];
            uti.add(indice);
        }

        // Retourner la route avec l'ordre des ID clients
        return route;
    }

    public int DistanceMin(Double[] list, ArrayList<Integer> uti) {
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

}