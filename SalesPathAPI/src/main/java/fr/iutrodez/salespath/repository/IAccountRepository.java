package fr.iutrodez.salespath.repository;

import fr.iutrodez.salespath.model.SalesPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<SalesPerson, Long> {
    /**
     * Permet de récupérer l'email d'un utilisateur en fonction de son email
     * @param email l'email de l'utilisateur
     * @return l'email de l'utilisateur si l'utilisateur existe
     */
    @Query("SELECT s FROM SalesPerson s  WHERE s.email = :email")
    Optional<SalesPerson> findByEmail(String email);

    /**
     * Permet de vérifier si une clé API existe en base de données
     * @param apiKey la clé API à vérifier
     * @return la clé API si elle existe
     */
    @Query("SELECT apiKey FROM SalesPerson WHERE apiKey = :apiKey")
    Optional<String> existsByApiKey(String apiKey);
}
