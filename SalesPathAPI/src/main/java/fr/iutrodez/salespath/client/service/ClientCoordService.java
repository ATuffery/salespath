package fr.iutrodez.salespath.client.service;

import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.client.repository.IClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service pour gérer les opérations relatives aux coordonnées des clients.
 */
@Service
public class ClientCoordService {

    @Autowired
    private IClientRepository clientRepository;

    /**
     * Récupère les coordonnées d'un client à partir de son ID.
     * @param id L'ID du client.
     * @return Un tableau de coordonnées (latitude, longitude).
     */
    public Double[] getCoordById(String id) {
        try {
            Optional<Client> clientOptional = clientRepository.findById(id);

            if (clientOptional.isPresent()) {
                Client client = clientOptional.get();
                return client.getCoordonates();
            } else {
                throw new RuntimeException("Client non trouvé avec l'ID : " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des coordonnées du client : " + e.getMessage());
        }
    }
}
