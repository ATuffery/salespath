package fr.iutrodez.salespath.utils.pathFinder;

public class CalculDistance {


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
}
