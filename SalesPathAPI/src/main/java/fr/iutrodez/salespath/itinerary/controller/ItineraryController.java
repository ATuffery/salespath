package fr.iutrodez.salespath.itinerary.controller;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.client.service.ClientService;
import fr.iutrodez.salespath.itinerary.dto.ItineraryAddRequest;
import fr.iutrodez.salespath.itinerary.dto.ItineraryInfos;
import fr.iutrodez.salespath.itinerary.dto.ItineraryWithCoordinates;
import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.itinerary.service.ItineraryService;
import fr.iutrodez.salespath.itinerarystep.dto.ItineraryStepWithClient;
import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import fr.iutrodez.salespath.itinerarystep.service.ItineraryStepService;
import fr.iutrodez.salespath.route.dto.Coordinates;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/itinerary")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ItineraryStepService itineraryStepService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Créer un nouvel itinéraire",
            description = "Permet de créer un itinéraire en envoyant les informations nécessaires.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Itinéraire créé avec succès"),
            @ApiResponse(responseCode = "404", description = "Erreur dans les données fournies"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PostMapping()
    public ResponseEntity<?> createItinerary(@RequestBody ItineraryAddRequest itinerary) {
        try {
            itineraryService.createItinerary(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.status(201).body(Map.of("success", "Itinerary created"));
    }

    @Operation(summary = "Obtenir les itinéraires d'un utilisateur",
            description = "Permet de récupérer tous les itinéraires associés à un utilisateur en fonction de son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itinéraires récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/{idUser}")
    public ResponseEntity<?> getItineraryUser(@PathVariable Long idUser) {
        try {
            Optional<Itinerary[]> itinerariesOpt = itineraryService.getItineraryUser(idUser);

            Itinerary[] itineraries = itinerariesOpt.get();
            return ResponseEntity.status(200).body(itineraries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Id user not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer les informations d'un itinéraire avec les étapes",
            description = "Cette méthode permet de récupérer les informations détaillées pour un itinéraire, y compris les informations sur les clients pour chaque étape.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Informations sur l'itinéraire récupérées avec succès",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ItineraryInfos.class))),
                    @ApiResponse(responseCode = "404", description = "Itinéraire ou étapes non trouvés",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur interne",
                            content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/getInfos/{id}")
    public ResponseEntity<?> getInfos(@PathVariable Long id) {
        try {
            // On récupère l'itinéraire
            Optional<Itinerary> itineraryOpt = itineraryService.getItinerary(id);

            if (itineraryOpt.isPresent()) {
                Itinerary itinerary = itineraryOpt.get();

                // On récupère les étapes de l'itinéraire
                Optional<ItineraryStep[]> stepsOpt = itineraryStepService.getSteps(String.valueOf(itinerary.getIdItinerary()));

                if (stepsOpt.isPresent()) {
                    ItineraryStep[] steps = stepsOpt.get();
                    List<ItineraryStep> stepsList = Arrays.asList(steps);

                    // Ajouter les informations des clients
                    ItineraryStepWithClient[] stepsWithClient = (ItineraryStepWithClient[]) stepsList.stream().map(step -> {
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

                    return ResponseEntity.status(200).body(infos);
                } else {
                    return ResponseEntity.status(404).body(Map.of("error", "No steps found for this itinerary"));
                }
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Itinerary not found for this Id"));
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Supprimer un itinéraire",
            description = "Permet de supprimer un itinéraire à partir de son identifiant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itinéraire supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Itinéraire non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItinerary(@PathVariable Long id) {
        try {;
            if (itineraryService.deleteItinerary(id)) {
                return ResponseEntity.ok(Map.of("success", "Itinerary deleted successfully"));
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Itinerary not found"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

}
