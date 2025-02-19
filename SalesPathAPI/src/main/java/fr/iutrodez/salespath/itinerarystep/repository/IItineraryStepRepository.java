package fr.iutrodez.salespath.itinerarystep.repository;

import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IItineraryStepRepository extends JpaRepository<ItineraryStep, String> {

    /**
     * Récupère une étape d'un itinéraire par l'id de l'itinéraire et l'id du client
     * @param idItinerary L'identifiant de l'itinéraire
     * @param idClient L'identifiant du client
     * @return L'étape de l'itinéraire
     */
    @Query("SELECT i FROM ItineraryStep i WHERE i.idItinerary = ?1 AND i.idClient = ?2")
    Optional<ItineraryStep> findByIds(String idItinerary, String idClient);

    /**
     * Récupère les étapes d'un itinéraire par ordre croissant
     * @param idItinerary L'identifiant de l'itinéraire
     * @return Les étapes de l'itinéraire
     */
    @Query("SELECT i FROM ItineraryStep i WHERE i.idItinerary = ?1 ORDER BY i.step ASC")
    Optional<ItineraryStep[]> findByIdItinerary(String idItinerary);

    /**
     * Supprime les étapes d'un itinéraire
     * @param idItinerary L'identifiant de l'itinéraire
     */
    @Modifying
    @Query("DELETE FROM ItineraryStep i WHERE i.idItinerary = :idItinerary")
    void deleteByIdItinerary(@Param("idItinerary") String idItinerary);

    /**
     * Vérifie si un client est utilisé dans une étape d'un itinéraire
     * @param idClient L'identifiant du client
     * @return Vrai si le client est utilisé, Faux sinon
     */
    @Query("SELECT COUNT(i) > 0 FROM ItineraryStep i WHERE i.idClient = :idClient")
    boolean existsByIdClient(@Param("idClient") String idClient);

    /**
     * Récupére les id des itinéraires qui possèdent le client en étape
     * @param idClient L'identifiant du client
     * @return Les id des itinéraires
     */
    @Query("SELECT i.idItinerary FROM ItineraryStep i WHERE i.idClient = :idClient")
    List<String> findByIdClient(@Param("idClient") String idClient);
}
