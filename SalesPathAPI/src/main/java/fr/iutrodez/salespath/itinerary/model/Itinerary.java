package fr.iutrodez.salespath.itinerary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Représete un itinéraire
 */
@Entity
@Schema(description = "Informations sur un itinéraire")
public class Itinerary {

    @Id
    @GeneratedValue
    private Long idItinerary;

    @Schema(description = "Nom de l'itinéraire")
    private String nameItinerary;

    @Schema(description = "Id de l'utilisateur")
    private String codeUser;

    @Schema(description = "Date de création de l'itinéraire")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Schema(description = "Nombre d'étapes de l'itinéraire")
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
