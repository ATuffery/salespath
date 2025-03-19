package fr.iutrodez.salespathapp.enums;

public enum ContactStatus {
    VISITED, SKIPPED, UNVISITED;

    public static ContactStatus fromInt(int value) {
        for (ContactStatus status : ContactStatus.values()) {
            if (status.ordinal() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
