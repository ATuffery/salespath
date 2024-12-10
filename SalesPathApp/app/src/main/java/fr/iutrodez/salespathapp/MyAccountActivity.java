package fr.iutrodez.salespathapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyAccountActivity extends BaseActivity {

    private EditText lastNameInput;
    private EditText firstNameInput;
    private EditText addressInput;
    private EditText emailInput;

    private static final String URL = "http://ec2-13-39-14-30.eu-west-3.compute.amazonaws.com:8080/account/";

    private Intent intent;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        lastNameInput = findViewById(R.id.lastName);
        firstNameInput = findViewById(R.id.firstName);
        addressInput = findViewById(R.id.address);
        emailInput = findViewById(R.id.email);

        intent = getIntent();
        queue = Volley.newRequestQueue(this);

        initialDisplayUpdate();
    }

    public void initialDisplayUpdate() {
        queue.add(requestInfo(this.URL + "infos"));
    }

    public JsonObjectRequest requestInfo(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            lastNameInput.setText(response.getString("lastName"));
                            firstNameInput.setText(response.getString("firstName"));
                            addressInput.setText(response.getString("address"));
                            emailInput.setText(response.getString("email"));
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

    public void displayServerError() {
        Toast.makeText(getBaseContext(), R.string.error_server, Toast.LENGTH_LONG).show();
    }

    public void updateAccount(View button) {
        String lastName = lastNameInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();

        if (!CheckInput.text(lastName, 1, 50) ||
                !CheckInput.text(firstName, 1, 50) ||
                !CheckInput.text(address, 1, 150) ||
                !CheckInput.email(email)) {

            Toast.makeText(getBaseContext(), R.string.typing_error, Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstName", firstName);
            jsonBody.put("lastName", lastName);
            jsonBody.put("address", address);
            jsonBody.put("email", email);
        } catch (JSONException e) {
            Toast.makeText(getBaseContext(), R.string.error_server, Toast.LENGTH_LONG).show();
        }

        queue.add(requestUpdate(this.URL + "update/" + intent.getStringExtra("accountId"),
                                jsonBody));
    }

    private JsonObjectRequest requestUpdate(String url, JSONObject body) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getBaseContext(), R.string.success_account_update,
                                       Toast.LENGTH_LONG).show();
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
                                Toast.makeText(getBaseContext(), R.string.error_server,
                                               Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), R.string.error_server,
                                           Toast.LENGTH_LONG).show();
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
}