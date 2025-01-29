package fr.iutrodez.salespath.itinerary.dto;

import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import fr.iutrodez.salespath.itinerary.model.Itinerary;

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
