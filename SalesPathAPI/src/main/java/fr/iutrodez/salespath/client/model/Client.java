package fr.iutrodez.salespath.client.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Arrays;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Représentation d'un client dans Mongo DB
 */
@Schema(description = "Informations sur un client")
@Document(collection = "Client")
public class Client {

    @Schema(description = "Nom de l'entreprise du client", example = "Entreprise XYZ")
    private String enterpriseName;

    @Schema(description = "Adresse du client", example = "123 Rue Exemple")
    private String address;

    @Schema(description = "Description de l'entreprise", example = "Société de consulting")
    private String description;

    @Schema(description = "Prénom du contact", example = "Jean")
    private String firstName;

    @Schema(description = "Nom de famille du contact", example = "Dupont")
    private String lastName;

    @Schema(description = "Numéro de téléphone du client", example = "0123456789")
    private String phoneNumber;

    @Schema(description = "Indique si le client est actif", example = "true")
    private Boolean isClient;

    @Schema(description = "Coordonnées GPS du client", example = "[48.8566, 2.3522]")
    private Double[] coordinates;

    @Schema(description = "ID de la personne associée au client", example = "12345")
    private Long idPerson;

    @Id
    @GeneratedValue
    @Schema(description = "Identifiant unique du client", example = "1")
    private String id;

    /**
     * Constructeur par défaut
     */
    public Client(String enterpriseName, String address, String description,
            String firstName, String lastName, String phoneNumber,
            Boolean isClient, Double[] coordinates, Long idPerson) {

        this.enterpriseName = enterpriseName;
        this.address = address;
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isClient = isClient;
        this.coordinates = coordinates;
        this.idPerson = idPerson;

        this.id = UUID.randomUUID().toString();
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getClient() {
        return isClient;
    }

    public void setClient(Boolean client) {
        isClient = client;
    }

    public Double[] getCoordonates() {
        return coordinates;
    }

    public void setCoordonates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Long idPerson) {
        this.idPerson = idPerson;
    }
}
