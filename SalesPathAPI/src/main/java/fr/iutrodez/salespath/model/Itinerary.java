package fr.iutrodez.salespath.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Itinerary {

    @Id
    @GeneratedValue
    private String idItinerary;

    private String nameItinerary;

    private String codeUser;

    public Itinerary() {
    }

    public String getIdItinerary() {
        return idItinerary;
    }

    public void setIdItinerary(String idItinerary) {
        this.idItinerary = idItinerary;
    }

    public String getNameItinerary() {
        return nameItinerary;
    }

    public void setNameItinerary(String nameItinerary) {
        this.nameItinerary = nameItinerary;
    }

    public String getCodeUser() {
        return codeUser;
    }

    public void setCodeUser(String codeUser) {
        this.codeUser = codeUser;
    }
}
