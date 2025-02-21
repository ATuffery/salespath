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
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

    /**
     * Crée un nouvel itinéraire
     * @param itinerary Les informations de l'itinéraire à créer
     * @return 201 si l'itinéraire a été créé avec succès,
     *        404 si le nom de l'itinéraire existe déjà,
     *        500 en cas d'erreur de sauvegarde
     */
    @Operation(summary = "Créer un nouvel itinéraire",
            description = "Permet de créer un itinéraire en envoyant les informations nécessaires.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Itinéraire créé avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "success": "Itinerary created"
                                 }
                             """))),
            @ApiResponse(responseCode = "404", description = "Nom déjà utilisé pour un itinéraire",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Itinerary name already exists"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Error while saving the itinerary : ..."
                                 }
                             """)))
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

    /**
     * Récupère les itinéraires d'un utilisateur
     * @param idUser L'identifiant de l'utilisateur
     * @return 200 avec les informations des itinéraire,
     *        404 si l'utilisateur n'existe pas ou n'a pas d'itinéraire,
     *        500 en cas d'erreur de serveur/base de donnée
     */
    @Operation(summary = "Obtenir les itinéraires d'un utilisateur",
            description = "Permet de récupérer tous les itinéraires associés à un utilisateur en fonction de son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itinéraires récupérés avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 [
                                     {
                                         "idItinerary": 1102,
                                         "nameItinerary": "test_modif",
                                         "codeUser": "2",
                                         "creationDate": "2025-02-20T21:46:28",
                                         "nbSteps": 3
                                     },
                                     {
                                         "idItinerary": 1152,
                                         "nameItinerary": "test_modif_2",
                                         "codeUser": "2",
                                         "creationDate": "2025-02-21T08:16:35",
                                         "nbSteps": 3
                                     }
                                 ]
                             """))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé ou aucun itinéraire pour l'utilisateur",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Account not found for ID : 22"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "..."
                                 }
                             """)))
    })
    @GetMapping("/{idUser}")
    public ResponseEntity<?> getItineraryUser(@PathVariable Long idUser) {
        try {
            return ResponseEntity.status(200).body(itineraryService.getItineraryUser(idUser));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Récupère les informations d'un itinéraire
     * @param id L'identifiant de l'itinéraire
     * @return 200 avec les informations de l'itinéraire,
     *       404 si l'itinéraire n'existe pas,
     *       500 en cas d'erreur de serveur/base de donnée
     */
    @Operation(summary = "Récupérer les informations d'un itinéraire avec les étapes",
            description = "Cette méthode permet de récupérer les informations détaillées pour un itinéraire, y compris les informations sur les clients pour chaque étape.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Informations sur l'itinéraire récupérées avec succès",
                                 content = @Content(mediaType = "application/json",
                                 schema = @Schema(implementation = ItineraryInfos.class))),
                    @ApiResponse(responseCode = "404", description = "Itinéraire ou étapes non trouvés",
                                 content = @Content(mediaType = "application/json",
                                 examples = @ExampleObject(value = """
                                         {
                                              "error": "Itinerary not found for ID : 1105"
                                          }
                                     """))),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur interne",
                                 content = @Content(mediaType = "application/json",
                                 examples = @ExampleObject(value = """
                                         {
                                             "error": "..."
                                         }
                                     """)))
            })
    @GetMapping("/getInfos/{id}")
    public ResponseEntity<?> getInfos(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(itineraryService.getAllInfos(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Supprime un itinéraire
     * @param id L'identifiant de l'itinéraire
     * @return 200 si l'itinéraire a été supprimé avec succès,
     *       404 si l'itinéraire n'existe pas,
     *       500 en cas d'erreur de serveur/base de donnée
     */
    @Operation(summary = "Supprimer un itinéraire",
            description = "Permet de supprimer un itinéraire à partir de son identifiant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itinéraire supprimé avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "success": "Itinerary deleted successfully"
                                 }
                             """))),
            @ApiResponse(responseCode = "404", description = "Itinéraire non trouvé",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Itinerary not found"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Error deleting itinerary : ..."
                                 }
                             """)))
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

    /**
     * Vérifie si un client est utilisé dans une étape d'un itinéraire
     * @param idClient L'identifiant du client
     * @return 200 avec un objet Json pour indiquer si le client est utilisé,
     *         404 si l'id du client n'existe pas,
     *         500 en cas d'erreur de serveur/base de donnée
     */
    @Operation(summary = "Vérifier si un client est utilisé dans un itinéraire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information si le client est utilisé ou pas",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "success": false
                                 }
                             """))),
            @ApiResponse(responseCode = "404", description = "Client non trouvé",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Client introuvable avec l'ID : 7402a264-0f10-4489-9199-754ddc2ba1dD"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Erreur serveur ou base de données : ..."
                                 }
                             """)))
    })
    @GetMapping("/verify/{idClient}")
    public ResponseEntity<?> verifyUtilisation(@PathVariable String idClient) {
        try {
            return ResponseEntity.ok(Map.of("success", itineraryStepService.existsByIdClient(idClient)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur serveur ou base de données : "
                                                                          + e.getMessage()));
        }
    }
}
