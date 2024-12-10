package fr.iutrodez.salespath.service;

import fr.iutrodez.salespath.repository.IClientRepository;
import fr.iutrodez.salespath.model.Client;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
    public void CreateClient(Client client) {
        try {
            clientRepository.save(client);
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
    public List<Client> GetClientsById(Long idPerson) {
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
     * @return L'objet Client correspondant.
     * @throws RuntimeException En cas d'erreur lors de la récupération.
     */
    public Client GetClientById(ObjectId id) {
        try {
            return clientRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du client par ID : " + e.getMessage());
        }
    }

    /**
     * Supprime un client à partir de son ID.
     * 
     * @param id L'ID du client à supprimer.
     * @throws RuntimeException En cas d'erreur lors de la suppression.
     */
    public void DeleteClientById(ObjectId id) {
        try {
            clientRepository.deleteClientById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du client par ID : " + e.getMessage());
        }
    }

    /**
     * Met à jour un client existant avec de nouvelles informations.
     * 
     * @param updatedClient L'objet Client contenant les nouvelles données.
     * @param id            L'ID du client à mettre à jour.
     * @throws IllegalArgumentException Si le client avec l'ID spécifié n'existe
     *                                  pas.
     */
    public void UpdateClient(Client updatedClient, ObjectId id) {
        Optional<Client> cli;
        cli = Optional.ofNullable(clientRepository.findById(id));

        if (cli.isPresent()) {
            Client existingClient = cli.get();

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
        } else {
            // Gestion du cas où le client n'existe pas
            throw new IllegalArgumentException("Client introuvable avec l'ID : " + id);
        }
    }

}
