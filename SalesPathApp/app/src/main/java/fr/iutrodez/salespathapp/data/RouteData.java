package fr.iutrodez.salespathapp.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import fr.iutrodez.salespathapp.entity.Contact;
import fr.iutrodez.salespathapp.enums.ContactCheckbox;
import fr.iutrodez.salespathapp.enums.ContactStatus;
import fr.iutrodez.salespathapp.entity.Route;
import fr.iutrodez.salespathapp.enums.RouteStatus;
import fr.iutrodez.salespathapp.utils.Utils;

public class RouteData {

    public interface OnRouteDetailsLoadedListener {
        void OnRouteDetailsLoaded(Route route);
        void onError(String errorMessage);
    }

    public interface OnRouteListLoadedListener {
        void OnRouteListLoaded(ArrayList<Route> routes);
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

    public interface OnRouteDeletedListener {
        void onRouteDeleted();
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
                            String accountId = response.optString("idSalesPerson");
                            Date startDate = Utils.parseStringToDate(startDateStr, "yyyy-MM-dd'T'HH:mm:ss.SSS");

                            ArrayList<Contact> steps = new ArrayList<>();

                            JSONArray stepsArray = response.optJSONArray("steps");
                            if (stepsArray != null) {
                                for (int i = 0; i < stepsArray.length(); i++) {
                                    JSONObject stepObject = stepsArray.getJSONObject(i);
                                    JSONObject clientObject = stepObject.getJSONObject("client");
                                    String idClient = clientObject.optString("id", "");
                                    String clientName = clientObject.optString("firstName", "Client inconnu");
                                    clientName += " " + clientObject.optString("lastName", "Client inconnu");
                                    String clientAddress = clientObject.optString("address", "Client inconnu");
                                    JSONArray coord = clientObject.getJSONArray("coordonates");
                                    double clientLatitude = coord.getDouble(1);
                                    double clientLongitude = coord.getDouble(0);
                                    String company = clientObject.optString("enterpriseName");
                                    boolean isClient = clientObject.optBoolean("client");
                                    ContactStatus status = ContactStatus.valueOf(stepObject.optString("status"));

                                    Contact contact = new Contact(idClient, clientName, clientAddress, clientLatitude, clientLongitude, ContactCheckbox.NO_CHECKBOX, isClient, company);
                                    contact.setStatus(status);

                                    steps.add(contact);
                                }
                            }
                            Route route = new Route(id, name, steps, startDate, accountId);

                            JSONArray loc = response.optJSONArray("localisation");

                            if (loc != null) {
                                for (int y = 0; y < loc.length(); y++) {
                                    JSONObject obj = (JSONObject) loc.get(y);
                                    double clientLatitude = obj.getDouble("latitude");
                                    double clientLongitude = obj.getDouble("longitude");
                                    route.addLocation(new GeoPoint(clientLatitude, clientLongitude));
                                }
                            }

                            listener.OnRouteDetailsLoaded(route);

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

        // Ajoute la requête à la file d'attente Volley
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

