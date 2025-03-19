package fr.iutrodez.salespathapp.enums;

public enum RouteStatus {

    PAUSED, FINISHED, STARTED;


    public static RouteStatus fromInt(int value) {
        for (RouteStatus status : RouteStatus.values()) {
            if (status.ordinal() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
