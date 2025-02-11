package fr.iutrodez.salespath.utils.pathFinder;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Little {

    @Autowired
    public AccountService accountService;

    @Autowired
    public ClientService clientService;

    private int cout = 0;
    private List<double[]> listeDistances;
    private int taille;

    /**
     * Algorithme de Little pour trouver le chemin optimal.
     * @param idClients Liste des ID des clients
     * @param idUser ID de l'utilisateur (point de départ)
     * @return Liste des ID des clients dans l'ordre optimal
     */
    public List<String> algoLittle(String[] idClients, Long idUser) {
        if (idClients == null || idClients.length == 0) {
            throw new IllegalArgumentException("La liste des clients ne peut pas être vide.");
        }

        // Coordonnées du point de départ
        Double[] startingPoint = accountService.getCoordPerson(idUser);
        List<Double[]> clientCoords = new ArrayList<>();

        clientCoords.add(startingPoint);

        // Coordonnées des clients
        for (String idClient : idClients) {
            clientCoords.add(clientService.getCoordById(idClient));
        }

        taille = clientCoords.size();
        listeDistances = new ArrayList<>();

        // Création de la matrice des distances
        for (int i = 0; i < taille; i++) {
            double[] row = new double[taille];
            for (int j = 0; j < taille; j++) {
                if (i == j) {
                    row[j] = Double.POSITIVE_INFINITY;
                } else {
                    row[j] = CalculDistance.distanceCalculBirdFly(
                            clientCoords.get(i)[0], clientCoords.get(i)[1],
                            clientCoords.get(j)[0], clientCoords.get(j)[1]
                    );
                }
            }
            listeDistances.add(row);
        }

        return null;
    }

    private void reduceMatrix () {
        for(int line = 0; line < listeDistances.size(); line++) {
            for (int col = 0; col < listeDistances.size(); col++) {
                
            }
        }
    }

}