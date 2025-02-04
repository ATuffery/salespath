package fr.iutrodez.salespath.route.dto;

import fr.iutrodez.salespath.client.model.Client;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Représentation d'une étape d'un parcours, avec les infos du client et son statut
 */
@Schema(description = "Représentation d'une étape d'un parcours, incluant les informations du client et son statut.")
public class RouteStep {

    @Schema(implementation = Client.class, description = "Informations du client")
    private Client client;

    @Schema(description = "Statut de l'étape", example = "1")
    private int status;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
