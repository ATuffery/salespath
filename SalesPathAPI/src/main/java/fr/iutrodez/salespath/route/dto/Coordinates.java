package fr.iutrodez.salespath.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Représentation des coordonnées géographiques
 */
@Schema(description = "Représentation des coordonnées géographiques avec latitude et longitude.")
public class Coordinates {

    @Schema(description = "Latitude du point géographique", example = "43.604652")
    private double latitude;

    @Schema(description = "Longitude du point géographique", example = "1.444209")
    private double longitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
