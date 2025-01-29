package fr.iutrodez.salespathapp.itinerary;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.MainActivity;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.card.CardWithTwoLines;
import fr.iutrodez.salespathapp.card.CardWithTwoLinesAdapteur;
import fr.iutrodez.salespathapp.contact.ContactAdapter;
import fr.iutrodez.salespathapp.contact.ContactsActivity;
import fr.iutrodez.salespathapp.contact.UpdateContactActivity;
import fr.iutrodez.salespathapp.data.ItineraryData;
import fr.iutrodez.salespathapp.utils.Utils;

public class ItinerariesActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ArrayList<CardWithTwoLines> itineraries;
    private CardWithTwoLinesAdapteur adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itineraries);

        recyclerView = findViewById(R.id.recyclerViewHorizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itineraries = new ArrayList<>();

        fillItineraries();

        adapter = new CardWithTwoLinesAdapteur(itineraries);
        recyclerView.setAdapter(adapter);
    }

    private void fillItineraries() {
        ItineraryData.getItineraries(getBaseContext(), getApiKey(), getAccountId(), new ItineraryData.OnItinerariesLoadedListener() {
            @Override
            public void OnItinerariesLoaded(ArrayList<JSONObject> data) {
                try {
                    for (JSONObject jsonContact : data) {
                        itineraries.add(new CardWithTwoLines(
                                jsonContact.getString("firstName"),
                                jsonContact.getBoolean("client") ? "Client" : "Prospect",
                                jsonContact.getString("address"),
                                jsonContact.getString("enterpriseName"),
                                "Modifier",
                                () -> {
                                    //Intent intent = new Intent(ContactsActivity.this, UpdateContactActivity.class);
                                    //try {
                                    //    intent.putExtra("contactId", jsonContact.getString("id"));
                                    //} catch (JSONException e) {
                                    //    throw new RuntimeException(e);
                                    //}
                                    //startActivity(intent);
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

    public void goToCreateItinerary(View btn) {
        Intent intent = new Intent(this, CreateItineraryActivity.class);
        startActivity(intent);
    }
}
