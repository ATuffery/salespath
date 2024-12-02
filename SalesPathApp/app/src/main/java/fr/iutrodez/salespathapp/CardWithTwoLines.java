package fr.iutrodez.salespathapp;

public class CardWithTwoLines {
    private String title;
    private String status;
    private String line1;
    private String line2;

    public CardWithTwoLines(String title, String status, String line1, String line2) {
        this.title = title;
        this.status = status;
        this.line1 = line1;
        this.line2 = line2;
    }

    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getLine1() { return line1; }
    public String getLine2() { return line2; }
}

