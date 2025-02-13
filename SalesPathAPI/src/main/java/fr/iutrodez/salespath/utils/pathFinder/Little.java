package fr.iutrodez.salespath.utils.pathFinder;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Little {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    /** Coût du trajet optimal. */
    private double cout;

    /** Liste des distances entre les points. */
    private List<double[]> listeDistances;

    /** Taille de la matrice des distances. */
    private int taille;

    /** Liste des identifiants des clients dans l'ordre optimal. */
    private List<String> optimalPath;

    /** Liste des identifiants des clients. */
    private String[] idClients;

    /** Liste des clients déjà visités.*/
    private Set<Integer> visited;

    /**
     * Exécute l'algorithme de Little.
     * @param idClients Liste des identifiants des clients.
     * @param idUser Identifiant de l'utilisateur (point de départ).
     * @return Liste des identifiants des clients dans l'ordre optimal.
     */
    public List<String> algoLittle(String[] idClients, Long idUser) {
        if (idClients == null || idClients.length == 0) {
            throw new IllegalArgumentException("La liste des clients ne peut pas être vide.");
        }

        this.idClients = idClients;
        visited = new HashSet<>();
        initialiserMatrice(idClients, idUser);
        trouverCheminOptimal();

        return optimalPath;
    }

    /**
     * Initialise la matrice des distances entre les points.
     * @param idClients Liste des identifiants des clients.
     * @param idUser Identifiant de l'utilisateur (point de départ).
     */
    private void initialiserMatrice(String[] idClients, Long idUser) {
        // Récupère coordonnées de départ
        Double[] startingPoint = accountService.getCoordPerson(idUser);

        List<Double[]> clientCoords = new ArrayList<>();
        clientCoords.add(startingPoint);

        // Récupère les coordonnées des clients
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
     * Trouve le chemin optimal en explorant les différentes branches.
     */
    private void trouverCheminOptimal() {
        explorerBranches(new ArrayList<>(), listeDistances, 0);
    }

    /**
     * Explore récursivement les branches pour minimiser le coût du trajet.
     * @param path Liste des points visités.
     * @param matrix Matrice des distances.
     * @param currentCost Coût actuel du trajet.
     */
    private void explorerBranches(List<Integer> path, List<double[]> matrix, double currentCost) {
        if (path.size() == taille) {
            if (optimalPath.isEmpty() || currentCost < cout) {
                cout = currentCost;
                optimalPath.clear();

                // Ajoute les clients dans l'ordre optimal
                for (int index : path) {
                    if (index != 0) optimalPath.add(idClients[index - 1]);
                }
            }
            return;
        }

        reduireMatrice(matrix);
        int[] meilleureArrete = trouverMeilleureArrete(matrix);
        if (meilleureArrete == null || visited.contains(meilleureArrete[0])) {
            return;
        }

        visited.add(meilleureArrete[0]);

        List<double[]> matriceIncluse = copierMatrice(matrix);
        List<double[]> matriceExclue = copierMatrice(matrix);

        inclureArrete(matriceIncluse, meilleureArrete);
        exclureArrete(matriceExclue, meilleureArrete);

        path.add(meilleureArrete[0]);
        explorerBranches(new ArrayList<>(path), matriceIncluse, currentCost + cout);
        path.remove(path.size() - 1);
        visited.remove(meilleureArrete[0]);
        explorerBranches(path, matriceExclue, currentCost);
    }

    /**
     * Réduit la matrice des distances.
     * @param matrix Matrice des distances.
     */
    private void reduireMatrice(List<double[]> matrix) {
        for (double[] row : matrix) {
            double min = Double.POSITIVE_INFINITY;

            // Trouver le minimum de la ligne
            for (double value : row) {
                if (value < min) {
                    min = value;
                }
            }

            // Réduire la ligne si un minimum valide a été trouvé
            if (min != Double.POSITIVE_INFINITY) {
                cout += min;
                for (int j = 0; j < row.length; j++) {
                    row[j] -= min;
                }
            }
        }
    }

    /**
     * Trouve la meilleure arête à inclure dans la matrice.
     * @param matrix Matrice des distances.
     * @return La meilleure arête à inclure.
     */
    private int[] trouverMeilleureArrete(List<double[]> matrix) {
        int meilleureLigne = -1, meilleureColonne = -1;
        double maxRegret = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                if (matrix.get(i)[j] == 0 && !visited.contains(i)) {
                    double regret = minLigne(matrix, i, j) + minColonne(matrix, j, i);
                    if (regret > maxRegret) {
                        maxRegret = regret;
                        meilleureLigne = i;
                        meilleureColonne = j;
                    }
                }
            }
        }
        return meilleureLigne == -1 ? null : new int[]{meilleureLigne, meilleureColonne};
    }

    /**
     * Trouve le minimum de la ligne en excluant une colonne.
     * @param matrix Matrice des distances.
     * @param ligne Ligne à évaluer.
     * @param exclureCol Colonne à exclure.
     * @return Le minimum de la ligne.
     */
    private double minLigne(List<double[]> matrix, int ligne, int exclureCol) {
        double min = Double.POSITIVE_INFINITY;
        double[] row = matrix.get(ligne);

        for (int col = 0; col < row.length; col++) {
            if (col != exclureCol && row[col] < min) {
                min = row[col];
            }
        }

        return (min == Double.POSITIVE_INFINITY) ? 0 : min;
    }

    /**
     * Trouve le minimum de la colonne en excluant une ligne.
     * @param matrix Matrice des distances.
     * @param col Colonne à évaluer.
     * @param exclureLigne Ligne à exclure.
     * @return Le minimum de la colonne.
     */
    private double minColonne(List<double[]> matrix, int col, int exclureLigne) {
        double min = Double.POSITIVE_INFINITY;

        for (int ligne = 0; ligne < matrix.size(); ligne++) {
            if (ligne != exclureLigne) {
                double val = matrix.get(ligne)[col];
                if (val < min) {
                    min = val;
                }
            }
        }

        return (min == Double.POSITIVE_INFINITY) ? 0 : min;
    }

    /**
     * Copie la matrice des distances.
     * @param matrix Matrice à copier.
     * @return Copie de la matrice.
     */
    private List<double[]> copierMatrice(List<double[]> matrix) {
        List<double[]> copie = new ArrayList<>();
        for (double[] row : matrix) {
            copie.add(Arrays.copyOf(row, row.length));
        }
        return copie;
    }

    /**
     * Inclut une arête dans la matrice des distances.
     * @param matrix Matrice des distances.
     * @param arrete Arête à inclure.
     */
    private void inclureArrete(List<double[]> matrix, int[] arrete) {
        matrix.get(arrete[0])[arrete[1]] = Double.POSITIVE_INFINITY;
    }

    /**
     * Exclut une arête de la matrice des distances.
     * @param matrix Matrice des distances.
     * @param arrete Arête à exclure.
     */
    private void exclureArrete(List<double[]> matrix, int[] arrete) {
        for (int i = 0; i < matrix.size(); i++) {
            matrix.get(i)[arrete[1]] = Double.POSITIVE_INFINITY;
            matrix.get(arrete[0])[i] = Double.POSITIVE_INFINITY;
        }
    }
}