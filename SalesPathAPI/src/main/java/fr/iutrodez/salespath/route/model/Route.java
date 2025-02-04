package fr.iutrodez.salespath.route.model;

import fr.iutrodez.salespath.route.dto.Coordinates;
import fr.iutrodez.salespath.route.dto.RouteStep;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

/**
 * Représentation d'un parcours dans Mongo DB
 */
@Document(collection = "Route")
public class Route {
    @Id
    private String id;
    private Long idSalesPerson;
    private Long itineraryId;
    private String itineraryName;
    private Date startDate;
    private Date endDate;
    private RouteStep[] steps;
    private Coordinates[] localisation;
    private int status;

    /**
     * Constructeur par défaut
     *
     * @param id Id du parcours
     * @param idSalesPerson Id du commercial
     * @param itineraryId Id de l'itinéraire
     * @param itineraryName Nom de l'itinéraire
     * @param startDate Date de début
     * @param endDate Date de fin
     * @param steps Etapes du parcours
     * @param localisation Localisation
     * @param status Statut
     */
    public Route(String id, Long idSalesPerson, Long itineraryId, String itineraryName, Date startDate,
                 Date endDate, RouteStep[] steps, Coordinates[] localisation, int status) {
        this.id = UUID.randomUUID().toString();

        this.idSalesPerson = idSalesPerson;
        this.itineraryId = itineraryId;
        this.itineraryName = itineraryName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.steps = steps;
        this.localisation = localisation;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdSalesPerson() {
        return idSalesPerson;
    }

    public void setIdSalesPerson(Long idSalesPerson) {
        this.idSalesPerson = idSalesPerson;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public RouteStep[] getSteps() {
        return steps;
    }

    public void setSteps(RouteStep[] steps) {
        this.steps = steps;
    }

    public Coordinates[] getLocalisation() {
        return localisation;
    }

    public void setLocalisation(Coordinates[] localisation) {
        this.localisation = localisation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
