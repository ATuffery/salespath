package fr.iutrodez.salespath.itinerarystep.repository;

import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IItineraryStepRepository extends JpaRepository<ItineraryStep, String> {

    @Query("SELECT i FROM ItineraryStep i WHERE i.idItinerary = ?1 AND i.idClient = ?2")
    Optional<ItineraryStep> findByIds(String idItinerary, String idClient);

    @Query("SELECT i FROM ItineraryStep i WHERE i.idItinerary = ?1")
    Optional<ItineraryStep[]> findByIdItinerary(String idItinerary);

    @Modifying
    @Query("DELETE FROM ItineraryStep i WHERE i.idItinerary = :idItinerary")
    void deleteByIdItinerary(@Param("idItinerary") String idItinerary);
}
