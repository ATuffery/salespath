package fr.iutrodez.salespath.utils.pathFinder;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static double distanceCalculBirdFly(double lat1, double lon1, double lat2, double lon2) {

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

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        try {
            String urlString = "https://router.project-osrm.org/route/v1/driving/"
                    + lon1 + "," + lat1 + ";" + lon2 + "," + lat2 + "?overview=false";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON
            JSONObject json = new JSONObject(response.toString());
            JSONArray routes = json.getJSONArray("routes");
            if (routes.length() > 0) {
                return routes.getJSONObject(0).getDouble("distance") / 1000.0; // Convert to km
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Erreur
    }
}
