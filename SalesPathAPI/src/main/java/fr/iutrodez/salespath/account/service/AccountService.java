package fr.iutrodez.salespath.account.service;

import fr.iutrodez.salespath.account.model.SalesPerson;
import fr.iutrodez.salespath.account.dto.SalesPersonUpdateRequest;
import fr.iutrodez.salespath.account.repository.IAccountRepository;
import fr.iutrodez.salespath.utils.Utils;
import fr.iutrodez.salespath.utils.exception.CoordinatesException;
import fr.iutrodez.salespath.utils.exception.DifferentPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
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
    public SalesPerson login(String email, String password) {
        return accountRepository.findByEmail(email)
                                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                                .orElseThrow(() -> new NoSuchElementException("Invalid email or password."));
    }


    /**
     * Permet d'ajouter un commercial en base de données
     * Vérifie que l'email ne soit pas utilisé dans un autre compte
     *
     * @param salesPerson les infos de la personne à ajouter
     * @throws IllegalArgumentException si l'email est déjà utilisé
     */
    public void addSalesPerson(SalesPerson salesPerson) {
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

            // On récupère les coordonnées de l'adresse
            double[] coord = Utils.GetCoordByAddress(salesPerson.getAddress());

            salesPerson.setLatitude(coord[1]);
            salesPerson.setLongitude(coord[0]);

            accountRepository.save(salesPerson);
        } catch (CoordinatesException e) {
            throw new RuntimeException("Error while getting coordinates : " + e.getMessage());

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
     * @param id l'ID du commercial à mettre à jour
     * @param request les informations à mettre à jour
     * @throws DifferentPasswordException si le mot de passe actuel est incorrect
     * @throws RuntimeException si une erreur est survenue lors de la mise à jour
     * @throws IllegalArgumentException si le compte n'existe pas
     */
    public void updateSalesPerson(Long id, SalesPersonUpdateRequest request) throws DifferentPasswordException {
        SalesPerson existing = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account not found for ID : " + id));

        // On vérifie que l'email n'est pas déjà utilisé
        accountRepository.findByEmail(request.getSalesPerson().getEmail())
                         .filter(salesPerson -> !salesPerson.getId().equals(id))
                         .ifPresent(error -> {
                            throw new IllegalArgumentException("An account with this email already exists");
                });

        // On vérifie les mots de passe
        if (!request.getOldPassword().isEmpty() && !passwordEncoder.matches(request.getOldPassword(),
                                                                            existing.getPassword())) {
            throw new DifferentPasswordException("Invalid old password");
        }

        existing.setFirstName(request.getSalesPerson().getFirstName());
        existing.setLastName(request.getSalesPerson().getLastName());
        existing.setAddress(request.getSalesPerson().getAddress());
        existing.setEmail(request.getSalesPerson().getEmail());

        try {
            // On récupère les coordonnées de l'adresse
            double[] coord = Utils.GetCoordByAddress(existing.getAddress());

            existing.setLatitude(coord[1]);
            existing.setLongitude(coord[0]);
        } catch (CoordinatesException e) {
            throw new RuntimeException("Error while getting coordinates : " + e.getMessage());

        }

        if (!request.getOldPassword().isEmpty() && !request.getSalesPerson().getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(request.getSalesPerson().getPassword()));
        }

        try {
            accountRepository.save(existing);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the account: " + e.getMessage());
        }
    }

    /**
     * Permet de récupérer un commercial en fonction de sa clé API
     * 
     * @param apiKey la clé API du commercial
     * @return le commercial correspondant
     * @throws RuntimeException en cas de problème avec la base de données
     */
    public Map<String, Object> getSalesPerson(String apiKey) {
        try {
            Optional<SalesPerson> salesPersonOptional = accountRepository.findByApiKey(apiKey);

            SalesPerson salesPerson = salesPersonOptional.get();

            return Map.of (
                    "firstName", salesPerson.getFirstName(),
                    "lastName", salesPerson.getLastName(),
                    "email", salesPerson.getEmail(),
                    "address", salesPerson.getAddress(),
                    "latitude", salesPerson.getLatitude(),
                    "longitude", salesPerson.getLongitude()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to get the account : " + e.getMessage());
        }
    }

    /**
     * Permet de récupérer les coordonnées d'un commercial
     *
     * @param id l'ID du commercial
     * @return les coordonnées du commercial
     * @throws IllegalArgumentException si le commercial n'existe pas
     */
    public Double[] getCoordPerson(Long id) {
        accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for ID : " + id));

        Object[] result = accountRepository.getCoordById(id);

        // Vérifier que result n'est pas null et que result[0] contient bien des valeurs
        if (result != null && result.length > 0 && result[0] instanceof Object[] coordinates) {

            // Vérification que le tableau de coordonnées contient bien la latitude et la longitude
            if (coordinates.length > 1) {
                double latitude = ((Number) coordinates[0]).doubleValue();
                double longitude = ((Number) coordinates[1]).doubleValue();
                return new Double[] {latitude, longitude};
            }
        }

        throw new IllegalStateException("Impossible de récupérer les coordonnées du commercial " + id);
    }

}
