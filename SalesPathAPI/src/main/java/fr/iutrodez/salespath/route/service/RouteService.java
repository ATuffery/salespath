package fr.iutrodez.salespath.route.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.iutrodez.salespath.route.repository.IRouteRepository;
import fr.iutrodez.salespath.route.model.Route;

import java.util.ArrayList;

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

    /**
     * Récupère tous les parcours d'un utilisateur.
     * @param idSalesPerson L'ID de l'utilisateur.
     * @return La liste des parcours de l'utilisateur.
     */
    public ArrayList<Route> getAllRoutes(Long idSalesPerson) {
        try {
            return routeRepository.findByIdSalesPerson(idSalesPerson);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des parcours d'un utilisateur: " + e.getMessage());
        }
    }
}
