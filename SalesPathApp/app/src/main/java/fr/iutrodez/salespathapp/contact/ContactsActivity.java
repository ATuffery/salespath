package fr.iutrodez.salespathapp.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHorizontal);

        this.intent = getIntent();
        this.id = intent.getStringExtra("accountId");
        this.apiKey = intent.getStringExtra("apiKey");

       this. queue = Volley.newRequestQueue(this);

        List<CardWithTwoLines> clientList = getClientList();

        CardWithTwoLinesAdapteur adapter = new CardWithTwoLinesAdapteur(clientList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<CardWithTwoLines> getClientList() {
        this.queue.add(requestClientList(this.URL + this.id));
        return null;
    }

    /**
     * Créer une requête Volley pour récupérer la liste des clients d'un commercial avec l'API
     * @param url l'url de l'API
     * @return le JSON avec les informations du compte en cas de succès
     */
    private JsonObjectRequest requestClientList(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String lastName = response.getString("lastName");
                            String firstName = response.getString("firstName");

                            lastNameInput.setText(lastName);
                            firstNameInput.setText(firstName);
                            addressInput.setText(response.getString("address"));
                            emailInput.setText(response.getString("email"));

                            updateInitials(firstName, lastName);
                        } catch (JSONException e) {
                            displayServerError();
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
                            }
                        } else {
                            displayServerError();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-API-KEY", intent.getStringExtra("apiKey"));
                return headers;
            }
        };
        return jsonObjectRequest;
    }

    public void goToCreateContact(View btn) {
        Intent intent = new Intent(this, CreateContactActivity.class);

        Intent intentParent = getIntent();
        intent.putExtra("apiKey", intentParent.getStringExtra("apiKey"));
        intent.putExtra("accountId", intentParent.getStringExtra("accountId"));

        startActivity(intent);
    }

}