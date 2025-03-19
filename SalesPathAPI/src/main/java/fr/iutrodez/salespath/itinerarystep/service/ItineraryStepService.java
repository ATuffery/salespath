package fr.iutrodez.salespath.itinerarystep.service;

import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.client.repository.IClientRepository;
import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import fr.iutrodez.salespath.itinerarystep.repository.IItineraryStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service pour les étapes d'un itinéraire
 */
@Service
public class ItineraryStepService {

    @Autowired
    private IItineraryStepRepository itineraryStepRepository;

    @Autowired
    private IClientRepository clientRepository;

    /**
     * Ajoute une étape à un itinéraire
     * @param itineraryStep L'étape à ajouter
     * @throws IllegalArgumentException Si l'étape existe déjà
     * @throws RuntimeException Si une erreur survient lors de la sauvegarde
     */
    public void addStep(ItineraryStep itineraryStep) {
        // On vérifie qu'une clé n'existe pas déjà
        Optional<ItineraryStep> stepOpt = itineraryStepRepository.findByIds(itineraryStep.getIdItinerary(),
                                                                            itineraryStep.getIdClient());
        if (stepOpt.isPresent()) {
            throw new IllegalArgumentException("The step already exists");
        }

        try {
            itineraryStepRepository.save(itineraryStep);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the step : " + e.getMessage());
        }
    }

    /**
     * Récupère les étapes d'un itinéraire
     * @param idItinerary L'identifiant de l'itinéraire
     * @return Les étapes de l'itinéraire
     * @throws NoSuchElementException Si l'itinéraire n'existe pas
     */
    public ItineraryStep[] getSteps(String idItinerary) {
        return itineraryStepRepository.findByIdItinerary(idItinerary)
                                      .orElseThrow(() -> new NoSuchElementException("Itinerary not found with the ID : "
                                                                                    + idItinerary));
    }

    /**
     * Vérifie si un client est utilisé dans une étape d'un itinéraire
     * @param idClient L'identifiant du client
     * @return Vrai si le client est utilisé, Faux sinon
     * @throws NoSuchElementException Si le client n'existe pas
     */
    public boolean existsByIdClient(String idClient) {
        clientRepository.findById(idClient)
                        .orElseThrow(() -> new NoSuchElementException("Client introuvable avec l'ID : " + idClient));

        return itineraryStepRepository.existsByIdClient(idClient);
    }

    /**
     * Récupère les id des itinéraires qui possèdent le client en étape
     * @param idClient L'identifiant du client
     * @return Les id des itinéraires
     * @throws NoSuchElementException Si le client n'existe pas
     */
    public List<String> findByIdClient(String idClient) {
        clientRepository.findById(idClient)
                        .orElseThrow(() -> new NoSuchElementException("Client introuvable avec l'ID : " + idClient));

        return itineraryStepRepository.findByIdClient(idClient);
    }
}
