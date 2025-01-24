package fr.iutrodez.salespathapp.itinerary;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.contact.ContactAdapter;
import fr.iutrodez.salespathapp.contact.ContactCheckbox;

public class CreateItinerary extends AppCompatActivity {

    private RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    private ArrayList<Contact> contactList;
    private String apiKey;
    private String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_itinerary);

        Intent intent = getIntent();
        this.apiKey = intent.getStringExtra("apiKey");
        this.accountId = intent.getStringExtra("accountId");

        rvContacts = findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        contactList.add(new Contact("MARTIN Guillaume", "Pomme", ContactCheckbox.CHECKED));
        contactList.add(new Contact("SERRES Patrice", "Miracle", ContactCheckbox.UNCHECKED));
        contactList.add(new Contact("POSTMAN Nathalie", "Microflop", ContactCheckbox.UNCHECKED));
        contactList.add(new Contact("DENAMIEL JP", "Pell", ContactCheckbox.UNCHECKED));

        contactAdapter = new ContactAdapter(contactList);
        rvContacts.setAdapter(contactAdapter);
    }
}

