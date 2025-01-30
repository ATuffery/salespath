package fr.iutrodez.salespathapp.itinerary;

import java.util.ArrayList;
import java.util.List;

public class Itinerary {
    private int idItinerary;
    private String nameItinerary;
    private String codeUser;
    private List<Step> steps;

    public Itinerary(int idItinerary, String nameItinerary, String codeUser) {
        this.idItinerary = idItinerary;
        this.nameItinerary = nameItinerary;
        this.codeUser = codeUser;
        this.steps = new ArrayList<>();
    }

    public void addStep(Step step) {
        this.steps.add(step);
    }

    public List<Step> getSteps() {
        return steps;
    }

    public String getNameItinerary() {
        return nameItinerary;
    }

    public int getIdItinerary() {
        return idItinerary;
    }
}

