package fr.iutrodez.salespath.itinerarystep.model;

import java.io.Serializable;
import java.util.Objects;

public class ItineraryStepId implements Serializable {

    private String idItinerary;
    private String idClient;

    public ItineraryStepId() {
    }

    public ItineraryStepId(String idItinerary, String idClient) {
        this.idItinerary = idItinerary;
        this.idClient = idClient;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItineraryStepId that = (ItineraryStepId) o;
        return Objects.equals(idItinerary, that.idItinerary) &&
                Objects.equals(idClient, that.idClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idItinerary, idClient);
    }
}
