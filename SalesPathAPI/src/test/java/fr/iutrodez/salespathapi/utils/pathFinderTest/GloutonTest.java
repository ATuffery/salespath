package fr.iutrodez.salespathapi.utils.pathFinderTest;

import fr.iutrodez.salespath.account.service.AccountService;
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
    private ClientService clientService;

    @InjectMocks
    private Glouton glouton;

    @BeforeEach
    void setUp() {
        glouton = new Glouton();
    }

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
    void testItineraryOrder_EmptyClients() {
        String[] clientIds = {};
        Long userId = 1L;

        when(accountService.getCoordPerson(userId)).thenReturn(new Double[]{48.8566, 2.3522});

        String[] result = glouton.itineraryOrder(clientIds, userId);

        assertNotNull(result);
        assertEquals(0, result.length);
    }
}
