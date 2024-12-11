package fr.iutrodez.salespath.controller;

import fr.iutrodez.salespath.model.Client;
import fr.iutrodez.salespath.service.ClientService;
import org.bson.types.ObjectId;
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
    @PostMapping(value = "/add")
    public ResponseEntity<?> createNewClient(@RequestBody Client client) {
        try {
            clientService.CreateClient(client);
            return ResponseEntity.status(201).body(Map.of("success", "Client ajouté avec succès"));
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return ResponseEntity.status(500).body(Map.of("Erreur", "Ajout non effectué (500)"));
        }
    }

    /**
     * Endpoint pour récupérer une liste de clients associés à une personne donnée.
     * 
     * @param id L'ID de la personne.
     * @return Une liste de clients avec un code 200 si réussi, ou null avec un code
     *         500 en cas d'erreur.
     */
    @GetMapping(value = "/get")
    public ResponseEntity<?> getClients(@RequestParam Long id) {
        try {
            return ResponseEntity.status(200).body(clientService.GetClientsById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("Error", "type Id not valid"));
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return ResponseEntity.status(500).body(Map.of("Error", "in BDD"));
        }
    }

    /**
     * Endpoint pour récupérer un client spécifique via son ID.
     * 
     * @param id L'ID du client.
     * @return L'objet Client correspondant avec un code 200 si réussi, ou null avec
     *         un code 500 en cas d'erreur.
     */
    @GetMapping(value = "/getOne")
    public ResponseEntity<?> getClient(@RequestParam String id) {
        try {
            return ResponseEntity.status(200).body(clientService.GetClientById(id));
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return ResponseEntity.status(500).body(Map.of("Error", "in BDD"));
        }
    }

    /**
     * Endpoint pour supprimer un client via son ID.
     * 
     * @param id L'ID du client à supprimer.
     * @return Une réponse HTTP avec un code 200 si réussi, ou un message d'erreur
     *         avec un code 500 en cas d'échec.
     */
    @DeleteMapping(value = "/deleteOne")
    public ResponseEntity<?> deleteClient(@RequestParam String id) {
        try {
            clientService.DeleteClientById(id);
            return ResponseEntity.status(200).body(Map.of("success", "Suppression effectuée"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("Error","Suppression non effectuée"));
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
    @PutMapping(value = "/updateOne")
    public ResponseEntity<?> updateClient(@RequestBody Client client, @RequestParam String id) {
        try {
            clientService.UpdateClient(client, id);
            return ResponseEntity.status(200).body(Map.of("success", "Mise à jour effectuée"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("Error", "Modification non effectué (500)"));
        }
    }
}
