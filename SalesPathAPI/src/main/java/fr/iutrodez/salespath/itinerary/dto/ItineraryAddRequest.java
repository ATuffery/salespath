package fr.iutrodez.salespath.itinerary.dto;

import fr.iutrodez.salespath.itinerary.model.Itinerary;

public class ItineraryAddRequest {
    private Itinerary itinerary;
    private String[] idClients;

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public String[] getIdClients() {
        return idClients;
    }

    public void setIdClients(String[] idClients) {
        this.idClients = idClients;
    }
}
