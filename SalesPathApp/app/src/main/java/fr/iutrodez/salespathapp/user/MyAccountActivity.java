package fr.iutrodez.salespathapp.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.utils.CheckInput;
import fr.iutrodez.salespathapp.utils.Utils;

/**
 * Permet de gérer la page de d'information d'un compte d'un commercial
 */
public class MyAccountActivity extends BaseActivity {

    private EditText lastNameInput;
    private EditText firstNameInput;
    private EditText addressInput;
    private EditText emailInput;
    private EditText oldPasswordInput;
    private EditText newPasswordInput;
    private TextView userInitials;

    private static final String URL = Config.API_URL + "account/";

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        lastNameInput = findViewById(R.id.lastName);
        firstNameInput = findViewById(R.id.firstName);
        addressInput = findViewById(R.id.address);
        emailInput = findViewById(R.id.email);
        userInitials = findViewById(R.id.userInitials);
        oldPasswordInput = findViewById(R.id.oldPassword);
        newPasswordInput = findViewById(R.id.newPassword);

        queue = Volley.newRequestQueue(this);

        initialDisplayUpdate();
    }

    /**
     * Permet de récupérer les informations du compte courant
     */
    private void initialDisplayUpdate() {
        queue.add(requestInfo(this.URL + "infos"));
    }

    /**
     * Créer une requête Volley pour récupérer les informations du compte avec l'API
     * @param url l'url de l'API
     * @return le JSON avec les informations du compte en cas de succès
     */
    private JsonObjectRequest requestInfo(String url) {
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
                                Utils.displayToast(getBaseContext(), getString(R.string.error_find_account));
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
                headers.put("X-API-KEY", getApiKey()); // MArche pas this.apiKey
                return headers;
            }
        };
        return jsonObjectRequest;
    }

    /**
     * Affiche un message d'erreur du serveur en cas de problème
     */
    private void displayServerError() {
        Utils.displayToast(getBaseContext(), getString(R.string.error_server));
    }

    /**
     * Permet de mettre à jour les informations du compte courant
     * @param button bouton cliqué
     */
    public void updateAccount(View button) {
        String lastName = Utils.inputValueFormatted(lastNameInput);
        String firstName = Utils.inputValueFormatted(firstNameInput);
        String address = Utils.inputValueFormatted(addressInput);
        String email = Utils.inputValueFormatted(emailInput);
        String oldPassword = Utils.inputValueFormatted(oldPasswordInput);
        String newPassword = Utils.inputValueFormatted(newPasswordInput);

        if (!CheckInput.text(lastName, 1, 50) ||
                !CheckInput.text(firstName, 1, 50) ||
                !CheckInput.text(address, 1, 150) ||
                !CheckInput.email(email)) {

            Utils.displayToast(getBaseContext(), getString(R.string.typing_error));
            return;
        }

        // En cas de modif de mot de passe
        if (oldPassword.isEmpty() || newPassword.isEmpty()
            || !CheckInput.text(newPassword, 8, 50)) {
            Utils.displayToast(getBaseContext(), getString(R.string.error_passwordLenght));
            return;
        }

        JSONObject jsonBody = new JSONObject();
        JSONObject salesperson = new JSONObject();
        try {
            salesperson.put("firstName", firstName);
            salesperson.put("lastName", lastName);
            salesperson.put("address", address);
            salesperson.put("email", email);
            salesperson.put("password", newPassword);
            jsonBody.put("oldPassword", oldPassword);
            jsonBody.put("salesPerson", salesperson);
        } catch (JSONException e) {
            Utils.displayToast(getBaseContext(), getString(R.string.error_server));
        }

        queue.add(requestUpdate(this.URL + "update/" + getAccountId(),
                                jsonBody));
    }

    /**
     * Créer une requête Volley pour mettre à jour les informations du compte à l'aide de l'API
     * @param url l'url de l'API
     * @param body le Json contenant les informations du compte
     * @return Un toast en fonction du résultat de la requête
     */
    private JsonObjectRequest requestUpdate(String url, JSONObject body) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getBaseContext(), R.string.success_account_update,
                                       Toast.LENGTH_LONG).show();

                        updateInitials(firstNameInput.getText().toString(),
                                       lastNameInput.getText().toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                Utils.displayToast(getBaseContext(), getString(R.string.error_find_account));
                            } else {
                                Utils.displayToast(getBaseContext(), getString(R.string.error_server));
                            }
                        } else {
                            Utils.displayToast(getBaseContext(), getString(R.string.error_server));
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-API-KEY", getApiKey());
                return headers;
            }
        };
        return jsonObjectRequest;
    }

    /**
     * Permet de mettre à jour les initiales du profil
     * @param firstName le prénom de l'utilisateur
     * @param lastName le nom de l'utilisateur
     */
    private void updateInitials(String firstName, String lastName) {
        String initials = firstName.charAt(0) + "" + lastName.charAt(0);
        userInitials.setText(initials.toUpperCase());
    }
}