package fr.iutrodez.salespath.service;

import fr.iutrodez.salespath.model.SalesPerson;
import fr.iutrodez.salespath.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service permettant de gérer les comptes utilisateurs
 */
@Service
public class AccountService {

    @Autowired
    private IAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur du service
     *
     * @param passwordEncoder l'encodeur de mot de passe
     */
    public AccountService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Permet de récupérer la clé API d'un utilisateur en fonction de son email et
     * de son mot de passe
     * 
     * @param email    l'email de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @return la clé API de l'utilisateur si l'utilisateur existe,
     *         vide sinon
     */
    public Optional<SalesPerson> login(String email, String password) {
        Optional<SalesPerson> userOpt = accountRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            SalesPerson user = userOpt.get();

            // On vérifie que le mot de passe soit correct
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    /**
     * Permet d'ajouter un commercial en base de données
     * Vérifie que l'email ne soit pas utilisé dans un autre compte
     * 
     * @param salesPerson les infos de la personne à ajouter
     * @return sauvegarde les infos de la personne en base de données
     * @throws IllegalArgumentException si l'email est déjà utilisé
     */
    public SalesPerson addSalesPerson(SalesPerson salesPerson) {
        // On vérifie que l'email n'est pas déjà utilisé
        accountRepository.findByEmail(salesPerson.getEmail())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("An account with this email already exists");
                });

        try {
            // On génère l'API Key à l'aide de l'UUID
            String uuid = UUID.randomUUID().toString();
            String apiKey = uuid.replace("-", "");

            salesPerson.setApiKey(apiKey);

            // On hash le mot de passe
            salesPerson.setPassword(passwordEncoder.encode(salesPerson.getPassword()));

            return accountRepository.save(salesPerson);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the account : " + e.getMessage());
        }
    }

    /**
     * Permet de vérifier si une clé API existe en base de données
     * 
     * @param apiKey la clé API à vérifier
     * @return true si la clé API existe
     */
    public boolean existsByApiKey(String apiKey) {
        return accountRepository.existsByApiKey(apiKey).isPresent();
    }

    /**
     * Permet de mettre à jour les informations d'un commercial
     * 
     * @param id          l'id du commercial à mettre à jour
     * @param salesPerson les nouvelles informations du commercial
     * @return true si la mise à jour a réussi
     * @throws RuntimeException si une erreur est survenue lors de la mise à jour
     */
    public boolean updateSalesPerson(Long id, SalesPerson salesPerson) {
        return accountRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(salesPerson.getFirstName());
                    existing.setLastName(salesPerson.getLastName());
                    existing.setAddress(salesPerson.getAddress());
                    existing.setEmail(salesPerson.getEmail());
                    existing.setPassword(passwordEncoder.encode(salesPerson.getPassword()));

                    try {
                        accountRepository.save(existing);
                        return true;
                    } catch (Exception e) {
                        throw new RuntimeException("Error while saving the account : " + e.getMessage());
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("Account not found for ID : " + id));
    }

    /**
     * Permet de récupérer un commercial en fonction de sa clé API
     * 
     * @param apiKey la clé API du commercial
     * @return le commercial correspondant
     * @throws IllegalArgumentException si le commercial n'existe pas
     */
    public Optional<SalesPerson> getSalesPerson(String apiKey) {
        return accountRepository.findByApiKey(apiKey);
    }
}
