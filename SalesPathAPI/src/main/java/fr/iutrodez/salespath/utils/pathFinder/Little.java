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
    private List<Double[]> listeDistances;
    private int taille;

    public List<String> algoLittle(String[] idClients, Long idUser) {
        Double[] startingPoint = accountService.getCoordPerson(idUser);
        List<Double[]> clientCoords = new ArrayList<>();

        for (String idClient : idClients) {
            clientCoords.add(clientService.getCoordById(idClient));
        }

        taille = clientCoords.size();
        listeDistances = new ArrayList<>();

        for (int i = 0; i < taille; i++) {
            Double[] row = new Double[taille];
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
        List<int[]> tournée =  branchAndBound();
        return getClientOrder(tournée, idClients);
    }

    public List<int[]> branchAndBound() {
        PriorityQueue<Noeud> file = new PriorityQueue<>(Comparator.comparingInt(n -> n.cout));
        Noeud racine = new Noeud(listeDistances, cout, new ArrayList<>(), new ArrayList<>());
        file.add(racine);
        int meilleurCout = Integer.MAX_VALUE;
        List<int[]> meilleureTournée = null;

        while (!file.isEmpty()) {
            Noeud courant = file.poll();
            if (courant.trajetsInclus.size() == taille - 1) {
                if (courant.cout < meilleurCout) {
                    meilleurCout = courant.cout;
                    meilleureTournée = courant.trajetsInclus;
                }
                continue;
            }
            int[] trajet = chercherMaxRegret(courant.matrice);
            Noeud inclusion = genererNoeud(courant, trajet, true);
            Noeud exclusion = genererNoeud(courant, trajet, false);
            if (inclusion.cout < meilleurCout) file.add(inclusion);
            if (exclusion.cout < meilleurCout) file.add(exclusion);
        }
        System.out.println("Meilleure tournée : " + meilleureTournée + " avec un coût de " + meilleurCout);
        return meilleureTournée;
    }

    private List<String> getClientOrder(List<int[]> meilleureTournée, String[] idClients) {
        List<String> orderedClients = new ArrayList<>();
        for (int[] trajet : meilleureTournée) {
            orderedClients.add(idClients[trajet[0]]);
        }
        return orderedClients;
    }

    private Noeud genererNoeud(Noeud parent, int[] trajet, boolean inclure) {
        List<Double[]> nouvelleMatrice = new ArrayList<>(parent.matrice);
        int nouveauCout = parent.cout;
        List<int[]> nouveauxInclus = new ArrayList<>(parent.trajetsInclus);
        List<int[]> nouveauxExclus = new ArrayList<>(parent.trajetsExclus);

        if (inclure) {
            nouveauxInclus.add(trajet);
            reduireMatrice(nouvelleMatrice, trajet[0], trajet[1]);
            nouveauCout += parent.matrice.get(trajet[0])[trajet[1]];
        } else {
            nouveauxExclus.add(trajet);
            nouvelleMatrice.get(trajet[0])[trajet[1]] = Double.POSITIVE_INFINITY;
        }
        return new Noeud(nouvelleMatrice, nouveauCout, nouveauxInclus, nouveauxExclus);
    }

    private boolean verifSousTournee(List<int[]> trajets, int nouvelleVille) {
        List<Integer> parcours = new ArrayList<>();
        for (int[] trajet : trajets) {
            parcours.add(trajet[0]);
            parcours.add(trajet[1]);
        }
        return parcours.contains(nouvelleVille) && parcours.size() < taille;
    }
    private int[] chercherMaxRegret(List<Double[]> matrice) {
        double maxRegret = 0;
        int[] indice = new int[2];
        for (int i = 0; i < matrice.size(); i++) {
            for (int j = 0; j < matrice.get(i).length; j++) {
                if (matrice.get(i)[j] == 0) {
                    double regret = minCol(matrice, j, i) + minLigne(matrice, i, j);
                    if (regret > maxRegret) {
                        maxRegret = regret;
                        indice[0] = i;
                        indice[1] = j;
                    }
                }
            }
        }
        return indice;
    }

    private double minCol(List<Double[]> matrice, int col, int line) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < matrice.size(); i++) {
            if (i != line) {
                min = Math.min(min, matrice.get(i)[col]);
            }
        }
        return min;
    }

    private double minLigne(List<Double[]> matrice, int line, int col) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < matrice.get(line).length; i++) {
            if (i != col) {
                min = Math.min(min, matrice.get(line)[i]);
            }
        }
        return min;
    }

    private void reduireMatrice(List<Double[]> matrice, int ligne, int colonne) {
        for (int i = 0; i < matrice.size(); i++) {
            matrice.get(i)[colonne] = Double.POSITIVE_INFINITY;
        }
        for (int j = 0; j < matrice.get(ligne).length; j++) {
            matrice.get(ligne)[j] = Double.POSITIVE_INFINITY;
        }
    }
}

class Noeud {
    List<Double[]> matrice;
    int cout;
    List<int[]> trajetsInclus;
    List<int[]> trajetsExclus;

    public Noeud(List<Double[]> matrice, int cout, List<int[]> trajetsInclus, List<int[]> trajetsExclus) {
        this.matrice = matrice;
        this.cout = cout;
        this.trajetsInclus = new ArrayList<>(trajetsInclus);
        this.trajetsExclus = new ArrayList<>(trajetsExclus);
    }
}