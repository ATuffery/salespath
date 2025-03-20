package fr.iutrodez.salespathapp.enums;

public enum ContactStatus {
    VISITED, SKIPPED, UNVISITED;

    /**
     * Retour l'enum à partir de son ID
     * @param value l'ID de l'enum
     * @throw IllegalArgumentException si l'ID de l'enum n'existe pas
     * @return l'enum correspondant
     */
    public static ContactStatus fromInt(int value) {
        for (ContactStatus status : ContactStatus.values()) {
            if (status.ordinal() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
