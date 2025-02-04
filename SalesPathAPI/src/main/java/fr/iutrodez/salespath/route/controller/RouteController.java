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
import java.util.NoSuchElementException;
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

                Client client = clientOpt.get();

                step.getClient().setFirstName(client.getFirstName());
                step.getClient().setLastName(client.getLastName());
                step.getClient().setEnterpriseName(client.getEnterpriseName());
                step.getClient().setAddress(client.getAddress());
                step.getClient().setDescription(client.getDescription());
                step.getClient().setPhoneNumber(client.getPhoneNumber());
                step.getClient().setIdPerson(client.getIdPerson());
                step.getClient().setCoordonates(client.getCoordonates());
                step.getClient().setClient(client.getClient());

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

    /**
     * Endpoint pour récupérer tous les parcours d'un commercial.
     * @param idSalesPerson L'ID du commercial.
     * @return Une réponse HTTP avec un code 200 et la liste des parcours si réussi, un code 500 en cas de problème avec
     *         la BDD ou un code 404 si le commercial n'existe pas
     */
    @GetMapping(value = "/{idSalesPerson}")
    public ResponseEntity<?> getAllRoutes(@PathVariable Long idSalesPerson) {
        try {
            // On vérifie que le commercial existe
            Optional<SalesPerson> salesPersonOpt = accountRepository.findById(idSalesPerson);

            if (salesPersonOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "ID commercial non trouvé"));
            }

            return ResponseEntity.status(200).body(routeService.getAllRoutes(idSalesPerson));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de la récupération des parcours : "
                                                                          + e.getMessage()));
        }
    }

    /**
     * Endpoint pour récupérer un parcours par son ID.
     * @param idRoute L'ID du parcours.
     * @return Une réponse HTTP avec un code 200 et le parcours si réussi, un code 500 en cas de problème avec la BDD ou
     *         un code 404 si le parcours n'existe pas
     */
    @GetMapping(value = "/getOne/{idRoute}")
    public ResponseEntity<?> getOneRoute(@PathVariable String idRoute) {
        try {
            return ResponseEntity.status(200).body(routeService.getRouteById(idRoute));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de la récupération du parcours :"
                                                                          + e.getMessage()));
        }
    }
}
