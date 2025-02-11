package fr.iutrodez.salespathapp.contact;

import org.osmdroid.util.GeoPoint;

public class Contact {
    private String name;
    private String id;
    private String address;
    private String company;
    private boolean isClient;
    private GeoPoint coord;
    private ContactCheckbox checkbox;
    private ContactStatus visited;

    public Contact(String id, String name, String address, double lat, double lon, ContactCheckbox isChecked, boolean isClient, String company) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.checkbox = isChecked;
        this.isClient = isClient;
        this.coord = new GeoPoint(lat, lon);
        this.visited = ContactStatus.UNVISITED;
        this.company = company;
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

    public ContactStatus getStatus() {
        return visited;
    }

    public void setStatus(ContactStatus visited) {
        this.visited = visited;
    }

    public boolean isClient() {
        return isClient;
    }

    public double getLatitude() {
        return this.coord.getLatitude();
    }

    public double getLongitude() {
        return this.coord.getLongitude();
    }
    public GeoPoint getCoord() { return this.coord; }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
