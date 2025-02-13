package fr.iutrodez.salespathapp.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represente un itinéraire
 */
public class Itinerary {
    private int idItinerary;
    private String nameItinerary;
    private String codeUser;
    private String dateCreation;
    private int nbSteps;
    private List<Step> steps;

    public Itinerary(int idItinerary, String nameItinerary, String codeUser, String date) {
        this.idItinerary = idItinerary;
        this.nameItinerary = nameItinerary;
        this.codeUser = codeUser;
        this.dateCreation = date;
        this.nbSteps = 0;
        this.steps = new ArrayList<>();
    }

    public void addStep(Step step) {
        this.steps.add(step);
    }

    public void setNbSteps(int nbSteps) {
        this.nbSteps = nbSteps;
    }

    public int getNbSteps() {
        return nbSteps;
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

    public String getCodeUser() {
        return codeUser;
    }

    public String getDateCreation() {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE);
        LocalDateTime dateTime = LocalDateTime.parse(dateCreation, inputFormat);
        return dateTime.format(outputFormat);
    }
}

