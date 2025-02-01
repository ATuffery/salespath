package fr.iutrodez.salespathapp.card;

public class CardWithTwoLines {
    private String title;
    private String status;
    private String line1;
    private String line2;
    private String btnText;
    private Runnable onClickAction;

    public CardWithTwoLines(String title, String status, String line1, String line2, String btnText, Runnable onClickAction) {
        this.title = title;
        this.status = status;
        this.line1 = line1;
        this.line2 = line2;
        this.btnText = btnText;
        this.onClickAction = onClickAction;
    }

    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getLine1() { return line1; }
    public String getLine2() { return line2; }
    public String getBtnText() { return btnText; }
    public Runnable getOnClickAction() { return onClickAction; }
}
