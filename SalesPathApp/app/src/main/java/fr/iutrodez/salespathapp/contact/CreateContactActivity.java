package fr.iutrodez.salespathapp.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

public class CreateContactActivity extends BaseActivity {

    private EditText companyNameInput;
    private EditText companyAddressInput;
    private EditText companyDescriptionInput;
    private EditText lastNameInput;
    private EditText firstNameInput;
    private EditText phoneNumberInput;
    private RadioGroup typeInput;
    private TextView msgError;
    private final static String URL = Config.API_URL + "client";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        this.companyAddressInput = findViewById(R.id.companyAddress);
        this.companyNameInput = findViewById(R.id.companyName);
        this.companyDescriptionInput = findViewById(R.id.companyDescription);
        this.lastNameInput = findViewById(R.id.lastName);
        this.firstNameInput = findViewById(R.id.firstName);
        this.phoneNumberInput = findViewById(R.id.phoneNumber);
        this.typeInput = findViewById(R.id.contactType);
        this.msgError = findViewById(R.id.msgError);

        findViewById(R.id.typeProspect).setSelected(true);
        findViewById(R.id.delete_button).setVisibility(View.INVISIBLE);
    }

    /**
     * Redigire vers la liste des contacts
     */
    public void goToContacts() {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }

    /**
     * Créer un contact à partir des données saisies par l'utilisateur
     * @param button le bouton de création
     */
    public void create(View button) {
        String companyName = companyNameInput.getText().toString().trim();
        String address = companyAddressInput.getText().toString().trim();
        String description = companyDescriptionInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String phone = phoneNumberInput.getText().toString().trim();
        String type = ((RadioButton) findViewById(typeInput.getCheckedRadioButtonId())).getText().toString();

        if (!CheckInput.text(description, 1, 150) ||
            !CheckInput.text(companyName, 1, 50) ||
            !CheckInput.text(lastName, 1, 50) ||
            !CheckInput.text(firstName, 1, 50) ||
            !CheckInput.text(address, 1, 150) ||
            !CheckInput.text(phone, 10, 10) ||
            !CheckInput.text(type, 1, 50)) {

            Utils.displayToast(getBaseContext(), getString(R.string.typing_error));
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("enterpriseName", companyName);
            jsonBody.put("address", address);
            jsonBody.put("description", description);
            jsonBody.put("firstName", firstName);
            jsonBody.put("lastName", lastName);
            jsonBody.put("phoneNumber", phone);
            jsonBody.put("isClient", type.equals("Client"));
            jsonBody.put("idPerson", getAccountId());
        } catch (JSONException e) {
            Utils.displayToast(getBaseContext(), getString(R.string.error_server));
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(requestCreation(this.URL, jsonBody));
    }

    private JsonObjectRequest requestCreation(String url, JSONObject body) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        goToContacts();
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
                return headers;
            }
        };
        return jsonObjectRequest;
    }

}
