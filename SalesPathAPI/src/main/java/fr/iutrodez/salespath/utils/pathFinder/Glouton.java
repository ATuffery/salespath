package fr.iutrodez.salespath.utils.pathFinder;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Glouton {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

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
                distances[i][j] = CalculDistance.getDistance(clients[i][0], clients[i][1], clients[j][0], clients[j][1]);
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
}
