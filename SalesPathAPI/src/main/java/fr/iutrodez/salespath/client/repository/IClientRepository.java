package fr.iutrodez.salespath.client.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import fr.iutrodez.salespath.client.model.Client;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repository pour gérer les opérations CRUD liées aux clients.
 */
public interface IClientRepository extends MongoRepository<Client, String> {

    /**
     * Recherche les clients associés à une personne spécifique en utilisant son ID.
     * @param idPerson L'ID de la personne.
     * @return Une liste de clients associés à la personne donnée.
     */
    List<Client> findByIdPerson(Long idPerson);

    /**
     * Recherche un client spécifique via son ID unique.
     *
     * @param id L'ID unique du client.
     * @return L'objet Client correspondant ou null si non trouvé.
     */
    Optional<Client> findById(String id);

    /**
     * Supprime un client de la base de données en utilisant son ID unique.
     * @param id L'ID unique du client à supprimer.
     */
    void deleteClientById(String id);

    /**
     * Recherche les prospects qui sont à moins de 1 km de la position du user
     * @param latitude
     * @param longitude
     * @return la liste des prospects
     */
    @Query("{ coordinates: { $nearSphere: { $geometry: { type: 'Point', coordinates: [?0, ?1] }, $maxDistance: 1000 } }, isClient: false, idPerson : ?2 }")
    List<Client> findProspectsWithin1Km(double longitude, double latitude, Long id);

    /**
     * Recherche si le client est a moins de 200m
     * @param latitude
     * @param longitude
     * @return
     */
    @Query("{ coordinates: { $nearSphere: { $geometry: { type: 'Point', coordinates: [?0, ?1] }, $maxDistance: 200 } }, isClient: true, idPerson : ?2 }")
    List<Client> findClientsWithin200m(double longitude, double latitude, Long id);
}
