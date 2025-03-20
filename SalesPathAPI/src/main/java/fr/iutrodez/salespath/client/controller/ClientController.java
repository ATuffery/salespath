package fr.iutrodez.salespath.client.controller;

import fr.iutrodez.salespath.client.service.ClientService;
import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.itinerary.service.ItineraryClientService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Contrôleur REST pour gérer les opérations relatives aux clients.
 */
@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ItineraryClientService itineraryClientService;

    /**
     * Endpoint pour créer un nouveau client.
     * 
     * @param client L'objet Client à ajouter.
     * @return Une réponse HTTP avec un code 201 si réussi, ou un code 500 en cas
     *         d'erreur.
     */
    @Operation(summary = "Créer un nouveau client", description = "Ajoute un nouveau client à la base de données.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client ajouté avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                {
                                    "success": "Client ajouté avec succès"
                                }
                             """))),
            @ApiResponse(responseCode = "500", description = "Ajout non effectué en raison d'une erreur serveur",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                {
                                    "error": "Ajout non effectué. Erreur: ..."
                                }
                             """)))
    })
    @PostMapping()
    public ResponseEntity<?> createNewClient(@RequestBody Client client) {
        try {
            clientService.createClient(client);
            return ResponseEntity.status(201).body(Map.of("success", "Client ajouté avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Ajout non effectué."  + e.getMessage()));
        }
    }

    /**
     * Endpoint pour récupérer une liste de clients associés à une personne donnée.
     * 
     * @param id L'ID de la personne.
     * @return Une liste de clients avec un code 200 si réussi, ou null avec un code
     *         500 en cas d'erreur.
     */
    @Operation(summary = "Récupérer les clients associés à une personne", description = "Renvoie une liste de clients liés à une personne spécifique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des clients renvoyée avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                [
                                    {
                                        "enterpriseName": "Test",
                                        "address": "50 avenue de Bordeaux, Rodez",
                                        "description": "Test",
                                        "firstName": "Test",
                                        "lastName": "Test",
                                        "phoneNumber": "0101010101",
                                        "idPerson": 2,
                                        "id": "7987d4df-4a0e-4dc2-90b9-d5f6b059a642",
                                        "coordonates": [
                                            44.3600539,
                                            2.5756241
                                        ],
                                        "client": true
                                    }
                                ]
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou base de données",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                {
                                    "error": "Erreur serveur ou base de données"
                                }
                             """)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getClients(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(clientService.getClientsById(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint pour récupérer un client spécifique via son ID.
     * 
     * @param id L'ID du client.
     * @return L'objet Client correspondant avec un code 200 si réussi,
     *         null avec un code 500 en cas d'erreur de la BDD,
     *         ou un message d'erreur avec un code 404 si le client n'existe pas.
     */
    @Operation(summary = "Récupérer un client spécifique", description = "Renvoie les informations d'un client en fonction de son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouvé avec succès",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "404", description = "Aucun client trouvé avec cet ID",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Any client with this ID"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou base de données",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                {
                                    "error": "Erreur serveur ou base de données : ..."
                                }
                             """)))
    })
    @GetMapping(value = "/getOne/{id}")
    public ResponseEntity<?> getClient(@PathVariable String id) {
        try {
            return ResponseEntity.status(200).body(clientService.getClientById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur serveur ou base de données : "
                                                                           + e.getMessage()));
        }
    }

    /**
     * Endpoint pour supprimer un client via son ID.
     * 
     * @param id L'ID du client à supprimer.
     * @return Une réponse HTTP avec un code 200 si réussi, ou un message d'erreur
     *         avec un code 500 en cas d'échec ou 404 si l'id est invalide
     */
    @Operation(summary = "Supprimer un client", description = "Supprime un client de la base de données en fonction de son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client supprimé avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "success": "Suppression effectuée"
                                 }
                             """))),
            @ApiResponse(responseCode = "404", description = "Aucun client avec l'id spécifié",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Client non trouvé avec l'ID : 302"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou suppression non effectuée",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Suppression non effectuée : ..."
                                 }
                             """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable String id) {
        try {
            itineraryClientService.deleteClientById(id);
            return ResponseEntity.status(200).body(Map.of("success", "Suppression effectuée"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error","Suppression non effectuée : " + e.getMessage()));
        }
    }

    /**
     * Endpoint pour mettre à jour un client existant.
     * 
     * @param client L'objet Client contenant les nouvelles données.
     * @param id     L'ID du client à mettre à jour.
     * @return Une réponse HTTP avec un code 200 si réussi, ou un message d'erreur
     *         avec un code 500 en cas d'échec.
     */
    @Operation(summary = "Mettre à jour un client", description = "Met à jour les informations d'un client existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mise à jour effectuée avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "success": "Mise à jour effectuée"
                                 }
                             """))),
            @ApiResponse(responseCode = "404", description = "Aucun client avec l'id spécifié",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                {
                                    "error": "Client introuvable avec l'ID : 302"
                                }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou mise à jour non effectuée",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                {
                                    "error": "Modification non effectuée : ..."
                                }
                             """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@RequestBody Client client, @PathVariable String id) {
        try {
            itineraryClientService.updateClient(client, id);
            return ResponseEntity.status(200).body(Map.of("success", "Mise à jour effectuée"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Modification non effectué : " + e.getMessage()));
        }
    }

    /**
     * Récupère liste client et prospects a proximité
     * @param coordinates list de coordonner de client
     * @return la liste des clients
     */
    @Operation(summary = "Récupérer les clients et prospects à proximité",
            description = "Renvoie une liste de clients et prospects situés respectivement à moins de 200m et 1km des coordonnées fournies.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des clients et prospects récupérée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide (coordonnées incorrectes ou manquantes)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                 {
                                     "error": "Invalid request: missing or incorrect coordinates"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou base de données",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "error": "Erreur serveur ou base de données : ..."
                                }
                             """)))
    })
    @GetMapping(value = "/proximity")
    public ResponseEntity<?> searchProximity(@RequestBody double[] coordinates) {
        try {
            return ResponseEntity.status(200).body(clientService.getClientProximity(coordinates[0], coordinates[1]));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de la mise à jour du parcours :"
                    + e.getMessage()));
        }
    }
}
