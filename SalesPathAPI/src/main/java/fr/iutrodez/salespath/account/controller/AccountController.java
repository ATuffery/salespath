package fr.iutrodez.salespath.account.controller;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.account.model.SalesPerson;
import fr.iutrodez.salespath.account.dto.SalesPersonUpdateRequest;
import fr.iutrodez.salespath.utils.exception.DifferentPasswordException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Controller pour les endpoints liés aux comptes
 */
@RestController
@RequestMapping(value="/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Endpoint pour se connecter à un compte
     * @param email l'email de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @return la clé API de l'utilisateur et l'id si l'utilisateur existe
     */
    @Operation(summary = "Connexion à un compte", description = "Permet de se connecter avec un email et un mot de passe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion réussie, clé API et ID renvoyée",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "id": 1,
                                     "apiKey": "91e8d1564aef4f94ac897e348b47b15e"
                                 }
                                 """))),
            @ApiResponse(responseCode = "404", description = "Compte non trouvé",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                {
                                    "error": "Invalid email or password"
                                }
                                """))),
            @ApiResponse(responseCode = "500", description = "Erreur de base de données",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "error": "Database error : error message ..."
                                }
                                """)))
    })
    @GetMapping(value = "/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            SalesPerson salesPerson = accountService.login(email, password);

            return ResponseEntity.ok(Map.of("apiKey", salesPerson.getApiKey(), "id", salesPerson.getId()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Database error : " + e.getMessage()));
        }

    }

    /**
     * Endpoint pour se récupérer les informations d'un utilisateur
     * @param apiKey la clé API de l'utilisateur
     * @return les informations si l'utilisateur existe
     */
    @Operation(summary = "Récupérer les informations d'un utilisateur", description = "Renvoie les informations d'un" +
                                                                                      " utilisateur avec sa clé API.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Informations de l'utilisateur récupérées avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "address": "50 Avenue de Bordeaux - Rodez",
                                     "email": "a",
                                     "longitude": 2.5756241,
                                     "latitude": 44.3600539,
                                     "lastName": "Test_API",
                                     "firstName": "Test_API"
                                 }
                            """))),
            @ApiResponse(responseCode = "403", description = "Si la clé API est invalide et donc le compte n'existe pas",
                         content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erreur de base de données",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                {
                                    "error": "Database error : error message ..."
                                }
                            """)))
    })
    @GetMapping(value="/infos")
    public ResponseEntity<?> infos(@RequestHeader("X-API-KEY") String apiKey) {
        try {
            return ResponseEntity.ok(accountService.getSalesPerson(apiKey));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Database error : " + e.getMessage()));
        }
    }

    /**
     * Endpoint pour ajouter un commercial
     * @param salesPerson les infos du commercial à ajouter
     * @return un code 201 si le commercial a été ajouté
     *        un code 400 si l'email est déjà utilisé
     *        un code 500 si une erreur est survenue lors de l'ajout
     */
    @Operation(summary = "Ajouter un nouveau commercial", description = "Ajoute un nouveau compte commercial.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Compte créé avec succès",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "success": "Account created"
                                 }
                             """))),
            @ApiResponse(responseCode = "400", description = "Email déjà utilisé ou données invalides",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "An account with this email already exists"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur interne lors de la création du compte",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Error while saving the account : ...."
                                 }
                             """)))
    })
    @PostMapping()
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
     * @param request les infos du commercial à mettre à jour
     * @return un code 200 si le commercial a été mis à jour
     *         un code 404 si aucun compte n'est trouvé
     *         un code 500 si une erreur est survenue lors de la mise à jour
     *         un code 400 si l'ancien mdp n'est pas le même que celui en BDD ou si le mail est déjà utilisé
     */
    @Operation(summary = "Mettre à jour un commercial", description = "Met à jour les informations d'un compte " +
                                                                      "commercial existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mise à jour réussie",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "success": "Account updated"
                                 }
                             """))),
            @ApiResponse(responseCode = "404", description = "ID du commercial inexistant",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "Account not found for ID : 1022"
                                 }
                             """))),
            @ApiResponse(responseCode = "400", description = "Ancien mot de passe incorrect ou données invalides",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "An account with this email already exists"
                                 }
                             """))),
            @ApiResponse(responseCode = "500", description = "Erreur interne lors de la mise à jour",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = """
                                 {
                                     "error": "...."
                                 }
                             """)))
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SalesPersonUpdateRequest request) {
        try {
            accountService.updateSalesPerson(id, request);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (DifferentPasswordException | IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }

        return ResponseEntity.status(201).body(Map.of("success", "Account updated"));
    }
}