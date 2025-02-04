package fr.iutrodez.salespath.route.repository;

import fr.iutrodez.salespath.route.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de repository pour gérer les opérations CRUD liées aux parcours.
 */
@Repository
public interface IRouteRepository extends MongoRepository<Route, String> {
}
