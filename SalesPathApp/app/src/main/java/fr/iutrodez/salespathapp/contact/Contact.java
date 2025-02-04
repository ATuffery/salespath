package fr.iutrodez.salespathapp.contact;

public class Contact {
    private String name;
    private String id;
    private String address;
    private boolean isClient;
    private double latitude;
    private double longitude;
    private ContactCheckbox checkbox;
    private boolean visited;

    public Contact(String id, String name, String address, double lat, double lon, ContactCheckbox isChecked, boolean isClient) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.checkbox = isChecked;
        this.isClient = isClient;
        this.latitude = lat;
        this.longitude = lon;
        this.visited = false;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getAddress() {
        return address;
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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isClient() {
        return isClient;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
