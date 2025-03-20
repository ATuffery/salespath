package fr.iutrodez.salespathapp.enums;

public enum RouteStatus {

    PAUSED, FINISHED, STARTED;

    /**
     * Retour l'enum à partir de son ID
     * @param value l'ID de l'enum
     * @throw IllegalArgumentException si l'ID de l'enum n'existe pas
     * @return l'enum correspondant
     */
    public static RouteStatus fromInt(int value) {
        for (RouteStatus status : RouteStatus.values()) {
            if (status.ordinal() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
