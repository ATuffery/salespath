package fr.iutrodez.salespath.utils.exception;

/**
 * Exception lancée lorsqu'un utilisateur tente de s'inscrire avec un mot de passe et une confirmation de
 * mot de passe différentes
 */
public class DifferentPasswordException extends Exception {

    /**
     * Constructeur de l'exception
     * @param message Message d'erreur
     */
    public DifferentPasswordException(String message) {
        super(message);
    }
}
