package fr.iutrodez.salespath.itinerarystep.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

/**
 * Classe représentant une étape d'un itinéraire
 */
@Schema(description = "Représente une étape d'un itinéraire")
@Entity
@IdClass(ItineraryStepId.class)
public class ItineraryStep {

    @Id
    private String idItinerary;

    @Id
    private String idClient;

    @Schema(description = "Numéro de l'étape")
    private int step;

    /**
     * Constructeur par défaut
     */
    public ItineraryStep() {
    }

    public String getIdItinerary() {
        return idItinerary;
    }

    public void setIdItinerary(String idItinerary) {
        this.idItinerary = idItinerary;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
