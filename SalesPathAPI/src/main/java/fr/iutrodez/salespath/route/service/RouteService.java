package fr.iutrodez.salespath.route.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.iutrodez.salespath.route.repository.IRouteRepository;
import fr.iutrodez.salespath.route.model.Route;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

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
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Récupère tous les parcours d'un utilisateur.
     * @param idSalesPerson L'ID de l'utilisateur.
     * @return La liste des parcours de l'utilisateur.
     * @throws RuntimeException En cas d'erreur lors de la récupération.
     */
    public ArrayList<Route> getAllRoutes(Long idSalesPerson) {
        try {
            return routeRepository.findByIdSalesPerson(idSalesPerson);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Récupère un parcours par son ID.
     * @param id L'ID du parcours.
     * @return Le parcours correspondant à l'ID.
     * @throws NoSuchElementException Si le parcours n'existe pas.
     */
    public Route getRouteById(String id) {
        return routeRepository.findById(id)
                              .orElseThrow(() -> new NoSuchElementException("Parcours non trouvé pour l'ID : " + id));
    }

}
