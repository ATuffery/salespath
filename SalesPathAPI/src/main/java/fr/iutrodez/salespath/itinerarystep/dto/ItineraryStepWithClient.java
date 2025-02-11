package fr.iutrodez.salespath.itinerarystep.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Détail d'une étape d'itinéraire avec les informations sur le client.")
public class ItineraryStepWithClient {

    @Schema(description = "L'ID de l'itinéraire")
    private String idItinerary;

    @Schema(description = "L'ID du client")
    private String idClient;

    @Schema(description = "Le numéro de l'étape")
    private int step;

    @Schema(description = "Le nom complet du client")
    private String clientName;

    @Schema(description = "Latitude de la position du client")
    private double clientLatitude;

    @Schema(description = "Longitude de la position du client")
    private double clientLongitude;

    @Schema(description = "Adresse du client")
    private String clientAddress;

    @Schema(description = "Si le client est un client")
    private boolean isClient;

    @Schema(description = "Nom de l'entreprise")
    private String companyName;

    public String getIdItinerary() {
        return idItinerary;
    }

    public void setIdItinerary(String idItinerary) {
        this.idItinerary = idItinerary;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public double getClientLatitude() {
        return clientLatitude;
    }

    public void setClientLatitude(double clientLatitude) {
        this.clientLatitude = clientLatitude;
    }

    public double getClientLongitude() {
        return clientLongitude;
    }

    public void setClientLongitude(double clientLongitude) {
        this.clientLongitude = clientLongitude;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
