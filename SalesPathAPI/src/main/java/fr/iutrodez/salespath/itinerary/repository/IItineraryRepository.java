package fr.iutrodez.salespath.itinerary.repository;

import fr.iutrodez.salespath.itinerary.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IItineraryRepository extends JpaRepository<Itinerary, Long> {

    /**
     * Permet de récupérer un itinéraire en fonction de son nom
     * @param id l'identifiant de l'itinéraire
     * @return l'itinéraire si il existe
     */
    @Query("SELECT i FROM Itinerary i WHERE i.codeUser = :id")
    Optional<Itinerary[]> findByIdUser(Long id);

    /**
     * Permet de vérifier si un itinéraire existe en fonction de son nom
     * @param name le nom de l'itinéraire
     * @return le nom s'il existe
     */
    @Query("SELECT i.nameItinerary FROM Itinerary i WHERE i.nameItinerary = :name AND i.codeUser = :id")
    Optional<String> existsByNameAndId(String name, String id);
}
