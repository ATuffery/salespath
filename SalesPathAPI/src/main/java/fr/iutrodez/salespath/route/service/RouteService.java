package fr.iutrodez.salespath.route.service;

import fr.iutrodez.salespath.account.model.SalesPerson;
import fr.iutrodez.salespath.account.repository.IAccountRepository;
import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.client.repository.IClientRepository;
import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.itinerary.repository.IItineraryRepository;
import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import fr.iutrodez.salespath.itinerarystep.service.ItineraryStepService;
import fr.iutrodez.salespath.route.dto.Coordinates;
import fr.iutrodez.salespath.route.dto.RouteStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import fr.iutrodez.salespath.route.repository.IRouteRepository;
import fr.iutrodez.salespath.route.model.Route;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service pour gérer les opérations relatives aux parcours.
 */
@Service
public class RouteService {

    @Autowired
    private IRouteRepository routeRepository;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private IItineraryRepository itineraryRepository;

    @Autowired
    private ItineraryStepService itineraryStepService;

    @Autowired
    private IClientRepository clientRepository;

    /**
     * Crée un nouveau parcours et l'enregistre dans la base de données.
     *
     * @param route L'objet Route à créer.
     * @throws RuntimeException En cas d'erreur lors de la création.
     */
    public Route createRoute(Route route, Long idSalesPerson, Long idItinerary) {
        // On vérifie que le commercial existe
        accountRepository.findById(idSalesPerson)
                         .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé pour l'ID : "
                                                                       + idSalesPerson));

        // On vérifie que l'itinéraire existe
        Optional<Itinerary> itineraryOpt = itineraryRepository.findById(idItinerary);

        if (itineraryOpt.isEmpty()) {
            throw new NoSuchElementException("Itinéraire non trouvé pour l'ID : " + idItinerary);
        }

        Itinerary itinerary = itineraryOpt.get();

        // On récupère les clients de l'itinéraire
        ArrayList<RouteStep> stepsList = new ArrayList<>();

        ItineraryStep[] steps = itineraryStepService.getSteps(String.valueOf(itinerary.getIdItinerary()));

        for (ItineraryStep step : steps) {
            // On vérifie que le client existe
            Optional<Client> clientOpt = clientRepository.findById(step.getIdClient());

            if (clientOpt.isEmpty()) {
                throw new NoSuchElementException("Client non trouvé pour l'ID : " + step.getIdClient());
            }

            Client client = clientOpt.get();

            // Par défaut le statut est "UNVISITED"
            stepsList.add(new RouteStep(client, "UNVISITED"));

        }

        route.setIdSalesPerson(idSalesPerson);
        route.setItineraryId(idItinerary);
        route.setItineraryName(itinerary.getNameItinerary());
        route.setSteps(stepsList);
        route.setStartDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));

        try {
            return routeRepository.save(route);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Récupère tous les parcours d'un utilisateur.
     * @param idSalesPerson L'ID de l'utilisateur.
     * @return La liste des parcours de l'utilisateur.
     * @throws RuntimeException En cas d'erreur lors de la récupération.
     * @throws NoSuchElementException Si l'utilisateur n'existe pas.
     */
    public ArrayList<Route> getAllRoutes(Long idSalesPerson) {
        accountRepository.findById(idSalesPerson)
                         .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé pour l'ID : "
                                                                       + idSalesPerson));

        try {
            return routeRepository.findByIdSalesPerson(idSalesPerson);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Récupère un parcours par son ID.
     * @param id L'ID du parcours.
     * @return Le parcours correspondant à l'ID.
     * @throws NoSuchElementException Si le parcours n'existe pas.
     */
    public Route getRouteById(String id) {
        return routeRepository.findById(id)
                              .orElseThrow(() -> new NoSuchElementException("Parcours non trouvé pour l'ID : " + id));
    }

    /**
     * Supprime un parcours par son ID.
     * @param id L'ID du parcours à supprimer.
     * @throws NoSuchElementException Si le parcours n'existe pas.
     */
    public void deleteRoute(String id) {
        routeRepository.findById(id)
                       .orElseThrow(() -> new NoSuchElementException("Parcours non trouvé pour l'ID : " + id));

        routeRepository.deleteById(id);
    }

    /**
     * Met à jour un parcours.
     * @param route Le parcours à mettre à jour.
     * @throws NoSuchElementException Si le parcours n'existe pas.
     */
    public void updateRoute(Route route) {
        Route existingRoute = routeRepository.findById(route.getId())
                                             .orElseThrow(() -> new NoSuchElementException("Parcours non trouvé pour " +
                                                                                           "l'ID : " + route.getId()));

        existingRoute.setEndDate(route.getEndDate());
        existingRoute.setStatus(route.getStatus());

        if (existingRoute.getLocalisation() == null) {
            existingRoute.setLocalisation(new ArrayList<>());
        }

        // On ajoute les nouvelles localisations aux anciennes (pas de suppression des anciennes)
        existingRoute.getLocalisation().addAll(route.getLocalisation());

        for (RouteStep stepExisting : existingRoute.getSteps()) {
            for (RouteStep step : route.getSteps()) {
                if (stepExisting.getClient().getId().equals(step.getClient().getId())) {
                    stepExisting.setStatus(step.getStatus());
                }
            }
        }

        routeRepository.save(existingRoute);
    }
}
