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
        String[] clientIds = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8"};
        Long userId = 1L;

        when(accountService.getCoordPerson(userId)).thenReturn(new Double[]{48.8566, 2.3522});
        when(clientService.getCoordById("C1")).thenReturn(new Double[]{48.8584, 2.2945});
        when(clientService.getCoordById("C2")).thenReturn(new Double[]{48.8606, 2.3376});
        when(clientService.getCoordById("C3")).thenReturn(new Double[]{48.8738, 2.2950});
        when(clientService.getCoordById("C4")).thenReturn(new Double[]{48.8462, 2.3524});
        when(clientService.getCoordById("C5")).thenReturn(new Double[]{48.8339, 2.3266});
        when(clientService.getCoordById("C6")).thenReturn(new Double[]{48.8519, 2.3773});
        when(clientService.getCoordById("C7")).thenReturn(new Double[]{48.8799, 2.3530});
        when(clientService.getCoordById("C8")).thenReturn(new Double[]{48.8667, 2.3125});

        String[] result = brutForce.brutForce(clientIds, userId);

        assertNotNull(result);
        assertEquals(clientIds.length, result.length);
        assertTrue(Arrays.asList(clientIds).containsAll(Arrays.asList(result)));
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
