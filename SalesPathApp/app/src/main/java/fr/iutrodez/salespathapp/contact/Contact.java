package fr.iutrodez.salespathapp.contact;

public class Contact {
    private String name;
    private String details;
    private ContactCheckbox checkbox;

    public Contact(String name, String details, ContactCheckbox isChecked) {
        this.name = name;
        this.details = details;
        this.checkbox = isChecked;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public boolean isChecked() {
        return checkbox.equals(ContactCheckbox.CHECKED);
    }

    public boolean noCheckbox () {
        return checkbox.equals(ContactCheckbox.NO_CHECKBOX);
    }

    public void setChecked(ContactCheckbox checked) {
        checkbox = checked;
    }
}
