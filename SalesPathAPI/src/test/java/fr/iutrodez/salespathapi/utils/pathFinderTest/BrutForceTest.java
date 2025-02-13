package fr.iutrodez.salespathapi.utils.pathFinderTest;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientService;
import fr.iutrodez.salespath.utils.pathFinder.BrutForce;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrutForceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private BrutForce brutForce;

    @Test
    void testBrutForce_ValidInput() {
        String[] clientIds = {"C1", "C2", "C3"};
        Long userId = 1L;

        when(accountService.getCoordPerson(userId)).thenReturn(new Double[]{48.8566, 2.3522});
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945});
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376});
        when(clientService.getCoordById("C3")).thenReturn(new Double[]{48.8738, 2.2950});

        String[] result = brutForce.brutForce(clientIds, userId);

        assertNotNull(result);
        assertEquals(clientIds.length, result.length);
        assertTrue(Arrays.asList(clientIds).containsAll(Arrays.asList(result)));
    }

    @Test
    void testBrutForce_EightClients() {
        String[] idClients = {"client1", "client2", "client3", "client4", "client5", "client6", "client7", "client8"};
        Long idUser = 1L;

        // Coordonnées simulées pour le compte utilisateur et les clients
        Double[] coordUser = {43.6047, 1.4442}; // Toulouse
        Double[] coordClient1 = {45.7640, 4.8357}; // Lyon
        Double[] coordClient2 = {48.8566, 2.3522}; // Paris
        Double[] coordClient3 = {50.6292, 3.0573}; // Lille
        Double[] coordClient4 = {43.8486, 3.0551}; // Montpellier
        Double[] coordClient5 = {44.8378, -0.5792}; // Bordeaux
        Double[] coordClient6 = {48.5734, 7.7521}; // Strasbourg
        Double[] coordClient7 = {45.7640, 4.8357}; // Lyon (Réutilisation pour test)
        Double[] coordClient8 = {47.2184, -1.5536}; // Nantes

        // Simuler le comportement des services
        when(accountService.getCoordPerson(idUser)).thenReturn(coordUser);
        when(clientService.getCoordById("client1")).thenReturn(coordClient1);
        when(clientService.getCoordById("client2")).thenReturn(coordClient2);
        when(clientService.getCoordById("client3")).thenReturn(coordClient3);
        when(clientService.getCoordById("client4")).thenReturn(coordClient4);
        when(clientService.getCoordById("client5")).thenReturn(coordClient5);
        when(clientService.getCoordById("client6")).thenReturn(coordClient6);
        when(clientService.getCoordById("client7")).thenReturn(coordClient7);
        when(clientService.getCoordById("client8")).thenReturn(coordClient8);

        String[] result = brutForce.brutForce(idClients, idUser);

        for (String idClient : result) {
            System.out.println(idClient);
        }

        assertNotNull(result);
        assertEquals(idClients.length, result.length);
        assertTrue(Arrays.asList(idClients).containsAll(Arrays.asList(result)));
    }

    @Test
    void testBrutForce_InvalidInput() {
        String[] clientIds = {};
        Long userId = 1L;

        when(accountService.getCoordPerson(anyLong())).thenReturn(new Double[]{48.8566, 2.3522});

        assertThrows(IllegalArgumentException.class, () -> brutForce.brutForce(clientIds, userId));
    }

    @Test
    void testBrutForce_InvalidUserId() {
        String[] clientIds = {"C1", "C2", "C3"};
        Long userId = null;

        when(accountService.getCoordPerson(null)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> brutForce.brutForce(clientIds, userId));
    }
}
