package fr.iutrodez.salespath.route.repository;

import fr.iutrodez.salespath.client.model.Client;
import fr.iutrodez.salespath.route.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Interface de repository pour gérer les opérations CRUD liées aux parcours.
 */
@Repository
public interface IRouteRepository extends MongoRepository<Route, String> {

    /**
     * Recherche les parcours associés à un commercial spécifique en utilisant son ID.
     * @param idSalesPerson L'ID du commercial.
     * @return La liste de parcours associés au commercial donné.
     */
    ArrayList<Route> findByIdSalesPerson(Long idSalesPerson);
}
