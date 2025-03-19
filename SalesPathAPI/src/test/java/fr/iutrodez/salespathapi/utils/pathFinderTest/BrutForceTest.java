package fr.iutrodez.salespathapi.utils.pathFinderTest;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientCoordService;
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
    private ClientCoordService clientService;

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

    @Test
    void testBrutForce_ThreeClients() {
        String[] idClients3 = {"client1", "client2", "client3"};
        Long idUser = 1L;

        Double[] coordUser = {43.6047, 1.4442}; // Toulouse
        Double[] coordClient1 = {45.7640, 4.8357}; // Lyon
        Double[] coordClient2 = {48.8566, 2.3522}; // Paris
        Double[] coordClient3 = {50.6292, 3.0573}; // Lille

        when(accountService.getCoordPerson(idUser)).thenReturn(coordUser);
        when(clientService.getCoordById("client1")).thenReturn(coordClient1);
        when(clientService.getCoordById("client2")).thenReturn(coordClient2);
        when(clientService.getCoordById("client3")).thenReturn(coordClient3);

        String[] result3 = brutForce.brutForce(idClients3, idUser);

        System.out.println("Résultat pour 3 clients : ");
        for (String idClient : result3) {
            System.out.println(idClient);
        }

        assertNotNull(result3);
        assertEquals(idClients3.length, result3.length);
        assertTrue(Arrays.asList(idClients3).containsAll(Arrays.asList(result3)));
    }

    @Test
    void testBrutForce_TenClients() {
        String[] idClients10 = {
                "client1", "client2", "client3", "client4", "client5", "client6", "client7", "client8", "client9", "client10"
        };
        Long idUser = 1L;

        Double[] coordUser = {43.6047, 1.4442}; // Toulouse
        Double[] coordClient1 = {45.7640, 4.8357}; // Lyon
        Double[] coordClient2 = {48.8566, 2.3522}; // Paris
        Double[] coordClient3 = {50.6292, 3.0573}; // Lille
        Double[] coordClient4 = {43.8486, 3.0551}; // Montpellier
        Double[] coordClient5 = {44.8378, -0.5792}; // Bordeaux
        Double[] coordClient6 = {48.5734, 7.7521}; // Strasbourg
        Double[] coordClient7 = {45.7640, 4.8357}; // Lyon (Réutilisation pour test)
        Double[] coordClient8 = {47.2184, -1.5536}; // Nantes
        Double[] coordClient9 = {48.0, 2.0}; // Nantes
        Double[] coordClient10 = {48.1351, 11.5820}; // Munich

        when(accountService.getCoordPerson(idUser)).thenReturn(coordUser);
        when(clientService.getCoordById("client1")).thenReturn(coordClient1);
        when(clientService.getCoordById("client2")).thenReturn(coordClient2);
        when(clientService.getCoordById("client3")).thenReturn(coordClient3);
        when(clientService.getCoordById("client4")).thenReturn(coordClient4);
        when(clientService.getCoordById("client5")).thenReturn(coordClient5);
        when(clientService.getCoordById("client6")).thenReturn(coordClient6);
        when(clientService.getCoordById("client7")).thenReturn(coordClient7);
        when(clientService.getCoordById("client8")).thenReturn(coordClient8);
        when(clientService.getCoordById("client9")).thenReturn(coordClient9);
        when(clientService.getCoordById("client10")).thenReturn(coordClient10);

        String[] result10 = brutForce.brutForce(idClients10, idUser);

        System.out.println("Résultat pour 10 clients : ");
        for (String idClient : result10) {
            System.out.println(idClient);
        }

        assertNotNull(result10);
        assertEquals(idClients10.length, result10.length);
        assertTrue(Arrays.asList(idClients10).containsAll(Arrays.asList(result10)));
    }

// Car prend trop de temps

//    @Test
//    void testBrutForce_FifteenClients() {
//        String[] idClients15 = {
//                "client1", "client2", "client3", "client4", "client5", "client6", "client7", "client8", "client9", "client10",
//                "client11", "client12", "client13", "client14", "client15"
//        };
//        Long idUser = 1L;
//
//        Double[] coordUser = {43.6047, 1.4442}; // Toulouse
//        Double[] coordClient1 = {45.7640, 4.8357}; // Lyon
//        Double[] coordClient2 = {48.8566, 2.3522}; // Paris
//        Double[] coordClient3 = {50.6292, 3.0573}; // Lille
//        Double[] coordClient4 = {43.8486, 3.0551}; // Montpellier
//        Double[] coordClient5 = {44.8378, -0.5792}; // Bordeaux
//        Double[] coordClient6 = {48.5734, 7.7521}; // Strasbourg
//        Double[] coordClient7 = {45.7640, 4.8357}; // Lyon (Réutilisation pour test)
//        Double[] coordClient8 = {47.2184, -1.5536}; // Nantes
//        Double[] coordClient9 = {48.0, 2.0}; // Nantes
//        Double[] coordClient10 = {48.1351, 11.5820}; // Munich
//        Double[] coordClient11 = {49.2827, -123.1207}; // Vancouver
//        Double[] coordClient12 = {51.5074, -0.1278}; // Londres
//        Double[] coordClient13 = {40.7128, -74.0060}; // New York
//        Double[] coordClient14 = {34.0522, -118.2437}; // Los Angeles
//        Double[] coordClient15 = {37.7749, -122.4194}; // San Francisco
//
//        when(accountService.getCoordPerson(idUser)).thenReturn(coordUser);
//        when(clientService.getCoordById("client1")).thenReturn(coordClient1);
//        when(clientService.getCoordById("client2")).thenReturn(coordClient2);
//        when(clientService.getCoordById("client3")).thenReturn(coordClient3);
//        when(clientService.getCoordById("client4")).thenReturn(coordClient4);
//        when(clientService.getCoordById("client5")).thenReturn(coordClient5);
//        when(clientService.getCoordById("client6")).thenReturn(coordClient6);
//        when(clientService.getCoordById("client7")).thenReturn(coordClient7);
//        when(clientService.getCoordById("client8")).thenReturn(coordClient8);
//        when(clientService.getCoordById("client9")).thenReturn(coordClient9);
//        when(clientService.getCoordById("client10")).thenReturn(coordClient10);
//        when(clientService.getCoordById("client11")).thenReturn(coordClient11);
//        when(clientService.getCoordById("client12")).thenReturn(coordClient12);
//        when(clientService.getCoordById("client13")).thenReturn(coordClient13);
//        when(clientService.getCoordById("client14")).thenReturn(coordClient14);
//        when(clientService.getCoordById("client15")).thenReturn(coordClient15);
//
//        String[] result15 = brutForce.brutForce(idClients15, idUser);
//
//        System.out.println("Résultat pour 15 clients : ");
//        for (String idClient : result15) {
//            System.out.println(idClient);
//        }
//
//        assertNotNull(result15);
//        assertEquals(idClients15.length, result15.length);
//        assertTrue(Arrays.asList(idClients15).containsAll(Arrays.asList(result15)));
//    }
}
