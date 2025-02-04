package fr.iutrodez.salespath.route.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.iutrodez.salespath.route.repository.IRouteRepository;
import fr.iutrodez.salespath.route.model.Route;

/**
 * Service pour gérer les opérations relatives aux parcours.
 */
@Service
public class RouteService {

    @Autowired
    private IRouteRepository routeRepository;

    /**
     * Crée un nouveau parcours et l'enregistre dans la base de données.
     *
     * @param route L'objet Route à créer.
     * @throws RuntimeException En cas d'erreur lors de la création.
     */
    public void createRoute(Route route) {
        try {
            routeRepository.save(route);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création d'un nouveau parcours : " + e.getMessage());
        }
    }
}
