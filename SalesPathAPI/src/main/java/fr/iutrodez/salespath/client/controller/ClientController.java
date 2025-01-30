package fr.iutrodez.salespath.client.controller;

import fr.iutrodez.salespath.client.service.ClientService;
import fr.iutrodez.salespath.client.model.Client;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur REST pour gérer les opérations relatives aux clients.
 */
@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

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
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Ajout non effectué en raison d'une erreur serveur",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping()
    public ResponseEntity<?> createNewClient(@RequestBody Client client) {
        try {
            clientService.createClient(client);
            return ResponseEntity.status(201).body(Map.of("success", "Client ajouté avec succès"));
        } catch (Exception e) {
            System.err.print(e.getMessage());
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
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "ID fourni non valide",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou base de données",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getClients(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(clientService.getClientsById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", "type Id not valid"));
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "in BDD"));
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
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou base de données",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/getOne/{id}")
    public ResponseEntity<?> getClient(@PathVariable String id) {
        try {
            return ResponseEntity.status(200).body(clientService.getClientById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Any client with this ID"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", "in BDD"));
        }
    }

    /**
     * Endpoint pour supprimer un client via son ID.
     * 
     * @param id L'ID du client à supprimer.
     * @return Une réponse HTTP avec un code 200 si réussi, ou un message d'erreur
     *         avec un code 500 en cas d'échec.
     */
    @Operation(summary = "Supprimer un client", description = "Supprime un client de la base de données en fonction de son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client supprimé avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou suppression non effectuée",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable String id) {
        try {
            clientService.deleteClientById(id);
            return ResponseEntity.status(200).body(Map.of("success", "Suppression effectuée"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error","Suppression non effectuée"));
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
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erreur serveur ou mise à jour non effectuée",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@RequestBody Client client, @PathVariable String id) {
        try {
            clientService.updateClient(client, id);
            return ResponseEntity.status(200).body(Map.of("success", "Mise à jour effectuée"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Modification non effectué (500)"));
        }
    }
}