        // Ajoute la requête à la file d'attente Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Requete permetant de mettre à jour un parcours
     * @param context le contexte de l'activité
     * @param apiKey l'api key
     * @param route les infos sur le parcours
     * @param listener évènement notifiant d'une erreur ou du success
     */
    public static void updateRoute(Context context, String apiKey, Route route, OnRouteUpdatedListener listener) {
        String url = Config.API_URL + "route";

        JSONObject routeData = new JSONObject();
        try {
            routeData.put("id", route.getRouteId());

            if (route.getStatus() == RouteStatus.FINISHED) {
                routeData.put("endDate", Utils.formatDateToISOString(new Date()));
            }

            routeData.put("status", route.getStatus().name());

            // Création du tableau des étapes
            JSONArray stepsArray = new JSONArray();
            for (Contact step : route.getSteps()) {
                JSONObject stepObject = new JSONObject();
                JSONObject clientObject = new JSONObject();
                clientObject.put("id", step.getId());
                stepObject.put("client", clientObject);
                stepObject.put("status", step.getStatus().name());
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

        // Ajoute la requête à la file d'attente Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Requete permetant recupérer la liste des tournée d'un compte
     * @param baseContext le contexte de l'activité
     * @param apiKey l'api key
     * @param accountId le numéro de compte du commercial
     * @param OnRouteListLoadedListener évènement notifiant d'une erreur ou du success
     */
    public static void getRoutesForAccount(Context baseContext, String apiKey, String accountId, OnRouteListLoadedListener OnRouteListLoadedListener) {
        String url = Config.API_URL + "route/" + accountId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Route> routes = new ArrayList<>();

                        try {
                            for (int j = 0; j < response.length(); j++) { // Boucle sur toutes les routes
                                JSONObject routeObject = response.getJSONObject(j);

                                String id = routeObject.optString("id");
                                String name = routeObject.optString("itineraryName");
                                String startDateStr = routeObject.optString("startDate");
                                String accountId = routeObject.optString("idSalesPerson");
                                RouteStatus routeStatus = RouteStatus.fromInt(routeObject.optInt("status"));
                                Date startDate = Utils.parseStringToDate(startDateStr, "yyyy-MM-dd'T'HH:mm:ss.SSS");

                                ArrayList<Contact> steps = new ArrayList<>();

                                JSONArray stepsArray = routeObject.optJSONArray("steps");
                                if (stepsArray != null) {
                                    for (int i = 0; i < stepsArray.length(); i++) {
                                        JSONObject stepObject = stepsArray.getJSONObject(i);
                                        JSONObject clientObject = stepObject.getJSONObject("client");

                                        String idClient = clientObject.optString("id", "");
                                        String clientName = clientObject.optString("firstName", "Client inconnu") + " " +
                                                clientObject.optString("lastName", "Client inconnu");
                                        String clientAddress = clientObject.optString("address", "Client inconnu");

                                        JSONArray coord = clientObject.getJSONArray("coordonates");
                                        double clientLatitude = coord.getDouble(1);
                                        double clientLongitude = coord.getDouble(0);

                                        String company = clientObject.optString("enterpriseName");
                                        boolean isClient = clientObject.optBoolean("client");
                                        ContactStatus status = ContactStatus.fromInt(stepObject.optInt("status"));

                                        Contact contact = new Contact(idClient, clientName, clientAddress, clientLatitude, clientLongitude, ContactCheckbox.NO_CHECKBOX, isClient, company);
                                        contact.setStatus(status);
                                        steps.add(contact);
                                    }
                                }

                                Route route = new Route(id, name, steps, startDate, accountId);
                                route.setStatus(routeStatus);
                                routes.add(route);
                            }
                        } catch (Exception e) {
                            OnRouteListLoadedListener.onError("Erreur lors de la création du JSON : " + e.getMessage());
                            return;
                        }

                        // Envoi de la liste des routes via le callback
                        OnRouteListLoadedListener.OnRouteListLoaded(routes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(baseContext, error, OnRouteListLoadedListener);
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

        // Ajoute la requête à la file d'attente Volley
        RequestQueue requestQueue = Volley.newRequestQueue(baseContext);
        requestQueue.add(jsonArrayRequest);
    }


    /**
     * Supprime une tournée à partir de son ID.
     * @param context le contexte de l'activité
     * @param apiKey l'API Key
     * @param routeId L'ID de la tournée à supprimer
     * @param listener la fonction qui va se déclencher au retour des données
     */
    public static void deleteRoute(Context context, String apiKey, String routeId, OnRouteDeletedListener listener) {
        String url = Config.API_URL + "route/" + routeId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.displayToast(context, "Tournée supprimée avec succès !");
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

        // Ajoute la requête à la file d'attente Volley
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

        if (listener instanceof OnRouteDetailsLoadedListener) {
            ((OnRouteDetailsLoadedListener) listener).onError(errorMessage);
        }
    }

}
