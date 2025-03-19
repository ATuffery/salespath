package fr.iutrodez.salespath.itinerary.service;

import fr.iutrodez.salespath.account.repository.IAccountRepository;
import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.client.service.ClientCoordService;
import fr.iutrodez.salespath.client.service.ClientService;
import fr.iutrodez.salespath.itinerary.dto.ItineraryInfos;
import fr.iutrodez.salespath.itinerary.dto.ItineraryWithCoordinates;
import fr.iutrodez.salespath.itinerarystep.dto.ItineraryStepWithClient;
import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import fr.iutrodez.salespath.itinerarystep.repository.IItineraryStepRepository;
import fr.iutrodez.salespath.itinerarystep.service.ItineraryStepService;
import fr.iutrodez.salespath.itinerary.dto.ItineraryAddRequest;
import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.itinerary.repository.IItineraryRepository;
import fr.iutrodez.salespath.route.dto.Coordinates;
import fr.iutrodez.salespath.utils.pathFinder.BrutForceThread;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service pour les itinéraires
 */
@Service
public class ItineraryService {

    @Autowired
    private IItineraryRepository itineraryRepository;

    @Autowired
    private BrutForceThread bf;

    @Autowired
    private ItineraryStepService itineraryStepService;

    @Autowired
    private IItineraryStepRepository itineraryStepRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    /**
     * Crée un itinéraire (vérifie que le nom soit valide, génére le chemin optimisé)
     * @param iti Requête d'ajout d'itinéraire
     * @throws IllegalArgumentException Si le nom de l'itinéraire existe déjà
     * @throws RuntimeException Si une erreur survient lors de la sauvegarde
     */
    public void createItinerary(ItineraryAddRequest iti) {
        itineraryRepository.existsByNameAndId(iti.getItinerary().getNameItinerary(), iti.getItinerary().getCodeUser())
                .ifPresent(name -> {
                    throw new IllegalArgumentException("Itinerary name already exists");
                });

        try {
            String[] order = bf.brutForce(iti.getIdClients(),
                                               Long.parseLong(iti.getItinerary().getCodeUser()));

            Itinerary saved = itineraryRepository.save(iti.getItinerary());

            for (int i = 0; i < order.length; i++) {
                ItineraryStep itiStep = new ItineraryStep();
                itiStep.setIdItinerary(String.valueOf(saved.getIdItinerary()));
                itiStep.setIdClient(order[i]);
                itiStep.setStep(i+1);

                itineraryStepService.addStep(itiStep);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the itinerary : " + e.getMessage());
        }
    }

    /**
     * Récupère les itinéraires d'un utilisateur
     * @param idUser ID de l'utilisateur
     * @return Tableau d'itinéraires
     * @throws NoSuchElementException Si l'utilisateur n'existe pas
     * @throws NoSuchElementException Si aucun itinéraire n'est trouvé
     */
    public Itinerary[] getItineraryUser(Long idUser) {
        // On vérifie que l'utilisateur existe
        accountRepository.findById(idUser)
                         .orElseThrow(() -> new NoSuchElementException("Account not found for ID : " + idUser));

        return itineraryRepository.findByIdUser(idUser)
                                  .orElseThrow(() -> new NoSuchElementException("No itinerary found for user : " + idUser));

    }

    /**
     * Récupère un itinéraire par son ID
     * @param id ID de l'itinéraire
     * @return l'itinéraire
     * @throws NoSuchElementException Si l'itinéraire n'existe pas
     */
    public Itinerary getItinerary(Long id) {
        return itineraryRepository.findById(id)
                                  .orElseThrow(() -> new NoSuchElementException("Itinerary not found for ID : " + id));
    }

    /**
     * Supprime un itinéraire (l'itinéraire et les étapes qui y sont rattachées)
     * @param id l'id de l'itinéraire
     * @return vrai si la suppression a réussi, faux si l'itinéraire existe pas
     * @throws RuntimeException Si une erreur survient lors de la suppression
     */
    @Transactional
    public boolean deleteItinerary(Long id) {
        Optional<Itinerary> itineraryOpt = itineraryRepository.findById(id);

        if (itineraryOpt.isEmpty()) {
            return false;
        }

        try {
            itineraryStepRepository.deleteByIdItinerary(String.valueOf(id));
            itineraryRepository.deleteById(id);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting itinerary: " + e.getMessage());
        }
    }

    /**
     * Récupère les informations d'un itinéraire avec les étapes
     * @param id ID de l'itinéraire
     * @return Informations sur l'itinéraire
     */
    public ItineraryInfos getAllInfos(Long id) {
        // On récupère l'itinéraire
        Itinerary itinerary = this.getItinerary(id);

        // On récupère les étapes de l'itinéraire
        ItineraryStep[] steps = itineraryStepService.getSteps(String.valueOf(itinerary.getIdItinerary()));
        List<ItineraryStep> stepsList = Arrays.asList(steps);

        // Ajouter les informations des clients
        ItineraryStepWithClient[] stepsWithClient = stepsList.stream().map(step -> {
            Client client = clientService.getClientById(step.getIdClient());

            ItineraryStepWithClient enrichedStep = new ItineraryStepWithClient();
            enrichedStep.setIdItinerary(step.getIdItinerary());
            enrichedStep.setIdClient(step.getIdClient());
            enrichedStep.setStep(step.getStep());

            enrichedStep.setClientName(client.getLastName() + ' ' + client.getFirstName());
            enrichedStep.setClientLatitude(client.getCoordonates()[0]);
            enrichedStep.setClientLongitude(client.getCoordonates()[1]);
            enrichedStep.setClientAddress(client.getAddress());
            enrichedStep.setClient(client.getClient());
            enrichedStep.setCompanyName(client.getEnterpriseName());

            return enrichedStep;
        }).toArray(ItineraryStepWithClient[]::new);

        // On récupère les coordonnées du domicile du commercial
        Double[] coord = accountService.getCoordPerson(Long.valueOf(itinerary.getCodeUser()));
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(coord[0]);
        coordinates.setLongitude(coord[1]);

        // On crée l'objet de réponse
        ItineraryWithCoordinates itineraryWithCoordinates = new ItineraryWithCoordinates();
        itineraryWithCoordinates.setItinerary(itinerary);
        itineraryWithCoordinates.setCoordinates(coordinates);

        ItineraryInfos infos = new ItineraryInfos();
        infos.setItinerary(itineraryWithCoordinates);
        infos.setSteps(stepsWithClient);

        return infos;
    }
}
