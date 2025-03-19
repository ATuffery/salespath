package fr.iutrodez.salespath.itinerarystep.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe représentant la clé composite d'un itinéraire
 */
@Schema(description = "ID d'un itinéraire (clé composite de idItinerary et idClient)")
public class ItineraryStepId implements Serializable {

    @Schema(description = "ID de l'itinéraire")
    private String idItinerary;

    @Schema(description = "ID du client")
    private String idClient;

    /**
     * Constructeur par défaut
     */
    public ItineraryStepId() {
    }

    /**
     * Constructeur avec paramètres
     * @param idItinerary Id de l'itinéraire
     * @param idClient Id du client
     */
    public ItineraryStepId(String idItinerary, String idClient) {
        this.idItinerary = idItinerary;
        this.idClient = idClient;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItineraryStepId that = (ItineraryStepId) o;
        return Objects.equals(idItinerary, that.idItinerary) &&
                Objects.equals(idClient, that.idClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idItinerary, idClient);
    }
}
