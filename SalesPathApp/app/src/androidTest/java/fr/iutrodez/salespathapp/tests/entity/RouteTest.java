package fr.iutrodez.salespathapp.tests.entity;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;
import java.util.ArrayList;
import java.util.Date;

import fr.iutrodez.salespathapp.entity.Contact;
import fr.iutrodez.salespathapp.entity.Route;
import fr.iutrodez.salespathapp.enums.ContactCheckbox;
import fr.iutrodez.salespathapp.enums.ContactStatus;

public class RouteTest {

    private Route route;
    private Contact contact1, contact2, contact3;

    @Before
    public void setUp() {
        ArrayList<Contact> steps = new ArrayList<>();
        contact1 = new Contact("1", "Client A", "Address A", 0.0, 0.0, ContactCheckbox.NO_CHECKBOX, true, "Company A");
        contact2 = new Contact("1", "Client B", "Address B", 1.0, 1.0, ContactCheckbox.NO_CHECKBOX, false, "Company B");
        contact3 = new Contact("1", "Client C", "Address C", 1.2, 2.1, ContactCheckbox.NO_CHECKBOX, true, "Company C");

        steps.add(contact1);
        steps.add(contact2);
        steps.add(contact3);

        route = new Route("123", "Test Route", steps, new Date(), "account1");
    }

    @Test
    public void testGetCurrentStep() {
        assertEquals("L'étape actuelle doit être 0", 0, route.getCurrentStep());

        contact1.setStatus(ContactStatus.VISITED);
        assertEquals("L'étape actuelle doit être 1", 1, route.getCurrentStep());

        contact2.setStatus(ContactStatus.SKIPPED);
        assertEquals("L'étape actuelle doit être 2", 2, route.getCurrentStep());

        contact3.setStatus(ContactStatus.VISITED);
        assertEquals("Aucune étape restante, doit retourner -1", -1, route.getCurrentStep());
    }

    @Test
    public void testAddLocation() {
        GeoPoint point1 = new GeoPoint(48.8566, 2.3522);
        GeoPoint point2 = new GeoPoint(45.7640, 4.8357);

        route.addLocation(point1);
        assertEquals(1, route.getLocalisation().size());

        route.addLocation(point2);
        assertEquals(2, route.getLocalisation().size());

        route.addLocation(point2); // Ajout du même point
        assertEquals("Ne doit pas ajouter un doublon", 2, route.getLocalisation().size());
    }

    @Test
    public void testNbVisit() {
        assertEquals("1/3 visites effectuées", "0/3", route.nbVisit());

        contact1.setStatus(ContactStatus.SKIPPED);
        assertEquals("1/3 visites effectuées", "1/3", route.nbVisit());

        contact2.setStatus(ContactStatus.VISITED);
        assertEquals("2/3 visites effectuées", "2/3", route.nbVisit());

        contact3.setStatus(ContactStatus.VISITED);
        assertEquals("3/3 visites effectuées", "3/3", route.nbVisit());
    }
}
