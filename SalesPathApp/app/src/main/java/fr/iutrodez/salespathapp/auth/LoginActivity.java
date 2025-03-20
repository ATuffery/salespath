package fr.iutrodez.salespathapp.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.MainActivity;
import fr.iutrodez.salespathapp.R;

public class LoginActivity extends AppCompatActivity {

    private final static String URL = Config.API_URL + "account/login";
    public EditText loginEntry;
    public EditText passwordEntry;
    public TextView errorMsg;
    private SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.myPreferences = getSharedPreferences("me.xml", Activity.MODE_PRIVATE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loginEntry = findViewById(R.id.email);
        passwordEntry = findViewById(R.id.password);
        errorMsg = findViewById(R.id.error);
    }

    /**
     * Clic sur le bouton de connexion
     * @param btn
     */
    public void connect(View btn) {
        checkLogin(btn);
    }

    /**
     * Affiche un message d'erreur
     */
    public void displayServerError() {
        this.errorMsg.setText(R.string.error_server);
    }

    /**
     * Teste la connexion via l'API
     * @param button
     */
    public void checkLogin(View button) {
        String login = loginEntry.getText().toString();
        String password = passwordEntry.getText().toString();

        if (login.trim().isEmpty() || password.trim().isEmpty()) {
            errorMsg.post(new Runnable() {
                public void run() {
                    errorMsg.setText(getString(R.string.error_loginEmpty));
                }
            });
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(requestAuth(this.URL + "?email=" + login + "&password=" + password));
    }

    private JsonObjectRequest requestAuth(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SharedPreferences.Editor editor = myPreferences.edit();
                            editor.putString("apiKey", response.getString("apiKey"));
                            editor.putString("accountId", response.getString("id"));
                            editor.apply();
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
                            if (statusCode == 404) {
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

    /**
     * Redirection vers la page d'acceuil
     */
    public void goToHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Clic sur le bouton de création d'un compte
     * @param button bouton de création d'un compte
     */
    public void goToCreateAccount(View button) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
}
