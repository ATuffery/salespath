package fr.iutrodez.salespathapp.route;

import java.util.ArrayList;
import java.util.Date;
import org.osmdroid.util.GeoPoint;

import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.utils.Utils;

public class Route {

    private String routeId;
    private ArrayList<Contact> steps;
    private int currentStep;
    private ArrayList<GeoPoint> localisation;
    private RouteStatus status;
    private Date startDate;
    private String accountId;

    public Route(String routeId, ArrayList<Contact> steps, Date startDate, String accountId) {
        this.routeId = routeId;
        this.steps = steps;
        this.currentStep = 0;
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
        return currentStep;
    }

    /**
     * Défini l'index de l'étape courante
     * @param currentStep index de l'étape courante
     * @return true si l'index est correct, false sinon
     */
    public boolean setCurrentStep(int currentStep) {
        if (currentStep <= this.steps.size() - 1) {
            this.currentStep = currentStep;
            return true;
        }
        return false;
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


    public Contact getCurrentContact() {
        return this.steps.get(this.currentStep);
    }

    public boolean nextStep() {
        return this.setCurrentStep(this.getCurrentStep() + 1);
    }

    /**
     * Retourne une chaine de caractère avec le nombre de
     * visite effectuée sur le nombre de visite total
     * @return le nombre de visite effectuée sur le nombre de visite total
     */
    public String nbVisit() {
        return this.getCurrentStep() + 1 + "/" + this.getSteps().size();
    }

    public String getRouteId() {
        return routeId;
    }

    public String getDateCreation() {
        return Utils.formatDateFr(startDate);
    }
}
