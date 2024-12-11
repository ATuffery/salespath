package fr.iutrodez.salespathapp.utils;

import java.util.regex.Pattern;

public class CheckInput {

    /**
     * Vérifie si un texte est valide en respectant une longueur minimale et maximale
     * @param text le texte à vérifier
     * @param minLength la longueur minimale du texte
     * @param maxLength la longueur maximale du texte
     * @return true si le texte est correct, false sinon
     */
    public static boolean text(String text, int minLength, int maxLength) {
        return text.trim().length() >= minLength && text.trim().length() <= maxLength;
    }

    /**
     * Verifie si une email est valide (en fonction de la regex)
     * @param email l'email a vérifier
     * @return true si l'email est valide
     */
    public static boolean email(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return Pattern.compile(emailRegex).matcher(email).matches();
    }

}
