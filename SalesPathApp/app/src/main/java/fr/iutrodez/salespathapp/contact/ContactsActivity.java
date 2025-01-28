package fr.iutrodez.salespathapp.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.card.CardWithTwoLines;
import fr.iutrodez.salespathapp.card.CardWithTwoLinesAdapteur;
import fr.iutrodez.salespathapp.data.ContactData;
import fr.iutrodez.salespathapp.data.ItineraryData;
import fr.iutrodez.salespathapp.utils.Utils;

public class ContactsActivity extends BaseActivity {

    private String id;
    private String apiKey;

    private ArrayList<CardWithTwoLines> contacts = new ArrayList<>();
    private CardWithTwoLinesAdapteur adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        this.apiKey = Utils.dataAccess(this, "apiKey");
        this.id = Utils.dataAccess(this, "accountId");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHorizontal);
        adapter = new CardWithTwoLinesAdapteur(contacts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Charger la liste des contacts
        getClientList();
    }

    private void getClientList() {
        ContactData.getContacts(getBaseContext(), this.apiKey, this.id, new ContactData.OnContactsLoadedListener() {
            @Override
            public void onContactsLoaded(ArrayList<JSONObject> data) {
                try {
                    for (JSONObject jsonContact : data) {
                        contacts.add(new CardWithTwoLines(
                                jsonContact.getString("firstName") + " " + jsonContact.getString("lastName"),
                                jsonContact.getBoolean("client") ? "Client" : "Prospect",
                                jsonContact.getString("address"),
                                jsonContact.getString("enterpriseName"),
                                "Modifier",
                                () -> {
                                    Intent intent = new Intent(ContactsActivity.this, UpdateContactActivity.class);
                                    try {
                                        intent.putExtra("contactId", jsonContact.getString("id"));
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    startActivity(intent);
                                }
                        ));
                    }

                    // Mettre à jour l'adaptateur
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Utils.displayServerError(getBaseContext(), e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayServerError(getBaseContext(), errorMessage);
            }
        });
    }

    public void goToCreateContact(View btn) {
        Intent intent = new Intent(this, CreateContactActivity.class);

        Intent intentParent = getIntent();
        String apiKey = intentParent.getStringExtra("apiKey");
        String accountId = intentParent.getStringExtra("accountId");

        startActivity(intent);
    }
}
