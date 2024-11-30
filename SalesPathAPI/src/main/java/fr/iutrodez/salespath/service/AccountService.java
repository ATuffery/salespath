package fr.iutrodez.salespath.service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.iutrodez.salespath.model.SalesPerson;
import fr.iutrodez.salespath.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private IAccountRepository accountRepository;

    /**
     * Permet de récupérer la clé API d'un utilisateur en fonction de son email et de son mot de passe
     * @param email l'email de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @return la clé API de l'utilisateur si l'utilisateur existe
     */
    public Optional<String> login(String email, String password) {
        return accountRepository.findByUsernameAndPassword(email, password);
    }

    /**
     * Permet d'ajouter un commercial en base de données
     * Vérifie que l'email ne soit pas utilisé dans un autre compte
     * @param salesPerson les infos de la personne à ajouter
     * @return sauvegarde les infos de la personne en base de données
     * @throws IllegalArgumentException si l'email est déjà utilisé
     */
    public SalesPerson addSalesPerson (SalesPerson salesPerson) {
        // On vérifie que l'email n'est pas déjà utilisé
        accountRepository.findByEmail(salesPerson.getEmail())
                         .ifPresent(existing -> {
                            throw new IllegalArgumentException("An account with this email already exists");
                         });

        try {
            String uuid = UUID.randomUUID().toString();
            String apiKey = uuid.replace("-", "").substring(0, 30);

            salesPerson.setApiKey(apiKey);
            return accountRepository.save(salesPerson);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the account : " + e.getMessage());
        }
    }

    /**
     * Permet de vérifier si une clé API existe en base de données
     * @param apiKey la clé API à vérifier
     * @return true si la clé API existe
     */
    public boolean existsByApiKey(String apiKey) {
        return accountRepository.existsByApiKey(apiKey).isPresent();
    }

    /**
     * Permet de mettre à jour les informations d'un commercial
     * @param id l'id du commercial à mettre à jour
     * @param salesPerson les nouvelles informations du commercial
     * @return true si la mise à jour a réussi
     *         false si la personne n'est pas trouvé
     * @throws RuntimeException si une erreur est survenue lors de la mise à jour
     */
    public boolean updateSalesPerson(Long id, SalesPerson salesPerson) {
        return accountRepository.findById(id)
                                .map(existing -> {
                                    existing.setFirstName(salesPerson.getFirstName());
                                    existing.setLastName(salesPerson.getLastName());
                                    existing.setAddress(salesPerson.getAddress());
                                    existing.setEmail(salesPerson.getEmail());
                                    existing.setPassword(salesPerson.getPassword());

                                    try {
                                        accountRepository.save(existing);
                                        return true;
                                    } catch (Exception e) {
                                        throw new RuntimeException("Error while saving the account : " + e.getMessage());
                                    }
                                })
                                .orElseThrow(() -> new IllegalArgumentException("Account not found for ID : " + id));
    }
}
