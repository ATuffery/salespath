package fr.iutrodez.salespath.itinerary.dto;

import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.route.dto.Coordinates;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Informations sur l'itinéraire et les coordonnées du domicile du commercial.
 */
public class ItineraryWithCoordinates {

    @Schema(implementation = Itinerary.class)
    private Itinerary itinerary;

    @Schema(description = "Coordonnées du domicile du commercial")
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
