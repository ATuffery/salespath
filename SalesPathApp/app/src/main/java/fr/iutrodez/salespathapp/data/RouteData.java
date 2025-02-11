package fr.iutrodez.salespathapp.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.contact.ContactCheckbox;
import fr.iutrodez.salespathapp.contact.ContactStatus;
import fr.iutrodez.salespathapp.itinerary.Itinerary;
import fr.iutrodez.salespathapp.itinerary.Step;
import fr.iutrodez.salespathapp.route.Route;
import fr.iutrodez.salespathapp.route.RouteStatus;
import fr.iutrodez.salespathapp.utils.Utils;

public class RouteData {

    public interface OnRouteDetailsLoadedListener {
        void OnRouteDetailsLoaded(Route route);
        void onError(String errorMessage);
    }

    public interface OnRouteCreatedListener {
        void onRouteCreated(String routeId);
        void onError(String errorMessage);
    }

    public interface OnRouteUpdatedListener {
        void onRouteUpdated();
        void onError(String errorMessage);
    }


    /**
     * Récupère le détails d'un parcours
     * @param context le contexte de l'activité
     * @param apiKey l'API Key
     * @param id L'id du parcours
     * @param listener la fonction qui va se déclancher au retour des données
     */
    public static void getRouteInfos(Context context, String apiKey, String id, OnRouteDetailsLoadedListener listener) {
        String url = Config.API_URL + "route/getOne/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String id = response.optString("id");
                            String name = response.optString("itineraryName");
                            String startDateStr = response.optString("startDate");
                            String accountId = response.optString("itineraryId");
                            Date startDate = Utils.parseStringToDate(startDateStr, "yyyy-MM-dd'T'HH:mm:ss.SSS");

                            ArrayList<Contact> steps = new ArrayList<>();

                            JSONArray stepsArray = response.optJSONArray("steps");
                            if (stepsArray != null) {
                                for (int i = 0; i < stepsArray.length(); i++) {
                                    JSONObject stepObject = stepsArray.getJSONObject(i);
                                    JSONObject clientObject = stepObject.getJSONObject("client");
                                    String idClient = clientObject.optString("id", "");
                                    String clientName = clientObject.optString("firstName", "Client inconnu");
                                    clientName += clientObject.optString("lastName", "Client inconnu");
                                    String clientAddress = clientObject.optString("address", "Client inconnu");
                                    JSONArray coord = clientObject.getJSONArray("coordonates");
                                    double clientLatitude = coord.getDouble(0);
                                    double clientLongitude = coord.getDouble(1);
                                    String company = clientObject.optString("enterpriseName");
                                    boolean isClient = clientObject.optBoolean("client");
                                    ContactStatus status = ContactStatus.valueOf(stepObject.optString("status"));

                                    Contact contact = new Contact(idClient, clientName, clientAddress, clientLatitude, clientLongitude, ContactCheckbox.NO_CHECKBOX, isClient, company);
                                    contact.setStatus(status);

                                    steps.add(contact);
                                }
                            }

                            listener.OnRouteDetailsLoaded(new Route(id, name, steps, startDate, accountId));

                        } catch (JSONException e) {
                            listener.onError("Erreur lors de la lecture des données.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(context, error, listener);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-API-KEY", apiKey);
                return headers;
            }
        };

        // Ajouter la requête à la file d'attente Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Requete permetant de créer un parcours à partir d'un itinéraire donnée et pour un commercial
     * particulier
     * @param context le contexte de l'activité
     * @param apiKey l'api key
     * @param accountId le numéro de compte du commercial
     * @param itineraryId le numéro de l'itinéraire
     * @param listener évènement notifiant d'une erreur ou du success
     */
    public static void createRoute(Context context, String apiKey, String accountId, String itineraryId, OnRouteCreatedListener listener) {
        String url = Config.API_URL + "route?idSalesPerson=" + accountId + "&idItinerary=" + itineraryId;

        JSONObject postData = new JSONObject();
        try {
            postData.put("status", RouteStatus.STARTED);
        } catch (JSONException e) {
            listener.onError("Erreur lors de la création du JSON.");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String routeId = response.getString("id");
                            listener.onRouteCreated(routeId);
                        } catch (JSONException e) {
                            listener.onError("Erreur lors de la lecture de la réponse.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(context, error, listener);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-API-KEY", apiKey);
                return headers;
            }
        };

        // Ajouter la requête à la file d'attente Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public static void updateRoute(Context context, String apiKey, Route route, OnRouteUpdatedListener listener) {
        String url = Config.API_URL + "route";

        JSONObject routeData = new JSONObject();
        try {
            routeData.put("id", route.getRouteId());

            if (route.getStatus() == RouteStatus.FINISHED) {
                routeData.put("endDate", new Date());
            }

            routeData.put("status", route.getStatus().ordinal());

            // Création du tableau des étapes
            JSONArray stepsArray = new JSONArray();
            for (Contact step : route.getSteps()) {
                JSONObject stepObject = new JSONObject();
                JSONObject clientObject = new JSONObject();
                clientObject.put("id", step.getId());
                stepObject.put("client", clientObject);
                stepObject.put("status", step.getStatus().ordinal());
                stepsArray.put(stepObject);
            }
            routeData.put("steps", stepsArray);

            // Création du tableau de localisation
            JSONArray localisationArray = new JSONArray();
            for (GeoPoint point : route.getLocalisation()) {
                JSONObject pointObject = new JSONObject();
                pointObject.put("latitude", point.getLatitude());
                pointObject.put("longitude", point.getLongitude());
                localisationArray.put(pointObject);
            }
            routeData.put("localisation", localisationArray);

        } catch (JSONException e) {
            listener.onError("Erreur lors de la création du JSON.");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, routeData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRouteUpdated();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(context, error, listener);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-API-KEY", apiKey);
                return headers;
            }
        };

        // Ajouter la requête à la file d'attente Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }


    /** Gestion des erreurs des requêtes */
    private static void handleError(Context context, VolleyError error, Object listener) {
        String errorMessage;
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            if (statusCode == 404) {
                errorMessage = context.getString(R.string.error_find_account);
            } else {
                errorMessage = "Erreur serveur: " + statusCode;
            }
        } else {
            errorMessage = "Une erreur réseau est survenue.";
        }
        Log.e("Erreur", errorMessage);

        if (listener instanceof OnRouteDetailsLoadedListener) {
            ((OnRouteDetailsLoadedListener) listener).onError(errorMessage);
        }
    }

}
