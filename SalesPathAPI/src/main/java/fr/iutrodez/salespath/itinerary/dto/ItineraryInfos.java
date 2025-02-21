package fr.iutrodez.salespath.itinerary.dto;

import fr.iutrodez.salespath.itinerarystep.dto.ItineraryStepWithClient;
import fr.iutrodez.salespath.itinerary.model.Itinerary;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Informations sur l'itinéraire et ses étapes, avec les détails des clients.
 */
@Schema(description = "Informations sur l'itinéraire et ses étapes, avec les détails des clients.")
public class ItineraryInfos {

    @Schema(implementation = ItineraryWithCoordinates.class)
    private ItineraryWithCoordinates itinerary;

    @Schema(implementation = ItineraryStepWithClient.class)
    private ItineraryStepWithClient[] steps;

    public ItineraryWithCoordinates getItinerary() {
        return itinerary;
    }

    public void setItinerary(ItineraryWithCoordinates itinerary) {
        this.itinerary = itinerary;
    }

    public ItineraryStepWithClient[] getSteps() {
        return steps;
    }

    public void setSteps(ItineraryStepWithClient[] steps) {
        this.steps = steps;
    }
}
