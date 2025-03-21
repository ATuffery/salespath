package fr.iutrodez.salespath.client.service;

import fr.iutrodez.salespath.client.repository.IClientRepository;
import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.itinerary.service.ItineraryService;
import fr.iutrodez.salespath.utils.exception.CoordinatesException;
import jakarta.transaction.Transactional;
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
                client.setCoordonates(new Double[]{coord[1], coord[0]});
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
     * Récupère client et prospects a une distance inférieur à 200m et 1km
     * @param lat latitude du user
     * @param lon longitude du user
     * @return la liste client et prospects
     */
    public List<Client> getClientProximity(double lat, double lon, Long id) {
        try {
            // Récupérer les clients et prospects séparément
            List<Client> clientProximity = clientRepository.findClientsWithin200m(lon, lat, id);
            List<Client> prospectProximity = clientRepository.findProspectsWithin1Km(lon, lat, id);

            // Fusionner les deux listes sans doublon
            Set<Client> proximitySet = new HashSet<>(clientProximity);
            proximitySet.addAll(prospectProximity);

            return new ArrayList<>(proximitySet);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des clients : " + e.getMessage());
        }

    }
}