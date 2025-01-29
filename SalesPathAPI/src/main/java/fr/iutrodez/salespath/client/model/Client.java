package fr.iutrodez.salespath.client.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Arrays;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Représentation d'un client dans Mongo DB
 */
@Document(collection = "Client")
public class Client {

    private String enterpriseName;
    private String address;
    private String description;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isClient;
    private Double[] coordinates;
    private Long idPerson;

    @Id
    @GeneratedValue
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
