package fr.iutrodez.salespathapp;

public class Config {

    /**
     * URL de l'API
     */
    public static final String API_URL = "http://ec2-35-180-71-227.eu-west-3.compute.amazonaws.com:8080/";

    /**
     * Latitude utilisé par défaut sur les cartes
     */
    public static final double MAP_DEFAULT_LATITUDE = 46.603354;

    /**
     * Longitude utilisé par défaut sur les cartes
     */
    public static final double MAP_DEFAULT_LONGITUDE = 1.888334;

    /**
     * Zoom appliqué par défaut de les cartes
     */
    public static final double MAP_DEFAULT_ZOOM = 7.0;

    /**
     * Nombre max d'étapes dans un itinéraire
     */
    public static final int MAX_ITINERARY_STEP = 8;

    /**
     * Intervale de temps entre chaque enregistrement de la position du
     * commercial lors de la réalisation d'une tournée
     */
    public static final int LOCATION_UPDATE_INTERVAL = 5000;

}
