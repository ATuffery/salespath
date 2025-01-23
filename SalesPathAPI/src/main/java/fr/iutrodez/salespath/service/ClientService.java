package fr.iutrodez.salespath.service;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import fr.iutrodez.salespath.repository.IClientRepository;
import fr.iutrodez.salespath.model.Client;
import fr.iutrodez.salespath.utils.exception.CoordinatesException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public void CreateClient(Client client) {
        try {
            double[] coord = GetCoordByAddress(client.getAddress());

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
    public Client GetClientById(String id) {
        try {
            Optional<Client> clientOptional = clientRepository.findById(id);

            if (clientOptional.isPresent()) {
                return clientOptional.get();
            } else {
                throw new RuntimeException("Client non trouvé avec l'ID : " + id);
            }
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
    public void DeleteClientById(String id) {
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
     * @throws RuntimeException         En cas d'erreur lors de la récupération des coordonnées
     */
    public void UpdateClient(Client updatedClient, String id) {
        // Recherche du client à partir de son ID, et gestion du cas où il n'est pas trouvé
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable avec l'ID : " + id));

        if (updatedClient.getCoordonates() != existingClient.getCoordonates()) {
            try {
                double[] coord = GetCoordByAddress(updatedClient.getAddress());

                if (coord[0] != 0) {
                    updatedClient.setCoordonates(new Double[]{coord[0], coord[1]});
                }
            } catch (CoordinatesException e) {
                throw new RuntimeException("Erreur lors de la récupération des coordonnées : " + e.getMessage());
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

    /**
     * Récupère les coordonnées d'un client à partir de son ID.
     * @param id L'ID du client.
     * @return Un tableau de coordonnées (latitude, longitude).
     */
    public Double[] GetCoordById(String id) {
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

    /**
     * Récupère les coordonnées d'un client à partir de son adresse.
     * @param address L'adresse du client.
     * @return Un tableau de coordonnées (latitude, longitude).
     * @throws CoordinatesException En cas d'erreur lors de la récupération des coordonnées ou si aucune
     *                              coordonnées n'est trouvée
     */
    private static double[] GetCoordByAddress(String address) throws CoordinatesException {
        double[] result = new double[0];
        Properties properties = new Properties();
        String apiKeyOpenCage;

        try {
            properties.load(ClientService.class.getClassLoader().getResourceAsStream("application.properties"));
            apiKeyOpenCage = properties.getProperty("api.key.open.cage");
        } catch (Exception e) {
            throw new CoordinatesException("Erreur lors de la récupération de la clé API OpenCage : " + e.getMessage());
        }

        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(apiKeyOpenCage);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);

        request.setRestrictToCountryCode("fr");

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);

        int statusCode = response.getStatus().getCode();

        switch (statusCode) {
            case 200:
                JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
                if (firstResultLatLng != null) {
                    result = new double[]{firstResultLatLng.getLat(), firstResultLatLng.getLng()};
                } else {
                    throw new CoordinatesException("Aucun résultat trouvé pour l'adresse : " + address);
                }
                break;
            case 400:
                throw new CoordinatesException("Requête invalide (paramètre manquant ou incorrect).");
            case 401:
                throw new CoordinatesException("Clé API invalide ou manquante.");
            case 402:
                throw new CoordinatesException("Quota dépassé (paiement requis).");
            case 403:
                throw new CoordinatesException("Accès interdit. Clé API bloquée.");
        }
        return result;
    }
}