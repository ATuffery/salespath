package fr.iutrodez.salespath.route.controller;

import fr.iutrodez.salespath.account.model.SalesPerson;
import fr.iutrodez.salespath.account.repository.IAccountRepository;
import fr.iutrodez.salespath.client.repository.IClientRepository;
import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.itinerary.repository.IItineraryRepository;
import fr.iutrodez.salespath.route.dto.RouteStep;
import fr.iutrodez.salespath.route.model.Route;
import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.route.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur pour gérer les routes.
 */
@RestController
@RequestMapping(value = "/route")
public class RouteController {

    @Autowired
    private RouteService routeService;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IItineraryRepository itineraryRepository;
    @Autowired
    private IClientRepository clientRepository;

    /**
     * Endpoint pour créer une nouvelle route.
     * @param idSalesPerson  L'ID de la personne.
     * @param idItinerary L'ID de l'itinéraire.
     * @param route L'objet Route à ajouter.
     * @return Une réponse HTTP avec un code 201 si réussi, un code 500 en cas de problème avec la BDD ou un code 404
     * si le commercial/l'itinéraire n'existe pas
     */
    @PostMapping()
    public ResponseEntity<?> createNewRoute(@RequestParam Long idSalesPerson, @RequestParam Long idItinerary,
                                            @RequestBody Route route) {
        try {
            // On vérifie que le commercial existe
            Optional<SalesPerson> salesPersonOpt = accountRepository.findById(idSalesPerson);

            if (salesPersonOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "ID commercial non trouvé"));
            }

            // On vérifie que l'itinéraire existe
            Optional<Itinerary> itineraryOpt = itineraryRepository.findById(idItinerary);

            if (itineraryOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "ID itinéraire non trouvé"));
            }

            Itinerary itinerary = itineraryOpt.get();

            // On vérifie que les clients des steps existent
            RouteStep[] steps = route.getSteps();
            for (RouteStep step : steps) {
                Optional<Client> clientOpt = clientRepository.findById(step.getClient().getId());

                if (clientOpt.isEmpty()) {
                    return ResponseEntity.status(404).body(Map.of("error", "Client non trouvé : "
                                                                                   + step.getClient().getId()));
                }
            }

            route.setIdSalesPerson(idSalesPerson);
            route.setItineraryId(idItinerary);
            route.setItineraryName(itinerary.getNameItinerary());

            routeService.createRoute(route);
            return ResponseEntity.status(201).body(Map.of("success", "Route ajoutée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Ajout non effectué."  + e.getMessage()));
        }
    }
}
