package fr.iutrodez.salespath.itinerary.dto;

import fr.iutrodez.salespath.itinerarystep.dto.ItineraryStepWithClient;
import fr.iutrodez.salespath.itinerary.model.Itinerary;

public class ItineraryInfos {

    private Itinerary itinerary;
    private ItineraryStepWithClient[] steps;

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public ItineraryStepWithClient[] getSteps() {
        return steps;
    }

    public void setSteps(ItineraryStepWithClient[] steps) {
        this.steps = steps;
    }
}
