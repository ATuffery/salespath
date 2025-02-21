package fr.iutrodez.salespath.itinerary.service;

import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.client.repository.IClientRepository;
import fr.iutrodez.salespath.itinerarystep.service.ItineraryStepService;
import fr.iutrodez.salespath.utils.Utils;
import fr.iutrodez.salespath.utils.exception.CoordinatesException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service pour gérer les opérations relatives aux itinéraires et aux clients
 */
@Service
public class ItineraryClientService {

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ItineraryStepService itineraryStepService;

    /**
     * Supprime un client à partir de son ID.
     *
     * @param id L'ID du client à supprimer.
     * @throws NoSuchElementException Si le client avec l'ID spécifié n'existe pas.
     */
    @Transactional
    public void deleteClientById(String id) {
        clientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client non trouvé avec l'ID : " + id));

        // On supprime également les itinéraires associés
        deleteItinerariesForClient(id);

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
            deleteItinerariesForClient(id);
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

    /**
     * Supprime les itinéraires associés à un client.
     * @param id L'ID du client.
     * @throws RuntimeException En cas d'erreur lors de la suppression.
     */
    public void deleteItinerariesForClient(String id) {
        List<String> itineraries = itineraryStepService.findByIdClient(id);
        for (String idItinerary : itineraries) {
            if (!itineraryService.deleteItinerary(Long.valueOf(idItinerary))) {
                throw new RuntimeException("Erreur lors de la suppression de l'itinéraire : " + idItinerary);
            }
        }
    }
}
