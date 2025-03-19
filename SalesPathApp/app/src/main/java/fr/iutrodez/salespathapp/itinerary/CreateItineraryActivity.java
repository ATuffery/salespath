package fr.iutrodez.salespathapp.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.Map;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.entity.Contact;
import fr.iutrodez.salespathapp.contact.ContactAdapter;
import fr.iutrodez.salespathapp.enums.ContactCheckbox;
import fr.iutrodez.salespathapp.data.ContactData;
import fr.iutrodez.salespathapp.utils.CheckInput;
import fr.iutrodez.salespathapp.utils.Utils;

public class CreateItineraryActivity extends BaseActivity {

    private RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    private ArrayList<Contact> contactList;
    private String urlAdd = Config.API_URL + "itinerary";
    private EditText nameInput;
    private TextView infoSaisie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_itinerary);
        this.nameInput = findViewById(R.id.name);
        this.infoSaisie = findViewById(R.id.infoSaisie);
        this.infoSaisie.setText("1 requis, " + Config.MAX_ITINERARY_STEP + " max.");

        rvContacts = findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        fillContacts();

        contactAdapter = new ContactAdapter(contactList);
        rvContacts.setAdapter(contactAdapter);
    }

    private void fillContacts() {
        contactList = new ArrayList<>();

        ContactData.getContacts(getBaseContext(), getApiKey(), getAccountId(), new ContactData.OnContactsLoadedListener() {
            @Override
            public void onContactsLoaded(ArrayList<JSONObject> contacts) {
                for (JSONObject jsonContact : contacts) {
                    try {
                        String name = jsonContact.getString("firstName") + " " + jsonContact.getString("lastName");
                        String address = jsonContact.getString("enterpriseName") + " - " + jsonContact.getString("address");

                        Contact contact = new Contact(jsonContact.getString("id"), name, address, 0, 0, ContactCheckbox.UNCHECKED, jsonContact.getBoolean("client"), "");
                        contactList.add(contact);

                    } catch (JSONException e) {
                        Utils.displayToast(getBaseContext(), "Erreur lors de la lecture des données.");
                    }
                }

                ContactAdapter adapter = new ContactAdapter(contactList);
                rvContacts.setAdapter(adapter);
                rvContacts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayToast(getBaseContext(), errorMessage);
            }
        });
    }

    /**
     * Lance le processus d'ajout d'un itinéraire
     * @param btn button cliqué
     */
    public void createItinerary(View btn) {
        ArrayList<String> contacts = new ArrayList<>();
        for (int i = 0 ; i < contactList.size() ; i++) {
            Contact current = contactList.get(i);
            if (current.isChecked()) {
                contacts.add(current.getId());
            }
        }

        String name = Utils.inputValueFormatted(nameInput);

        if (!CheckInput.text(name, 1, 50) ||
            contacts.size() == 0 || contacts.size() > Config.MAX_ITINERARY_STEP) {
            Utils.displayToast(getBaseContext(), getString(R.string.typing_error));
            return;
        }

        JSONObject jsonBody = new JSONObject();
        JSONObject itinerary = new JSONObject();
        try {
            JSONArray contactsSteps = new JSONArray(contacts.toArray());
            itinerary.put("codeUser", getAccountId());
            itinerary.put("nbSteps", contacts.size());
            itinerary.put("nameItinerary", name);
            jsonBody.put("itinerary", itinerary);
            jsonBody.put("idClients", contactsSteps);
        } catch (JSONException e) {
            Utils.displayToast(getBaseContext(), getString(R.string.error_server));
        }

        Log.e("DATA", jsonBody.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(requestCreation(this.urlAdd, jsonBody));
    }

    /** Envoie de la requete de création à l'API */
    private JsonObjectRequest requestCreation(String url, JSONObject body) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        goToItineraries();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.displayToast(getBaseContext(), getString(R.string.error_server));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-API-KEY", getApiKey());
                Log.e("APIKEY", getApiKey());
                return headers;
            }
        };
        return jsonObjectRequest;
    }

    private void goToItineraries() {
        Intent intent = new Intent(this, ItinerariesActivity.class);
        startActivity(intent);
    }

}

