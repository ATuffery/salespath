package fr.iutrodez.salespathapp;

public class Config {

    /**
     * URL de l'API
     */
    public static final String API_URL = "http://ec2-52-47-174-172.eu-west-3.compute.amazonaws.com:8080/";

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

}
