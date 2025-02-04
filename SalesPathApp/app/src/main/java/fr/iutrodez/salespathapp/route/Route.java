package fr.iutrodez.salespathapp.route;

import java.util.ArrayList;
import java.util.Date;

import fr.iutrodez.salespathapp.contact.Contact;

public class Route {

    private ArrayList<Contact> steps;
    private int currentStep;
    private ArrayList<GeoPoint> localisation;
    private boolean routePause;
    private Date startDate;
    private String itineraryId;

    public Route(String itineraryId, ArrayList<Contact> steps) {
        this.steps = steps;
        this.itineraryId = itineraryId;
        this.currentStep = 0;
        this.localisation = new ArrayList<>();
        this.routePause = false;
        this.startDate = new Date();
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
        this.localisation.add(localisation);
    }

    public boolean isRoutePause() {
        return routePause;
    }

    public void setRoutePause(boolean routePause) {
        this.routePause = routePause;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getItineraryId() {
        return itineraryId;
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
        return (this.getCurrentStep() + 1) + "/" + this.getSteps().size();
    }

}
