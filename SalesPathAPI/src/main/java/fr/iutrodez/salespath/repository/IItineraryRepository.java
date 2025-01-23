package fr.iutrodez.salespath.repository;

import fr.iutrodez.salespath.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IItineraryRepository extends JpaRepository<Itinerary, Long> {

    @Query("SELECT i FROM Itinerary i WHERE i.id = :id")
    Optional<Itinerary> findByIdUser(String id);
}
