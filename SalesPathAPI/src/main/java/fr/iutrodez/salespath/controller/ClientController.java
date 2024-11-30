package fr.iutrodez.salespath.controller;

import fr.iutrodez.salespath.model.Client;
import fr.iutrodez.salespath.service.ClientService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping(value = "/add")
    public ResponseEntity<String> createNewClient(@RequestBody Client client) {
        try {
            clientService.CreateClient(client);
            return ResponseEntity.status(201).body("");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error DB request 500");
        }
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Client>> getClients(@RequestParam Long id) {
        try {
            return ResponseEntity.status(200).body(clientService.GetClientsById(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping(value="/getOne")
    public ResponseEntity<Client> getClient(@RequestParam Long id) {
        try {
            return ResponseEntity.status(200).body(clientService.GetClientById(id));
        } catch (Exception e){
           return ResponseEntity.status(500).body(null);
        }
    }

}
