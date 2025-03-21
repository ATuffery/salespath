package fr.iutrodez.salespathapi.utils.pathFinderTest;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientCoordService;
import fr.iutrodez.salespath.client.service.ClientService;
import fr.iutrodez.salespath.utils.pathFinder.Little;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LittleTest {

    @InjectMocks
    private Little little;

    @Mock
    private AccountService accountService;

    @Mock
    private ClientCoordService clientService;

    @Test
    void testOptimalPath() {
        String[] idClients = {"client1", "client2", "client3"};
        Long idUser = 1L;

        // Coordonnées simulées pour le compte utilisateur et les clients
        Double[] coordUser = {43.6047, 1.4442}; // Toulouse
        Double[] coordClient1 = {45.7640, 4.8357}; // Lyon
        Double[] coordClient2 = {48.8566, 2.3522}; // Paris
        Double[] coordClient3 = {50.6292, 3.0573}; // Lille

        // Simuler le comportement des services
        when(accountService.getCoordPerson(idUser)).thenReturn(coordUser);
        when(clientService.getCoordById("client1")).thenReturn(coordClient1);
        when(clientService.getCoordById("client2")).thenReturn(coordClient2);
        when(clientService.getCoordById("client3")).thenReturn(coordClient3);

        // Appeler l'algorithme
        List<String> result = little.algoLittle(idClients, idUser);

        // Vérifications des résultats
        assertNotNull(result);
        assertEquals(3, result.size());

        // Vérifier l'ordre des clients
        assertEquals("client1", result.get(0)); // Lyon
        assertEquals("client2", result.get(1)); // Paris
        assertEquals("client3", result.get(2)); // Lille
    }

//    @Test
//    void testWithMultipleClients() {
//        String[] idClients = {"client1", "client2", "client3", "client4", "client5", "client6", "client7", "client8"};
//        Long idUser = 1L;
//
//        // Coordonnées simulées pour le compte utilisateur et les clients
//        Double[] coordUser = {43.6047, 1.4442}; // Toulouse
//        Double[] coordClient1 = {45.7640, 4.8357}; // Lyon
//        Double[] coordClient2 = {48.8566, 2.3522}; // Paris
//        Double[] coordClient3 = {50.6292, 3.0573}; // Lille
//        Double[] coordClient4 = {43.8486, 3.0551}; // Montpellier
//        Double[] coordClient5 = {44.8378, -0.5792}; // Bordeaux
//        Double[] coordClient6 = {48.5734, 7.7521}; // Strasbourg
//        Double[] coordClient7 = {45.7640, 4.8357}; // Lyon (Réutilisation pour test)
//        Double[] coordClient8 = {47.2184, -1.5536}; // Nantes
//
//        // Simuler le comportement des services
//        when(accountService.getCoordPerson(idUser)).thenReturn(coordUser);
//        when(clientService.getCoordById("client1")).thenReturn(coordClient1);
//        when(clientService.getCoordById("client2")).thenReturn(coordClient2);
//        when(clientService.getCoordById("client3")).thenReturn(coordClient3);
//        when(clientService.getCoordById("client4")).thenReturn(coordClient4);
//        when(clientService.getCoordById("client5")).thenReturn(coordClient5);
//        when(clientService.getCoordById("client6")).thenReturn(coordClient6);
//        when(clientService.getCoordById("client7")).thenReturn(coordClient7);
//        when(clientService.getCoordById("client8")).thenReturn(coordClient8);
//
//        List<String> result = little.algoLittle(idClients, idUser);
//
//        for (String idClient : result) {
//            System.out.println(idClient);
//        }
//
//        // Vérifications des résultats
//        assertNotNull(result);
//        assertEquals(8, result.size());
//        assertTrue(result.contains("client1"));
//        assertTrue(result.contains("client2"));
//        assertTrue(result.contains("client3"));
//        assertTrue(result.contains("client4"));
//        assertTrue(result.contains("client5"));
//        assertTrue(result.contains("client6"));
//        assertTrue(result.contains("client7"));
//        assertTrue(result.contains("client8"));
//
//        // Vérification que les services ont bien été appelés
//        verify(accountService).getCoordPerson(idUser);
//        verify(clientService, times(8)).getCoordById(anyString());
//    }
}
