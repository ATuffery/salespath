package fr.iutrodez.salespath.client.service;

import fr.iutrodez.salespath.client.repository.IClientRepository;
import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.itinerary.service.ItineraryService;
import fr.iutrodez.salespath.itinerarystep.service.ItineraryStepService;
import fr.iutrodez.salespath.utils.exception.CoordinatesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.iutrodez.salespath.utils.Utils;

import java.util.*;

/**
 * Service pour gérer les opérations relatives aux clients.
 */
@Service
public class ClientService {

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private ItineraryStepService itineraryStepService;

    @Autowired
    private ItineraryService itineraryService;

    /**
     * Crée un nouveau client et l'enregistre dans la base de données.
     * 
     * @param client L'objet Client à créer.
     * @throws RuntimeException En cas d'erreur lors de la création.
     */
    public void createClient(Client client) {
        try {
            double[] coord = Utils.GetCoordByAddress(client.getAddress());

            if (coord[0] != 0) {
                client.setCoordonates(new Double[]{coord[0], coord[1]});
            }

            clientRepository.save(client);
        } catch (CoordinatesException e) {
            throw new RuntimeException("Erreur lors de la récupération des coordonnées : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création d'un nouveau client : " + e.getMessage());
        }
    }

    /**
     * Récupère la liste des clients associés à une personne spécifique.
     * 
     * @param idPerson L'ID de la personne.
     * @return Une liste de clients.
     * @throws RuntimeException En cas d'erreur lors de la récupération.
     */
    public List<Client> getClientsById(Long idPerson) {
        try {
            return clientRepository.findByIdPerson(idPerson);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des clients par ID : " + e.getMessage());
        }
    }

    /**
     * Récupère un client spécifique à partir de son ID.
     *
     * @param id L'ID du client.
     * @return L'objet Client.
     * @throws NoSuchElementException Si le client avec l'ID spécifié n'existe pas.
     */
    public Client getClientById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client non trouvé avec l'ID : " + id));
    }

    /**
     * Supprime un client à partir de son ID.
     * 
     * @param id L'ID du client à supprimer.
     * @throws NoSuchElementException Si le client avec l'ID spécifié n'existe pas.
     */
    public void deleteClientById(String id) {
        clientRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Client non trouvé avec l'ID : " + id));

        clientRepository.deleteClientById(id);
    }

    /**
     * Met à jour un client existant avec de nouvelles informations.
     * 
     * @param updatedClient L'objet Client contenant les nouvelles données.
     * @param id            L'ID du client à mettre à jour.
     * @throws NoSuchElementException Si le client avec l'ID spécifié n'existe pas.
     * @throws RuntimeException         En cas d'erreur lors de la récupération des coordonnées ou
     *                                  en cas d'erreur lors de la supprzssion des itinéraires.
     */
    public void updateClient(Client updatedClient, String id) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client introuvable avec l'ID : " + id));

        // Si l'adresse a changé on met à jour les coordonnées
        if (!updatedClient.getAddress().equals(existingClient.getAddress())) {
            try {
                double[] coord = Utils.GetCoordByAddress(updatedClient.getAddress());

                if (coord[0] != 0) {
                    updatedClient.setCoordonates(new Double[]{coord[0], coord[1]});
                }
            } catch (CoordinatesException e) {
                throw new RuntimeException("Erreur lors de la récupération des coordonnées : " + e.getMessage());
            }

            // Si l'adresse a changé, on supprime également les itinéraires associés
            List<String> itineraries = itineraryStepService.findByIdClient(id);
            for (String idItinerary : itineraries) {
                if (!itineraryService.deleteItinerary(Long.valueOf(idItinerary))) {
                    throw new RuntimeException("Erreur lors de la suppression de l'itinéraire : " + idItinerary);
                }
                // TODO régler la dépendance circulaire
            }
        }

        // Mise à jour des champs
        existingClient.setEnterpriseName(updatedClient.getEnterpriseName());
        existingClient.setAddress(updatedClient.getAddress());
        existingClient.setDescription(updatedClient.getDescription());
        existingClient.setFirstName(updatedClient.getFirstName());
        existingClient.setLastName(updatedClient.getLastName());
        existingClient.setPhoneNumber(updatedClient.getPhoneNumber());
        existingClient.setClient(updatedClient.getClient());
        existingClient.setCoordonates(updatedClient.getCoordonates());
        existingClient.setIdPerson(updatedClient.getIdPerson());

        // Sauvegarde des modifications
        clientRepository.save(existingClient);
    }
}