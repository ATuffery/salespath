package fr.iutrodez.salespathapp.contact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.utils.CheckInput;
import fr.iutrodez.salespathapp.utils.Utils;

public class UpdateContactActivity extends BaseActivity {

    private Intent intent;
    private TextView title;
    private EditText companyNameInput;
    private EditText companyAddressInput;
    private EditText companyDescriptionInput;
    private EditText lastNameInput;
    private EditText firstNameInput;
    private EditText phoneNumberInput;
    private RadioGroup typeInput;
    private Button delete;
    private Button modify;
    private TextView msgError;
    private static final String URL_DELETE = Config.API_URL + "client/";
    private static final String URL_MODIFY = Config.API_URL + "client/";
    private static final String URL_INFO = Config.API_URL + "client/getOne/";
    private RequestQueue queue;
    private String contactId;
    private RadioButton client;
    private RadioButton prospect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        this.queue = Volley.newRequestQueue(this);

        this.intent = getIntent();
        this.contactId = intent.getStringExtra("contactId");

        this.title = findViewById(R.id.title);
        this.delete = findViewById(R.id.delete_button);
        this.modify = findViewById(R.id.create_button);
        this.companyAddressInput = findViewById(R.id.companyAddress);
        this.companyNameInput = findViewById(R.id.companyName);
        this.companyDescriptionInput = findViewById(R.id.companyDescription);
        this.lastNameInput = findViewById(R.id.lastName);
        this.firstNameInput = findViewById(R.id.firstName);
        this.phoneNumberInput = findViewById(R.id.phoneNumber);
        this.typeInput = findViewById(R.id.contactType);
        this.msgError = findViewById(R.id.msgError);
        this.client = findViewById(R.id.typeContact);
        this.prospect = findViewById(R.id.typeProspect);

        modify.setText("Modifier");
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyContact();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        title.setText(R.string.editContact);

        initialDisplay();
    }

    private void initialDisplay() {
        queue.add(requestInfo(this.URL_INFO + this.contactId));
    }

    private JsonObjectRequest requestInfo(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            companyAddressInput.setText(response.getString("address"));
                            companyNameInput.setText(response.getString("enterpriseName"));
                            companyDescriptionInput.setText(response.getString("description"));
                            lastNameInput.setText(response.getString("lastName"));
                            firstNameInput.setText(response.getString("firstName"));
                            phoneNumberInput.setText(response.getString("phoneNumber"));
                            if (response.getString("client").equals("true")) {
                                typeInput.check(client.getId());
                            } else {
                                typeInput.check(prospect.getId());
                            }

                        } catch (JSONException e) {
                            Utils.displayServerError(getBaseContext(), getString(R.string.error_server));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                Utils.displayServerError(getBaseContext(), getString(R.string.error_find_account));
                            } else {
                                Utils.displayServerError(getBaseContext(), getString(R.string.error_server));
                            }
                        } else {
                            Utils.displayServerError(getBaseContext(), getString(R.string.error_server));
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

    private void deleteContact() {
        this.queue.add(requestClientDeletion(this.URL_DELETE + this.contactId));
    }

    private void modifyContact() {
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

            Utils.displayServerError(getBaseContext(), getString(R.string.typing_error));
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
            Utils.displayServerError(getBaseContext(), getString(R.string.error_server));
        }

        this.queue.add(requestClientModification(this.URL_MODIFY + this.contactId, jsonBody));
    }

    private void goToContacts() {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }

    private JsonObjectRequest requestClientDeletion(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        goToContacts();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.displayServerError(getBaseContext(), getString(R.string.error_server));
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

    private JsonObjectRequest requestClientModification(String url, JSONObject body) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        goToContacts();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.displayServerError(getBaseContext(), getString(R.string.error_server));
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
