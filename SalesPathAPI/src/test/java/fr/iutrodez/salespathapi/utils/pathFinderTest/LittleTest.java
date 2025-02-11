package fr.iutrodez.salespathapi.utils.pathFinderTest;

import fr.iutrodez.salespath.account.service.AccountService;
import fr.iutrodez.salespath.client.service.ClientService;
import fr.iutrodez.salespath.utils.pathFinder.Little;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LittleTest {

    private Little little;
    private AccountService accountService;
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        // Mock des services
        accountService = mock(AccountService.class);
        clientService = mock(ClientService.class);

        // Initialisation de la classe Little avec les services mockés
        little = new Little();
        little.accountService = accountService;
        little.clientService = clientService;
    }

    @Test
    void test() {
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

        List<String> result = little.algoLittle(idClients, idUser);
    }

//    @Test
//    void testAlgoLittle() {
//        // Données simulées pour le test
//        String[] idClients = {"client1", "client2", "client3"};
//        Long idUser = 1L;
//
//        // Coordonnées simulées pour le compte utilisateur et les clients
//        Double[] coordUser = {43.6047, 1.4442}; // Toulouse
//        Double[] coordClient1 = {45.7640, 4.8357}; // Lyon
//        Double[] coordClient2 = {48.8566, 2.3522}; // Paris
//        Double[] coordClient3 = {50.6292, 3.0573}; // Lille
//
//        // Simuler le comportement des services
//        when(accountService.getCoordPerson(idUser)).thenReturn(coordUser);
//        when(clientService.getCoordById("client1")).thenReturn(coordClient1);
//        when(clientService.getCoordById("client2")).thenReturn(coordClient2);
//        when(clientService.getCoordById("client3")).thenReturn(coordClient3);
//
//        // Exécution de l'algorithme
//        List<String> result = little.algoLittle(idClients, idUser);
//
//        // Vérifications des résultats
//        assertNotNull(result, "La liste des clients ordonnés ne doit pas être nulle.");
//        assertEquals(3, result.size(), "La liste des clients doit contenir 3 éléments.");
//
//        // Vérification de l'ordre des clients (exemple basé sur un ordre attendu)
//        assertTrue(List.of("client1", "client2", "client3").containsAll(result),
//                "La liste des clients doit contenir tous les clients initiaux.");
//
//        // Vérification que les services ont bien été appelés
//        verify(accountService).getCoordPerson(idUser);
//        verify(clientService, times(3)).getCoordById(anyString());
//    }
//
//    @Test
//    void testAlgoLittleWithNoClients() {
//        // Cas où il n'y a pas de clients
//        String[] idClients = {};
//        Long idUser = 1L;
//
//        // Coordonnées simulées pour l'utilisateur
//        Double[] coordUser = {43.6047, 1.4442}; // Toulouse
//        when(accountService.getCoordPerson(idUser)).thenReturn(coordUser);
//
//        // Vérification que l'algorithme lève une exception ou retourne une liste vide
//        assertThrows(IllegalArgumentException.class, () -> little.algoLittle(idClients, idUser),
//                "Une exception doit être levée pour une liste de clients vide.");
//    }
}
