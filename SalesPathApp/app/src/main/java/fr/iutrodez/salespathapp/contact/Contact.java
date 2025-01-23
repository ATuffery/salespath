package fr.iutrodez.salespathapp.contact;

public class Contact {
    private String name;
    private String details;
    private boolean isChecked;

    public Contact(String name, String details, boolean isChecked) {
        this.name = name;
        this.details = details;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
