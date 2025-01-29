package fr.iutrodez.salespath.dto;

import fr.iutrodez.salespath.model.Itinerary;
import fr.iutrodez.salespath.model.ItineraryStep;

public class ItineraryInfos {

    private Itinerary itinerary;
    private ItineraryStep[] steps;

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public ItineraryStep[] getSteps() {
        return steps;
    }

    public void setSteps(ItineraryStep[] steps) {
        this.steps = steps;
    }
}
