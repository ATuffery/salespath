package fr.iutrodez.salespath.account.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Représentation d'un commercial en Base de données
 */
@Schema(description = "Description d'un compte/un commercial")
@Entity
public class SalesPerson {
    @Schema(description = "Prénom du commercial")
    private String firstName;

    @Schema(description = "Nom du commercial")
    private String lastName;

    @Schema(description = "Mot de passe du commercial")
    private String password;

    @Schema(description = "Email du commercial")
    private String email;

    @Schema(description = "Adresse du commercial")
    private String address;

    @Schema(description = "Clé API du commercial")
    private String apiKey;

    @Schema(description = "Latitude du domicile du commercial")
    private double latitude;

    @Schema(description = "Longitude du domicile du commercial")
    private double longitude;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SalesPerson() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}