package fr.iutrodez.salespath.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Client")
public class Client {

    private String enterpriseName;
    private String address;
    private String description;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isClient;
    private Double[] coordonates;
    private Long idPerson;

    @Id
    @GeneratedValue
    private ObjectId id;

    public Client(String enterpriseName, String address, String description,
            String firstName, String lastName, String phoneNumber,
            String isClient, String latitude, String longitude, String idPerson) {

        Boolean client = Boolean.parseBoolean(isClient);

        this.enterpriseName = enterpriseName;
        this.address = address;
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isClient = client;
        this.coordonates[0] = Double.parseDouble(latitude);
        this.coordonates[1] = Double.parseDouble(longitude);
        this.idPerson = Long.parseLong(idPerson);
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
        return coordonates;
    }

    public void setCoordonates(Double[] coordonates) {
        this.coordonates = coordonates;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public Long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Long idPerson) {
        this.idPerson = idPerson;
    }
}
