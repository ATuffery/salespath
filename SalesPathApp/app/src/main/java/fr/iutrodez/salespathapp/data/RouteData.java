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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.contact.ContactCheckbox;
import fr.iutrodez.salespathapp.itinerary.Itinerary;
import fr.iutrodez.salespathapp.itinerary.Step;
import fr.iutrodez.salespathapp.route.Route;
import fr.iutrodez.salespathapp.utils.Utils;

public class RouteData {

    public interface OnRouteDetailsLoadedListener {
        void OnRouteDetailsLoaded(Route route);
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
                            JSONObject routeObj = response.getJSONObject("itinerary");
                            String id = routeObj.optString("id");
                            String startDateStr = routeObj.optString("startDate");
                            String accountId = routeObj.optString("itineraryId");
                            Date startDate = Utils.parseStringToDate(startDateStr, "yyyy-MM-dd'T'HH:mm:ss.SSS");

                            ArrayList<Contact> steps = new ArrayList<>();

                            JSONArray stepsArray = response.optJSONArray("steps");
                            if (stepsArray != null) {
                                for (int i = 0; i < stepsArray.length(); i++) {
                                    JSONObject stepObject = stepsArray.getJSONObject(i);
                                    String idClient = stepObject.optString("idClient", "");
                                    String clientName = stepObject.optString("clientName", "Client inconnu");
                                    String clientAddress = stepObject.optString("clientAddress", "Client inconnu");
                                    double clientLatitude = stepObject.optDouble("clientLatitude", 0);
                                    double clientLongitude = stepObject.optDouble("clientLongitude", 0);
                                    String company = stepObject.optString("companyName");
                                    boolean isClient = stepObject.optBoolean("isClient");

                                    Contact contact = new Contact(idClient, clientName, clientAddress, clientLatitude, clientLongitude, ContactCheckbox.NO_CHECKBOX, isClient, company);

                                    steps.add(contact);
                                }
                            }

                            listener.OnRouteDetailsLoaded(new Route(id, steps, startDate, accountId));

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
