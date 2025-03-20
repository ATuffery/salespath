package fr.iutrodez.salespathapp.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;

public class ContactData {

    /** Interface pour notifier quand les contacts sont chargés */
    public interface OnContactsLoadedListener {
        void onContactsLoaded(ArrayList<JSONObject> contacts);
        void onError(String errorMessage);
    }

    /**
     * Récupère tous les contacts d'un commercial
     * @param context le contexte de l'activité
     * @param apiKey l'API Key
     * @param id L'id du commercial
     * @param listener la fonction qui va se déclancher au retour des données
     */
    public static void getContacts(Context context, String apiKey, String id, OnContactsLoadedListener listener) {
        String url = Config.API_URL + "client/" + id;

        // Création de la requête réseau
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<JSONObject> contacts = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                contacts.add(response.getJSONObject(i));
                            }
                            // Notifier que les contacts sont chargés
                            listener.onContactsLoaded(contacts);
                        } catch (JSONException e) {
                            listener.onError("Une erreur s'est produite lors de la lecture des données.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                        listener.onError(errorMessage);
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

        // Ajout de la requête à la file de requêtes
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Récupère tous les contacts/propects à proximiter
     * @param context le contexte de l'activité
     * @param apiKey l'API Key
     * @param id L'id du commercial
     * @param localisation la localisation du commercial
     * @param listener la fonction qui va se déclancher au retour des données
     */
    public static void getProximityClients(Context context, String apiKey, String id, GeoPoint localisation, OnContactsLoadedListener listener) {
        String url = Config.API_URL + "client/proximity?latitude=" + localisation.getLatitude() + "&longitude=" + localisation.getLongitude() + "&id=" + id;

        // Création de la requête réseau
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            ArrayList<JSONObject> contacts = new ArrayList<>();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    contacts.add(response.getJSONObject(i));
                                }
                                // Notifier que les contacts sont chargés
                                listener.onContactsLoaded(contacts);
                            } catch (JSONException e) {
                                listener.onError("Une erreur s'est produite lors de la lecture des données.");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                        listener.onError(errorMessage);
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

        // Ajout de la requête à la file de requêtes
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }
}
