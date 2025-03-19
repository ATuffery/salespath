package fr.iutrodez.salespath.itinerary.dto;

import fr.iutrodez.salespath.itinerary.model.Itinerary;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Requête pour ajouter un itinéraire
 */
public class ItineraryAddRequest {

    @Schema(implementation = Itinerary.class)
    private Itinerary itinerary;

    @Schema(description = "Liste des identifiants des clients à ajouter à l'itinéraire")
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
