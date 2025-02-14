package fr.iutrodez.salespathapp.tests.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.iutrodez.salespathapp.utils.CheckInput;

public class CheckInputTest {

    @Test
    public void testText() {
        // Cas normaux
        assertTrue("Texte valide", CheckInput.text("Bonjour", 3, 10));
        assertTrue("Texte avec espaces", CheckInput.text("   Salut  ", 3, 10));

        // Cas aux limites
        assertTrue("Longueur minimale", CheckInput.text("abc", 3, 10));
        assertTrue("Longueur maximale", CheckInput.text("abcdefghij", 3, 10));
        assertFalse("Trop court", CheckInput.text("ab", 3, 10));
        assertFalse("Trop long", CheckInput.text("abcdefghijk", 3, 10));

        // Cas extrêmes
        assertFalse("Texte vide", CheckInput.text("", 1, 10));
        assertFalse("Texte uniquement espaces", CheckInput.text("    ", 1, 10));
    }

    @Test
    public void testEmail() {
        // Cas normaux
        assertTrue("Email valide", CheckInput.email("test@example.com"));
        assertTrue("Email avec chiffres", CheckInput.email("user123@gmail.com"));
        assertTrue("Email avec tirets", CheckInput.email("prenom.nom@example.fr"));

        // Cas invalides
        assertFalse("Email sans @", CheckInput.email("testexample.com"));
        assertFalse("Email sans domaine", CheckInput.email("test@.com"));
        assertFalse("Email avec espaces", CheckInput.email("test @example.com"));
        assertFalse("Email vide", CheckInput.email(""));
        assertFalse("Email sans extension", CheckInput.email("test@example"));
    }
}