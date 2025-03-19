package fr.iutrodez.salespath.utils.pathFinder;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientCoordService;
import fr.iutrodez.salespath.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Implémentation de l'algorithme de Little pour résoudre le problème du voyageur de commerce.
 */
@Component
public class Little {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientCoordService clientService;

    private double cout;
    private List<double[]> listeDistances;
    private int taille;
    private List<String> optimalPath;
    private String[] idClients;

    /**
     * Résout le problème du voyageur de commerce avec l'algorithme de Little.
     * @param idClients Liste des identifiants des clients.
     * @param idUser Identifiant de l'utilisateur.
     * @return Liste ordonnée des clients selon le chemin optimal.
     */
    public List<String> algoLittle(String[] idClients, Long idUser) {
        if (idClients == null || idClients.length == 0) {
            throw new IllegalArgumentException("La liste des clients ne peut pas être vide.");
        }

        this.idClients = idClients;
        initialiserMatrice(idClients, idUser);
        trouverCheminOptimal();

        return optimalPath;
    }

    /**
     * Initialise la matrice des distances à partir des coordonnées des clients et de l'utilisateur.
     */
    private void initialiserMatrice(String[] idClients, Long idUser) {
        Double[] startingPoint = accountService.getCoordPerson(idUser);
        List<Double[]> clientCoords = new ArrayList<>();
        clientCoords.add(startingPoint);

        for (String idClient : idClients) {
            clientCoords.add(clientService.getCoordById(idClient));
        }

        taille = clientCoords.size();
        listeDistances = new ArrayList<>();
        optimalPath = new ArrayList<>();

        for (int i = 0; i < taille; i++) {
            double[] row = new double[taille];
            for (int j = 0; j < taille; j++) {
                row[j] = (i == j) ? Double.POSITIVE_INFINITY :
                        CalculDistance.getDistance(clientCoords.get(i)[0], clientCoords.get(i)[1],
                                clientCoords.get(j)[0], clientCoords.get(j)[1]);
            }
            listeDistances.add(row);
        }
    }

    /**
     * Trouve le chemin optimal en explorant les branches de l'arbre de décision.
     */
    private void trouverCheminOptimal() {
        List<Integer> cheminActuel = new ArrayList<>();
        boolean[] visites = new boolean[taille];
        cout = 0;
        explorerBranches(cheminActuel, 0, visites, 0);
    }

    private void explorerBranches(List<Integer> cheminActuel, double currentCost, boolean[] visites, int niveau) {
        if (cheminActuel.size() == taille - 1) {
            if (optimalPath.isEmpty() || currentCost < cout) {
                cout = currentCost;
                optimalPath.clear();
                for (int index : cheminActuel) {
                    optimalPath.add(idClients[index - 1]);
                }
            }
            return;
        }

        for (int i = 1; i < taille; i++) {
            if (!visites[i]) {
                visites[i] = true;
                cheminActuel.add(i);
                explorerBranches(cheminActuel, currentCost + listeDistances.get(niveau)[i], visites, i);
                cheminActuel.remove(cheminActuel.size() - 1);
                visites[i] = false;
            }
        }
    }
}
