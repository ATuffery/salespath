package fr.iutrodez.salespathapp.tests.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.iutrodez.salespathapp.utils.Utils;

public class UtilsTest {

    @Test
    public void testFormatDateFr() {
        Date date = new Date(0); // 1er janvier 1970
        String expected = new SimpleDateFormat("EEEE d MMMM yyyy à HH:mm:ss", Locale.FRANCE).format(date);
        assertEquals(expected, Utils.formatDateFr(date));
    }

    @Test
    public void testParseStringToDate() {
        String dateStr = "01/01/2024";
        String pattern = "dd/MM/yyyy";
        Date parsedDate = Utils.parseStringToDate(dateStr, pattern);
        assertNotNull(parsedDate);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.FRANCE);
        assertEquals(dateStr, sdf.format(parsedDate));
    }

    @Test
    public void testIsSameLocation() {
        GeoPoint pointA = new GeoPoint(48.8566, 2.3522); // Paris
        GeoPoint pointB = new GeoPoint(48.8566, 2.3522); // Paris identique
        GeoPoint pointC = new GeoPoint(40.7128, -74.0060); // New York

        assertTrue(Utils.isSameLocation(pointA, pointB));
        assertFalse(Utils.isSameLocation(pointA, pointC));
    }
}