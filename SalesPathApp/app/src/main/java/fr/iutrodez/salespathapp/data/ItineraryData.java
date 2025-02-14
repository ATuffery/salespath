package fr.iutrodez.salespathapp.data;

import android.content.Context;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.entity.Contact;
import fr.iutrodez.salespathapp.enums.ContactCheckbox;
import fr.iutrodez.salespathapp.entity.Itinerary;

public class ItineraryData {

    public interface OnItinerariesLoadedListener {
        void OnItinerariesLoaded(ArrayList<Itinerary> itineraries);
        void onError(String errorMessage);
    }

    public interface OnItineraryDetailsLoadedListener {
        void OnItineraryDetailsLoaded(Itinerary itinerary);
        void onError(String errorMessage);
    }

    public interface OnItineraryDeletedListener {
        void onItineraryDeleted(boolean success, String message);
    }


    /**
     * Récupère tous les itinéraire d'un commercial
     * @param context le contexte de l'activité
     * @param apiKey l'API Key
     * @param accountId L'id du commercial
     * @param listener la fonction qui va se déclancher au retour des données
     */
    public static void getItineraries(Context context, String apiKey, String accountId, OnItinerariesLoadedListener listener) {
        String url = Config.API_URL + "itinerary/" + accountId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Itinerary> itineraries = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject itineraryObject = response.getJSONObject(i);
                                int idItinerary = itineraryObject.optInt("idItinerary", -1);
                                String nameItinerary = itineraryObject.optString("nameItinerary", "Nom inconnu");
                                String codeUser = itineraryObject.optString("codeUser", "");
                                String date = itineraryObject.optString("creationDate", "");
                                int nbSteps = itineraryObject.optInt("nbSteps");

                                Itinerary itinerary = new Itinerary(idItinerary, nameItinerary, codeUser, date);
                                for (int y = 0 ; y < nbSteps ; y++) {
                                    itinerary.addStep(new Contact("","","",0.0,0.0,ContactCheckbox.UNCHECKED, true, ""));
                                }
                                itineraries.add(itinerary);
                            }
                            listener.OnItinerariesLoaded(itineraries);
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
        requestQueue.add(jsonArrayRequest);
    }


    /**
     * Récupère le détails d'un itinéraire
     * @param context le contexte de l'activité
     * @param apiKey l'API Key
     * @param id L'id du commercial
     * @param listener la fonction qui va se déclancher au retour des données
     */
    public static void getItinerariesInfos(Context context, String apiKey, String id, OnItineraryDetailsLoadedListener listener) {
        String url = Config.API_URL + "itinerary/getInfos/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject itineraryObject = response.getJSONObject("itinerary");
                            JSONObject itineraryObject2 = itineraryObject.getJSONObject("itinerary");
                            int idItinerary = itineraryObject2.optInt("idItinerary", -1);
                            String nameItinerary = itineraryObject2.optString("nameItinerary", "Nom inconnu");
                            String codeUser = itineraryObject2.optString("codeUser", "");
                            String date =  itineraryObject2.optString("creationDate", "");

                            Itinerary itinerary = new Itinerary(idItinerary, nameItinerary, codeUser, date);

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

                                    itinerary.addStep(contact);
                                }
                            }

                            listener.OnItineraryDetailsLoaded(itinerary);

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
     * Supprime un itinéraire
     * @param context le contexte de l'activité
     * @param apiKey l'API Key
     * @param idItinerary L'id du commercial
     * @param listener la fonction qui va se déclancher au retour des données
     */
    public static void deleteItinerary(Context context, String apiKey, int idItinerary, OnItineraryDeletedListener listener) {
        String url = Config.API_URL + "itinerary/" + idItinerary;

        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onItineraryDeleted(true, "Itinéraire supprimé avec succès.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(context, error, listener);
                        listener.onItineraryDeleted(false, error.getMessage());
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
        requestQueue.add(deleteRequest);
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

        if (listener instanceof OnItinerariesLoadedListener) {
            ((OnItinerariesLoadedListener) listener).onError(errorMessage);
        } else if (listener instanceof OnItineraryDetailsLoadedListener) {
            ((OnItineraryDetailsLoadedListener) listener).onError(errorMessage);
        } else if (listener instanceof OnItineraryDeletedListener) {
            ((OnItineraryDeletedListener) listener).onItineraryDeleted(false, errorMessage);
        }
    }

}
