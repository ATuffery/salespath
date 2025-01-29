package fr.iutrodez.salespathapp.contact;

public class Contact {
    private String name;
    private String id;
    private String details;
    private ContactCheckbox checkbox;

    public Contact(String id, String name, String details, ContactCheckbox isChecked) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.checkbox = isChecked;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
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
