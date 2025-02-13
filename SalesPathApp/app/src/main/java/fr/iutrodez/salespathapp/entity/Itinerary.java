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
    private ArrayList<Contact> steps;

    public Itinerary(int idItinerary, String nameItinerary, String codeUser, String date) {
        this.idItinerary = idItinerary;
        this.nameItinerary = nameItinerary;
        this.codeUser = codeUser;
        this.dateCreation = date;
        this.steps = new ArrayList<>();
    }

    public void addStep(Contact step) {
        this.steps.add(step);
    }

    public int getNbSteps() {
        return this.steps.size();
    }

    public ArrayList<Contact> getSteps() {
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

