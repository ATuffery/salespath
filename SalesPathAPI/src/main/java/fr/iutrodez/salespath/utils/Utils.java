package fr.iutrodez.salespath.utils;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import fr.iutrodez.salespath.service.ClientService;
import fr.iutrodez.salespath.utils.exception.CoordinatesException;

import java.util.Properties;

public class Utils {

    /**
     * Récupère les coordonnées d'un client à partir de son adresse.
     * @param address L'adresse du client.
     * @return Un tableau de coordonnées (latitude, longitude).
     * @throws CoordinatesException En cas d'erreur lors de la récupération des coordonnées ou si aucune
     *                              coordonnées n'est trouvée
     */
    public static double[] GetCoordByAddress(String address) throws CoordinatesException {
        double[] result = new double[0];
        Properties properties = new Properties();
        String apiKeyOpenCage;

        try {
            properties.load(ClientService.class.getClassLoader().getResourceAsStream("application.properties"));
            apiKeyOpenCage = properties.getProperty("api.key.open.cage");
        } catch (Exception e) {
            throw new CoordinatesException("Erreur lors de la récupération de la clé API OpenCage : " + e.getMessage());
        }

        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(apiKeyOpenCage);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);

        request.setRestrictToCountryCode("fr");

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);

        int statusCode = response.getStatus().getCode();

        switch (statusCode) {
            case 200:
                JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
                if (firstResultLatLng != null) {
                    result = new double[]{firstResultLatLng.getLat(), firstResultLatLng.getLng()};
                } else {
                    throw new CoordinatesException("Aucun résultat trouvé pour l'adresse : " + address);
                }
                break;
            case 400:
                throw new CoordinatesException("Requête invalide (paramètre manquant ou incorrect).");
            case 401:
                throw new CoordinatesException("Clé API invalide ou manquante.");
            case 402:
                throw new CoordinatesException("Quota dépassé (paiement requis).");
            case 403:
                throw new CoordinatesException("Accès interdit. Clé API bloquée.");
        }
        return result;
    }
}
