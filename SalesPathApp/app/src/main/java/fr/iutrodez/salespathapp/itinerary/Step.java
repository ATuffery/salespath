package fr.iutrodez.salespathapp.itinerary;

import fr.iutrodez.salespathapp.contact.Contact;

/**
 * Représente une étape d'un itinéraire
 */
public class Step {
    private int idItinerary;
    private Contact contact;
    private int step;
    private String clientName;
    private boolean isClient;
    private String clientAddress;
    private double clientLatitude;
    private double clientLongitude;

    public Step(int idItinerary, int step, Contact contact) {
        this.idItinerary = idItinerary;
        this.contact = contact;
        this.step = step;
    }

    public Contact getContact() {
        return contact;
    }

    public int getIdItinerary() {
        return idItinerary;
    }

    public int getStep() {
        return step;
    }

}
