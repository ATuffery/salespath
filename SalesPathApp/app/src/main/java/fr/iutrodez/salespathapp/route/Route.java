package fr.iutrodez.salespathapp.route;

import java.util.ArrayList;
import java.util.Date;
import org.osmdroid.util.GeoPoint;

import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.contact.ContactStatus;
import fr.iutrodez.salespathapp.utils.Utils;

public class Route {

    private String routeId;
    private ArrayList<Contact> steps;
    private ArrayList<GeoPoint> localisation;
    private RouteStatus status;
    private Date startDate;
    private String accountId;
    private String name;

    public Route(String routeId, String name, ArrayList<Contact> steps, Date startDate, String accountId) {
        this.routeId = routeId;
        this.steps = steps;
        this.name = name;
        this.localisation = new ArrayList<>();
        this.status = RouteStatus.STARTED;
        this.startDate = startDate;
        this.accountId = accountId;
    }

    public ArrayList<Contact> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Contact> steps) {
        this.steps = steps;
    }

    public int getCurrentStep() {
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getStatus() == ContactStatus.UNVISITED) {
                return i;
            }
        }
        return -1; // Retourne -1 si aucun client n'est à l'état UNVISITED
    }

    public ArrayList<GeoPoint> getLocalisation() {
        return localisation;
    }

    public void addLocation(GeoPoint localisation) {
        if (this.localisation.isEmpty() || !Utils.isSameLocation(this.localisation.get(this.localisation.size() - 1), localisation)) {
            this.localisation.add(localisation);
        }
    }

    public RouteStatus getStatus() {
        return status;
    }

    public void setStatus(RouteStatus status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }


    public Contact getCurrentContact() { return this.steps.get(this.getCurrentStep()); }

    /**
     * Retourne une chaine de caractère avec le nombre de
     * visite effectuée sur le nombre de visite total
     * @return le nombre de visite effectuée sur le nombre de visite total
     */
    public String nbVisit() {
        return (this.getCurrentStep() == -1 ? this.getSteps().size() : this.getCurrentStep())  + "/" + this.getSteps().size();
    }

    public String getRouteId() {
        return routeId;
    }

    public String getDateCreation() {
        return Utils.formatDateFr(startDate);
    }

    public void resetLocalisation() { this.localisation.clear(); }
}
