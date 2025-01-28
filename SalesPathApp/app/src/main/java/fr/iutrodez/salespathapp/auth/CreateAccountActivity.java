package fr.iutrodez.salespathapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.utils.CheckInput;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText lastNameInput;
    private EditText firstNameInput;
    private EditText addressInput;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView msgError;

    private final static String URL = Config.API_URL + "account/add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);

        this.lastNameInput = findViewById(R.id.lastName);
        this.firstNameInput = findViewById(R.id.firstName);
        this.addressInput = findViewById(R.id.address);
        this.emailInput = findViewById(R.id.email);
        this.passwordInput = findViewById(R.id.password);
        this.msgError = findViewById(R.id.msgError);
    }

    public void goToLogin(View button) {
        goToLogin();
    }

    public void goToLogin() {
        finish();
    }

    public void createAccount(View button) {
        String lastName = lastNameInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!CheckInput.text(lastName, 1, 50) ||
            !CheckInput.text(firstName, 1, 50) ||
            !CheckInput.text(address, 1, 150) ||
            !CheckInput.email(email) ||
            !CheckInput.text(password, 8, 50)) {

            msgError.setText(getString(R.string.typing_error));
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("lastName", lastName);
            jsonBody.put("firstName", firstName);
            jsonBody.put("address", address);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            msgError.setText(getString(R.string.error_server));
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(requestCreation(this.URL, jsonBody));
    }

    private JsonObjectRequest requestCreation(String url, JSONObject body) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        goToLogin();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 400) {
                                msgError.setText(getString(R.string.error_emailAlreadyUsed));
                            } else {
                                msgError.setText(getString(R.string.error_server));
                            }
                        } else {
                            msgError.setText(getString(R.string.error_server));
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

}
