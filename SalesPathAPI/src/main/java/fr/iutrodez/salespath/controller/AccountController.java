package fr.iutrodez.salespath.controller;

import fr.iutrodez.salespath.model.SalesPerson;
import fr.iutrodez.salespath.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value="/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Endpoint pour se connecter à un compte
     * @param email l'email de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @return la clé API de l'utilisateur si l'utilisateur existe
     */
    @GetMapping(value="/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Optional<String> apiKey = accountService.login(email, password);

        if (apiKey.isPresent()) {
            return ResponseEntity.ok(Map.of("apiKey", apiKey.get()));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Account not found"));
        }
    }

    /**
     * Endpoint pour se récupérer les informations d'un utilisateur
     * @param apiKey la clé API de l'utilisateur
     * @return les informations si l'utilisateur existe
     */
    @GetMapping(value="/infos/{apiKey}")
    public ResponseEntity<?> infos(@PathVariable String apiKey) {
        Optional<SalesPerson> salesPersonOptional = accountService.getSalesPerson(apiKey);

        if (salesPersonOptional.isPresent()) {
            SalesPerson salesPerson = salesPersonOptional.get();
            Map<String, Object> response = Map.of (
                    "firstName", salesPerson.getFirstName(),
                    "lastName", salesPerson.getLastName(),
                    "email", salesPerson.getEmail(),
                    "address", salesPerson.getAddress()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Account not found for API Key : " + apiKey));
        }
    }

    /**
     * Endpoint pour ajouter un commercial
     * @param salesPerson les infos du commercial à ajouter
     * @return un code 201 si le commercial a été ajouté
     *        un code 400 si l'email est déjà utilisé
     *        un code 500 si une erreur est survenue lors de l'ajout
     */
    @PostMapping(value = "/add")
    public ResponseEntity<?> add(@RequestBody SalesPerson salesPerson) {
        try {
            accountService.addSalesPerson(salesPerson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }

        return ResponseEntity.status(201).body(Map.of("success", "Account created"));
    }

    /**
     * Endpoint pour mettre à jour un commercial
     * @param id l'id du commercial à mettre à jour
     * @param salesPerson les infos du commercial à mettre à jour
     * @return un code 200 si le commercial a été mis à jour
     *         un code 404 si l'id en paramètre est invalide
     *         un code 500 si une erreur est survenue lors de la mise à jour
     */
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SalesPerson salesPerson) {
        try {
            accountService.updateSalesPerson(id, salesPerson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }

        return ResponseEntity.status(201).body(Map.of("success", "Account updated"));
    }
}