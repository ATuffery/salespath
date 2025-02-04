package fr.iutrodez.salespath.route.dto;

import fr.iutrodez.salespath.client.model.Client;

/**
 * Représentation d'une étape d'un parcours, avec les infos du client et son statut
 */
public class RouteStep {

    private Client client;
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
