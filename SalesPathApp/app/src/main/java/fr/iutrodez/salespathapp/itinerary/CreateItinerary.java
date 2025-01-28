package fr.iutrodez.salespathapp.itinerary;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.contact.ContactAdapter;
import fr.iutrodez.salespathapp.contact.ContactCheckbox;
import fr.iutrodez.salespathapp.data.ContactData;
import fr.iutrodez.salespathapp.data.ItineraryData;
import fr.iutrodez.salespathapp.utils.Utils;

public class CreateItinerary extends BaseActivity {

    private RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_itinerary);

        rvContacts = findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        fillContacts();

        contactAdapter = new ContactAdapter(contactList);
        rvContacts.setAdapter(contactAdapter);
    }

    private void fillContacts() {
        contactList = new ArrayList<>();

        // Appel à ContactData pour récupérer les données
        ContactData.getContacts(getBaseContext(), getApiKey(), getAccountId(), new ContactData.OnContactsLoadedListener() {
            @Override
            public void onContactsLoaded(ArrayList<JSONObject> contacts) {
                for (JSONObject jsonContact : contacts) {
                    try {
                        String name = jsonContact.getString("firstName") + " " + jsonContact.getString("lastName");
                        String details = jsonContact.getString("enterpriseName") + " - " + jsonContact.getString("address");

                        Contact contact = new Contact(name, details, ContactCheckbox.UNCHECKED);
                        contactList.add(contact);

                    } catch (JSONException e) {
                        Utils.displayServerError(getBaseContext(), "Erreur lors de la lecture des données.");
                    }
                }

                // Mise à jour de l'interface utilisateur
                ContactAdapter adapter = new ContactAdapter(contactList);
                rvContacts.setAdapter(adapter);
                rvContacts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayServerError(getBaseContext(), errorMessage);
            }
        });
    }

}

