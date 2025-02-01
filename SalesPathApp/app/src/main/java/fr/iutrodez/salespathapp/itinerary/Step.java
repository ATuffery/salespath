package fr.iutrodez.salespathapp.itinerary;

/**
 * Représente une étape d'un itinéraire
 */
public class Step {
    private int idItinerary;
    private String idClient;
    private int step;
    private String clientName;
    private double clientLatitude;
    private double clientLongitude;

    public Step(int idItinerary, String idClient, int step, String clientName, double clientLatitude, double clientLongitude) {
        this.idItinerary = idItinerary;
        this.idClient = idClient;
        this.step = step;
        this.clientName = clientName;
        this.clientLatitude = clientLatitude;
        this.clientLongitude = clientLongitude;
    }

    public String getClientName() {
        return clientName;
    }

    public int getIdItinerary() {
        return idItinerary;
    }

    public int getStep() {
        return step;
    }

    public double getClientLatitude() {
        return clientLatitude;
    }

    public double getClientLongitude() {
        return clientLongitude;
    }

    public String getIdClient() { return idClient; }
}
