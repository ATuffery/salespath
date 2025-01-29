package fr.iutrodez.salespath.itinerary.repository;

import fr.iutrodez.salespath.itinerary.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IItineraryRepository extends JpaRepository<Itinerary, Long> {

    @Query("SELECT i FROM Itinerary i WHERE i.codeUser = :id")
    Optional<Itinerary[]> findByIdUser(Long id);
}
