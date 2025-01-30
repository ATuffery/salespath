package fr.iutrodez.salespath.itinerary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Itinerary {

    @Id
    @GeneratedValue
    private Long idItinerary;

    private String nameItinerary;

    private String codeUser;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private int nbSteps = 0;

    public Itinerary() {
    }

    public Long getIdItinerary() {
        return idItinerary;
    }

    public void setIdItinerary(Long idItinerary) {
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getNbSteps() {
        return nbSteps;
    }

    public void setNbSteps(int nbSteps) {
        this.nbSteps = nbSteps;
    }
}
