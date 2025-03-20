package fr.iutrodez.salespathapi.utils.pathFinderTest;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientCoordService;
import fr.iutrodez.salespath.client.service.ClientService;
import fr.iutrodez.salespath.utils.pathFinder.Glouton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GloutonTest {

    @Mock
    private AccountService accountService;

    @Mock
    private ClientCoordService clientService;

    @InjectMocks
    private Glouton glouton;

    @Test
    void testItineraryOrder_ValidInput() {
        String[] clientIds = {"C1", "C2", "C3"};
        Long userId = 1L;

        Double[] userCoords = {48.8566, 2.3522};
        when(accountService.getCoordPerson(userId)).thenReturn(userCoords);
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945});
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376});
        when(clientService.getCoordById("C3")).thenReturn(new Double[]{48.8738, 2.2950});

        String[] result = glouton.itineraryOrder(clientIds, userId);

        assertNotNull(result);
        assertEquals(clientIds.length, result.length);
        assertTrue(Arrays.asList(clientIds).containsAll(Arrays.asList(result)));
    }

    @Test
    void testItineraryOrder_SingleClient() {
        String[] clientIds = {"C1"};
        Long userId = 1L;

        when(accountService.getCoordPerson(userId)).thenReturn(new Double[]{48.8566, 2.3522});
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945});

        String[] result = glouton.itineraryOrder(clientIds, userId);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("C1", result[0]);
    }

    @Test
    void testItineraryOrder_NullUserCoordinates() {
        String[] clientIds = {"C1", "C2"};
        Long userId = 1L;

        when(accountService.getCoordPerson(userId)).thenReturn(null);
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945});
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376});

        assertThrows(NullPointerException.class, () -> glouton.itineraryOrder(clientIds, userId));
    }

    @Test
    void testItineraryOrder_NullClientCoordinates() {
        String[] clientIds = {"C1", "C2"};
        Long userId = 1L;

        when(accountService.getCoordPerson(userId)).thenReturn(new Double[]{48.8566, 2.3522});
        when(clientService.getCoordById("C1")).thenReturn(null);
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376});

        assertThrows(NullPointerException.class, () -> glouton.itineraryOrder(clientIds, userId));
    }

    @Test
    void testItineraryOrder_EightClients() {
        String[] clientIds = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8"};
        Long userId = 1L;

        // Coordonnées simulées pour l'utilisateur
        Double[] userCoords = {48.8566, 2.3522}; // Paris
        when(accountService.getCoordPerson(userId)).thenReturn(userCoords);

        // Coordonnées simulées pour les clients
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945}); // Tour Eiffel
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376}); // Louvre
        when(clientService.getCoordById("C3")).thenReturn(new Double[]{48.8738, 2.2950}); // Arc de Triomphe
        when(clientService.getCoordById("C4")).thenReturn(new Double[]{48.8462, 2.3524}); // Panthéon
        when(clientService.getCoordById("C5")).thenReturn(new Double[]{48.8339, 2.3266}); // Montparnasse
        when(clientService.getCoordById("C6")).thenReturn(new Double[]{48.8519, 2.3773}); // Bastille
        when(clientService.getCoordById("C7")).thenReturn(new Double[]{48.8799, 2.3530}); // Montmartre
        when(clientService.getCoordById("C8")).thenReturn(new Double[]{48.8667, 2.3125}); // Champs-Élysées

        // Exécution de l'algorithme
        String[] result = glouton.itineraryOrder(clientIds, userId);

        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être nul.");
        assertEquals(clientIds.length, result.length, "Le nombre de clients dans la tournée doit être correct.");
        assertTrue(Arrays.asList(clientIds).containsAll(Arrays.asList(result)), "Tous les clients doivent être présents dans le résultat.");
    }

    @Test
    void testItineraryOrder_ThreeClients() {
        String[] clientIds = {"C1", "C2", "C3"};
        Long userId = 1L;

        // Coordonnées simulées pour l'utilisateur
        Double[] userCoords = {48.8566, 2.3522}; // Paris
        when(accountService.getCoordPerson(userId)).thenReturn(userCoords);

        // Coordonnées simulées pour les clients
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945}); // Tour Eiffel
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376}); // Louvre
        when(clientService.getCoordById("C3")).thenReturn(new Double[]{48.8738, 2.2950}); // Arc de Triomphe

        // Exécution de l'algorithme
        String[] result = glouton.itineraryOrder(clientIds, userId);

        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être nul.");
        assertEquals(clientIds.length, result.length, "Le nombre de clients dans la tournée doit être correct.");
        assertTrue(Arrays.asList(clientIds).containsAll(Arrays.asList(result)), "Tous les clients doivent être présents dans le résultat.");
    }

    @Test
    void testItineraryOrder_TenClients() {
        String[] clientIds = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10"};
        Long userId = 1L;

        // Coordonnées simulées pour l'utilisateur
        Double[] userCoords = {48.8566, 2.3522}; // Paris
        when(accountService.getCoordPerson(userId)).thenReturn(userCoords);

        // Coordonnées simulées pour les clients
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945}); // Tour Eiffel
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376}); // Louvre
        when(clientService.getCoordById("C3")).thenReturn(new Double[]{48.8738, 2.2950}); // Arc de Triomphe
        when(clientService.getCoordById("C4")).thenReturn(new Double[]{48.8462, 2.3524}); // Panthéon
        when(clientService.getCoordById("C5")).thenReturn(new Double[]{48.8339, 2.3266}); // Montparnasse
        when(clientService.getCoordById("C6")).thenReturn(new Double[]{48.8519, 2.3773}); // Bastille
        when(clientService.getCoordById("C7")).thenReturn(new Double[]{48.8799, 2.3530}); // Montmartre
        when(clientService.getCoordById("C8")).thenReturn(new Double[]{48.8667, 2.3125}); // Champs-Élysées
        when(clientService.getCoordById("C9")).thenReturn(new Double[]{49.1, 3.5});
        when(clientService.getCoordById("C10")).thenReturn(new Double[]{47.89, 2.99});

        // Exécution de l'algorithme
        String[] result = glouton.itineraryOrder(clientIds, userId);

        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être nul.");
        assertEquals(clientIds.length, result.length, "Le nombre de clients dans la tournée doit être correct.");
        assertTrue(Arrays.asList(clientIds).containsAll(Arrays.asList(result)), "Tous les clients doivent être présents dans le résultat.");
    }

    @Test
    void testItineraryOrder_FiveTeenClients() {
        String[] clientIds = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11", "C12", "C13",
                              "C14", "C15"};
        Long userId = 1L;

        // Coordonnées simulées pour l'utilisateur
        Double[] userCoords = {48.8566, 2.3522}; // Paris
        when(accountService.getCoordPerson(userId)).thenReturn(userCoords);

        // Coordonnées simulées pour les clients
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945}); // Tour Eiffel
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376}); // Louvre
        when(clientService.getCoordById("C3")).thenReturn(new Double[]{48.8738, 2.2950}); // Arc de Triomphe
        when(clientService.getCoordById("C4")).thenReturn(new Double[]{48.8462, 2.3524}); // Panthéon
        when(clientService.getCoordById("C5")).thenReturn(new Double[]{48.8339, 2.3266}); // Montparnasse
        when(clientService.getCoordById("C6")).thenReturn(new Double[]{48.8519, 2.3773}); // Bastille
        when(clientService.getCoordById("C7")).thenReturn(new Double[]{48.8799, 2.3530}); // Montmartre
        when(clientService.getCoordById("C8")).thenReturn(new Double[]{48.8667, 2.3125}); // Champs-Élysées
        when(clientService.getCoordById("C9")).thenReturn(new Double[]{49.1, 3.5});
        when(clientService.getCoordById("C10")).thenReturn(new Double[]{47.89, 3.4});
        when(clientService.getCoordById("C11")).thenReturn(new Double[]{49.675, 2.8652});
        when(clientService.getCoordById("C12")).thenReturn(new Double[]{47.8715, 2.99});
        when(clientService.getCoordById("C13")).thenReturn(new Double[]{46.975, 3.86});
        when(clientService.getCoordById("C14")).thenReturn(new Double[]{45.8353, 2.135});
        when(clientService.getCoordById("C15")).thenReturn(new Double[]{49.7253, 3.866});

        // Exécution de l'algorithme
        String[] result = glouton.itineraryOrder(clientIds, userId);

        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être nul.");
        assertEquals(clientIds.length, result.length, "Le nombre de clients dans la tournée doit être correct.");
        assertTrue(Arrays.asList(clientIds).containsAll(Arrays.asList(result)), "Tous les clients doivent être présents dans le résultat.");
    }

}
