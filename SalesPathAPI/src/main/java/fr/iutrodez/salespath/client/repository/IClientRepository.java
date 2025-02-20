package fr.iutrodez.salespath.client.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import fr.iutrodez.salespath.client.model.Client;
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
}
