package fr.iutrodez.salespath.route.controller;

import fr.iutrodez.salespath.account.model.SalesPerson;
import fr.iutrodez.salespath.account.repository.IAccountRepository;
import fr.iutrodez.salespath.client.repository.IClientRepository;
import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.itinerary.repository.IItineraryRepository;
import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import fr.iutrodez.salespath.itinerarystep.service.ItineraryStepService;
import fr.iutrodez.salespath.route.dto.RouteStep;
import fr.iutrodez.salespath.route.model.Route;
import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.route.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Contrôleur pour gérer les routes.
 */
@RestController
@RequestMapping(value = "/route")
@Tag(name = "Route Controller", description = "Gestion des parcours commerciaux")
public class RouteController {

    @Autowired
    private RouteService routeService;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IItineraryRepository itineraryRepository;
    @Autowired
    private IClientRepository clientRepository;
    @Autowired
    private ItineraryStepService itineraryStepService;

    /**
     * Endpoint pour créer une nouvelle route.
     * @param idSalesPerson  L'ID de la personne.
     * @param idItinerary L'ID de l'itinéraire.
     * @param route L'objet Route à ajouter.
     * @return Une réponse HTTP avec un code 201 si réussi, un code 500 en cas de problème avec la BDD ou un code 404
     * si le commercial/l'itinéraire n'existe pas
     */
    @Operation(summary = "Créer une nouvelle route",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Route.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcours ajouté avec succès"),
            @ApiResponse(responseCode = "404", description = "ID commerical non trouvé, ID itinéraire non trouvé " +
                                                             "ou ID client non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la sauvegarde du parcours")
    })
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

            // On récupère les clients de l'itinéraire
            ArrayList<RouteStep> stepsList = new ArrayList<>();

            Optional<ItineraryStep[]> stepsOpt = itineraryStepService.getSteps(String.valueOf(itinerary.getIdItinerary()));

            if (stepsOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Aucun client trouvé pour l'itinéraire : "
                                                                               + itinerary.getIdItinerary()));
            }

            ItineraryStep[] steps = stepsOpt.get();

            for (ItineraryStep step : steps) {
                Optional<Client> clientOpt = clientRepository.findById(step.getIdClient());

                if (clientOpt.isEmpty()) {
                    return ResponseEntity.status(404).body(Map.of("error", "Client non trouvé : "
                                                                                   + step.getIdClient()));
                }

                Client client = clientOpt.get();

                stepsList.add(new RouteStep(client, 2));

            }

            route.setIdSalesPerson(idSalesPerson);
            route.setItineraryId(idItinerary);
            route.setItineraryName(itinerary.getNameItinerary());
            route.setSteps(stepsList);
            route.setStartDate(LocalDateTime.now());

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
    @Operation(summary = "Récupérer tous les parcours d'un commercial",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des parcours",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Route.class))),
                    @ApiResponse(responseCode = "404", description = "ID commercial non trouvé"),
                    @ApiResponse(responseCode = "500", description = "Erreur lors de la récupération du parcours")})
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
    @Operation(summary = "Récupérer un parcours par son ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Détails du parcours",
                                 content = @Content(mediaType = "application/json",
                                 schema = @Schema(implementation = Route.class))),
                    @ApiResponse(responseCode = "404", description = "Parcours non trouvé"),
                    @ApiResponse(responseCode = "500", description = "Erreur lors de la récupération du parcours")})
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

    /**
     * Endpoint pour supprimer un parcours.
     * @param idRoute L'ID du parcours.
     * @return Une réponse HTTP avec un code 200 si réussi, un code 500 en cas de problème avec la BDD ou un code 404 si
     *         le parcours n'existe pas
     */
    @Operation(summary = "Supprimer un parcours", description = "Cette méthode permet de supprimer un parcours en fonction de son identifiant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcours supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Parcours non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la suppression du parcours")
    })
    @DeleteMapping(value = "/{idRoute}")
    public ResponseEntity<?> deleteRoute(@PathVariable String idRoute) {
        try {
            routeService.deleteRoute(idRoute);
            return ResponseEntity.status(200).body(Map.of("success", "Parcours supprimé avec succès"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de la suppression du parcours :"
                                                                          + e.getMessage()));
        }
    }

    /**
     * Endpoint pour mettre à jour un parcours.
     * @param route L'objet Route à mettre à jour.
     * @return Une réponse HTTP avec un code 200 si réussi, un code 500 en cas de problème avec la BDD ou un code 404 si
     *         le parcours n'existe pas
     */
    @Operation(summary = "Mettre à jour un parcours",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Route.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcours mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Parcours non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la mise à jour du parcours")
    })
    @PutMapping()
    public ResponseEntity<?> updateRoute(@RequestBody Route route) {
        try {
            routeService.updateRoute(route);
            return ResponseEntity.status(200).body(Map.of("success", "Parcours mis à jour avec succès"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de la mise à jour du parcours :"
                                                                          + e.getMessage()));
        }
    }
}
