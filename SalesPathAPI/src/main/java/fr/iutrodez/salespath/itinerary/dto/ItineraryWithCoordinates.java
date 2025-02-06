package fr.iutrodez.salespath.itinerary.dto;

import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.route.dto.Coordinates;

/**
 * Informations sur l'itinéraire et les coordonnées du domicile du commercial.
 */
public class ItineraryWithCoordinates {

    private Itinerary itinerary;
    private Coordinates coordinates;

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
