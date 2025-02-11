package fr.iutrodez.salespathapp.itinerary;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.card.CardWithTwoLines;
import fr.iutrodez.salespathapp.card.CardWithTwoLinesAdapteur;
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
            public void OnItinerariesLoaded(ArrayList<Itinerary> itinerariesList) {
                itineraries.clear();

                for (Itinerary itinerary : itinerariesList) {
                    itineraries.add(new CardWithTwoLines(
                            itinerary.getNameItinerary(),
                            "#" + itinerary.getIdItinerary(),
                            "Créé le " + itinerary.getDateCreation(),
                            itinerary.getNbSteps() + " clients/prospects à visiter",
                            "Supprimer",
                            () -> {
                                deleteItinerary(itinerary.getIdItinerary());
                            }
                    ));
                }

                // Mettre à jour l'adaptateur après avoir rempli la liste
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayToast(getBaseContext(), errorMessage);
            }
        });
    }

    private void deleteItinerary(int itineraryId) {
        ItineraryData.deleteItinerary(getBaseContext(), getApiKey(), itineraryId, new ItineraryData.OnItineraryDeletedListener() {
            @Override
            public void onItineraryDeleted(boolean success, String message) {
                if (success) {
                    // Supprime l'itinéraire de la liste locale
                    itineraries.removeIf(itinerary -> itinerary.getStatus().equals("#" + itineraryId));

                    // Met à jour l'adaptateur après suppression
                    adapter.notifyDataSetChanged();

                    Toast.makeText(ItinerariesActivity.this, "Itinéraire supprimé avec succès !", Toast.LENGTH_SHORT).show();
                } else {
                    Utils.displayToast(getBaseContext(), message);
                }
            }
        });
    }

    public void goToCreateItinerary(View btn) {
        Intent intent = new Intent(this, CreateItineraryActivity.class);
        startActivity(intent);
    }
}
