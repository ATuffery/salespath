package fr.iutrodez.salespath.route.model;

import fr.iutrodez.salespath.route.dto.Coordinates;
import fr.iutrodez.salespath.route.dto.RouteStep;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Représentation d'un parcours dans Mongo DB
 */
@Document(collection = "Route")
@Schema(description = "Représentation d'un parcours avec les informations associées.")
public class Route {
    @Id
    @Schema(description = "Identifiant unique du parcours", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Identifiant du commercial", example = "1001")
    private Long idSalesPerson;

    @Schema(description = "Identifiant de l'itinéraire associé", example = "2002")
    private Long itineraryId;

    @Schema(description = "Nom de l'itinéraire", example = "Visite clients Toulouse")
    private String itineraryName;

    @Schema(description = "Date de début du parcours", example = "2024-02-04T08:00:00Z")
    private LocalDateTime startDate;

    @Schema(description = "Date de fin du parcours", example = "2024-02-04T18:00:00Z")
    private Date endDate;

    @ArraySchema(
            schema = @Schema(description = "Liste des étapes du parcours"),
            arraySchema = @Schema(implementation = RouteStep.class)
    )
    private ArrayList<RouteStep> steps;

    @ArraySchema(
            schema = @Schema(description = "Coordonnées géographiques du parcours"),
            arraySchema = @Schema(implementation = Coordinates.class)
    )
    private ArrayList<Coordinates> localisation;

    @Schema(description = "Statut du parcours", example = "1")
    private int status;

    /**
     * Constructeur par défaut
     *
     * @param id Id du parcours
     * @param idSalesPerson Id du commercial
     * @param itineraryId Id de l'itinéraire
     * @param itineraryName Nom de l'itinéraire
     * @param endDate Date de fin
     * @param steps Etapes du parcours
     * @param localisation Localisation
     * @param status Statut
     */
    public Route(String id, Long idSalesPerson, Long itineraryId, String itineraryName,
                 Date endDate, ArrayList<RouteStep> steps, ArrayList<Coordinates> localisation, int status) {
        this.id = UUID.randomUUID().toString();

        this.idSalesPerson = idSalesPerson;
        this.itineraryId = itineraryId;
        this.itineraryName = itineraryName;
        this.endDate = endDate;
        this.steps = steps;
        this.localisation = localisation;
        this.status = status;
    }

    public Route() {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ArrayList<RouteStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<RouteStep> steps) {
        this.steps = steps;
    }

    public ArrayList<Coordinates> getLocalisation() {
        return localisation;
    }

    public void setLocalisation(ArrayList<Coordinates> localisation) {
        this.localisation = localisation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
