package fr.iutrodez.salespathapp.contact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.card.CardWithTwoLines;
import fr.iutrodez.salespathapp.card.CardWithTwoLinesAdapteur;

public class ContactsActivity extends BaseActivity {

    private RequestQueue queue;
    private Intent intent;
    private static final String URL = "http://ec2-13-39-14-30.eu-west-3.compute.amazonaws.com:8080/client/get?id=";
    private String id;
    private String apiKey;

    private ArrayList<CardWithTwoLines> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        this.intent = getIntent();
        this.id = intent.getStringExtra("accountId");
        this.apiKey = intent.getStringExtra("apiKey");

        this. queue = Volley.newRequestQueue(this);

        getClientList();
    }

    private void getClientList() {
        this.queue.add(requestClientList(this.URL + this.id));
    }

    public void displayServerError() {
        Toast.makeText(this, R.string.error_server, Toast.LENGTH_LONG).show();
    }

    /**
     * Créer une requête Volley pour récupérer la liste des clients d'un commercial avec l'API
     * @param url l'url de l'API
     * @return le JSON avec les informations du compte en cas de succès
     */
    private JsonArrayRequest requestClientList(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("data", "taille array " + response.length());
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject data = response.getJSONObject(i);

                                contacts.add(new CardWithTwoLines(
                                        data.getString("firstName") + " " + data.getString("lastName"),
                                        data.getBoolean("client") ? "Client" : "Prospect",
                                        data.getString("address"),
                                        data.getString("enterpriseName"),
                                        "Modifier",
                                        () -> {
                                            Intent intent = new Intent(ContactsActivity.this, UpdateContactActivity.class);

                                            Intent intentParent = getIntent();
                                            intent.putExtra("apiKey", intentParent.getStringExtra("apiKey"));
                                            intent.putExtra("accountId", intentParent.getStringExtra("accountId"));
                                            try {
                                                Log.e("id", data.getString("id"));
                                                intent.putExtra("contactId", data.getString("id"));
                                            } catch (JSONException e) {
                                                Log.e("JSONEXC", "JSONEXC");
                                                throw new RuntimeException(e);
                                            }

                                            startActivity(intent);
                                        }
                                ));
                            }
                            CardWithTwoLinesAdapteur adapter = new CardWithTwoLinesAdapteur(contacts);

                            RecyclerView recyclerView = findViewById(R.id.recyclerViewHorizontal);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));

                        } catch (JSONException e) {
                            displayServerError();
                            Log.e("EE", "EE");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                Toast.makeText(getBaseContext(), R.string.error_find_account,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                displayServerError();
                                Log.e("AA", "AA");
                            }
                        } else {
                            displayServerError();
                            Log.e("CC", "CC2");
                        }
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
        return jsonArrayRequest;
    }

    public void goToCreateContact(View btn) {
        Intent intent = new Intent(this, CreateContactActivity.class);

        Intent intentParent = getIntent();
        intent.putExtra("apiKey", intentParent.getStringExtra("apiKey"));
        intent.putExtra("accountId", intentParent.getStringExtra("accountId"));

        startActivity(intent);
    }

}