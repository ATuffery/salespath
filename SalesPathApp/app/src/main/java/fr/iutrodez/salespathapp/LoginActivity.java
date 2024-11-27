package fr.iutrodez.salespathapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private final static String URL = "";

    public EditText loginEntry;
    public EditText passwordEntry;
    public TextView errorMsg;

    public static String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEntry = findViewById(R.id.email);
        passwordEntry = findViewById(R.id.password);
        errorMsg = findViewById(R.id.error);
    }


    public void connect(View btn) {
        Thread req = new Thread(() -> {
            checkLogin(btn);
        });
        req.start();
    }

    public void displayServerError() {
        this.errorMsg.setText(R.string.error_server);
    }

    public void checkLogin(View button) {
        String login = loginEntry.getText().toString();
        String password = passwordEntry.getText().toString();

        if (login.isEmpty() || password.isEmpty()) {
            errorMsg.setText(R.string.error_login);
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(requestAuth(this.URL + login + "/" + password));
    }

    private JsonObjectRequest requestAuth(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            apiKey = response.getString("apiKey");
                            goToHomePage();
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
                            if (statusCode == 401) {
                                errorMsg.setText(getString(R.string.error_login));
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
                return headers;
            }
        };
        return jsonObjectRequest;
    }

    public void goToHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        // on passe l'apiKey dans l'intent
        intent.putExtra("apiKey", apiKey);
        startActivity(intent);
    }
}
