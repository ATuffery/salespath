package fr.iutrodez.salespath.utils.exception;

/**
 * Exception lancée lorsqu'une erreur est détectée dans les coordonnées d'un point
 */
public class CoordinatesException extends Exception {

    /**
     * Constructeur de l'exception
     * @param message Message d'erreur
     */
    public CoordinatesException(String message) {
        super(message);
    }
}
