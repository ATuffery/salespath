package fr.iutrodez.salespathapp;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.card.CardWithTwoLines;
import fr.iutrodez.salespathapp.card.CardWithTwoLinesAdapteur;
import fr.iutrodez.salespathapp.data.RouteData;
import fr.iutrodez.salespathapp.itinerary.CreateItineraryActivity;
import fr.iutrodez.salespathapp.itinerary.DetailsItineraryActivity;
import fr.iutrodez.salespathapp.data.ItineraryData;
import fr.iutrodez.salespathapp.data.ContactData;
import fr.iutrodez.salespathapp.itinerary.Itinerary;
import fr.iutrodez.salespathapp.route.Route;
import fr.iutrodez.salespathapp.route.RouteActivity;
import fr.iutrodez.salespathapp.route.RouteSaver;
import fr.iutrodez.salespathapp.utils.Utils;

public class MainActivity extends BaseActivity {

    private MapView map;
    private RecyclerView recyclerView;
    private ArrayList<CardWithTwoLines> itineraryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_home);

        // Initialiser la carte
        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

        recyclerView = findViewById(R.id.recyclerViewHorizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        loadItineraries();

        itineraryList = new ArrayList<>();
        CardWithTwoLinesAdapteur adapter = new CardWithTwoLinesAdapteur(itineraryList);
        recyclerView.setAdapter(adapter);

        loadContacts();

        Route backup = RouteSaver.loadRoute(this);
        if (backup != null) {
            Log.d("MainActivity", "Saved route accountId: " + backup.getAccountId() + ", Current accountId: " + getAccountId());
            if (backup.getAccountId().equals(getAccountId())) {
                showResumePopup(backup);
            } else {
                Log.d("MainActivity", "Account mismatch.");
            }
        } else {
            Log.d("MainActivity", "No route found.");
        }

    }

    private void showResumePopup(Route savedRoute) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reprendre la tournée ?");
        builder.setMessage("Une tournée est en cours. Voulez-vous la reprendre ou la terminer ?");

        builder.setPositiveButton("Continuer", (dialog, which) -> {
            Intent intent = new Intent(this, RouteActivity.class);
            intent.putExtra("backupRestore", true);
            intent.putExtra("routeId", savedRoute.getRouteId());
            intent.putExtra("apiKey", getApiKey());
            startActivity(intent);
        });

        builder.setNegativeButton("Terminer", (dialog, which) -> {
            Intent intent = new Intent(this, RouteActivity.class);
            intent.putExtra("backupRestore", false);
            intent.putExtra("routeId", savedRoute.getRouteId());
            intent.putExtra("apiKey", getApiKey());
            startActivity(intent);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadItineraries() {
        ItineraryData.getItineraries(getBaseContext(), getApiKey(), getAccountId(), new ItineraryData.OnItinerariesLoadedListener() {
            @Override
            public void OnItinerariesLoaded(ArrayList<Itinerary> itinerariesList) {
                itineraryList.clear();
                for (Itinerary itinerary : itinerariesList) {
                    // Ajouter chaque itinéraire dans la liste
                    itineraryList.add(new CardWithTwoLines(
                            itinerary.getNameItinerary(),
                            "#" + itinerary.getIdItinerary(),
                            "Créé le " + itinerary.getDateCreation(),
                            itinerary.getNbSteps() + " clients/prospects à visiter",
                            "Détails",
                            () -> {
                                goToItineraryDetails(itinerary.getIdItinerary() + "");
                            }
                    ));
                }
                // Mettre à jour l'adaptateur
                runOnUiThread(() -> {
                    // Actualisation du RecyclerView
                    ((CardWithTwoLinesAdapteur) recyclerView.getAdapter()).notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayToast(getBaseContext(), errorMessage);
            }
        });
    }

    private void goToItineraryDetails(String itineraryId) {
        Intent intent = new Intent(this, DetailsItineraryActivity.class);
        intent.putExtra("itineraryId", itineraryId);
        startActivity(intent);
    }

    private void loadContacts() {
        ContactData.getContacts(getBaseContext(), getApiKey(), getAccountId(), new ContactData.OnContactsLoadedListener() {
            @Override
            public void onContactsLoaded(ArrayList<JSONObject> contactsJson) {
                // Ajouter les marqueurs pour chaque contact sur la carte
                addMarkers(contactsJson);
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayToast(getBaseContext(), errorMessage);
            }
        });
    }

    public void gotToAddItinerary(View btn) {
        Intent intent = new Intent(this, CreateItineraryActivity.class);
        startActivity(intent);
    }

    private void addMarkers(ArrayList<JSONObject> contactsJson) {
        Drawable markerIcon = ContextCompat.getDrawable(this, R.drawable.marker);

        for (int i = 0; i < contactsJson.size(); i++) {
            try {
                JSONObject contactJson = contactsJson.get(i);
                JSONArray coordinates = contactJson.getJSONArray("coordonates");
                double latitude = coordinates.getDouble(0);
                double longitude = coordinates.getDouble(1);

                GeoPoint contactPoint = new GeoPoint(latitude, longitude);

                Marker marker = new Marker(map);
                marker.setPosition(contactPoint);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle(contactJson.getString("lastName") + " " + contactJson.getString("firstName"));
                marker.setIcon(markerIcon);
                map.getOverlays().add(marker);
            } catch (JSONException e) {
                Utils.displayToast(getBaseContext(), getString(R.string.error_server));
            }
        }

        IMapController mapController = map.getController();
        mapController.setZoom(Config.MAP_DEFAULT_ZOOM);
        GeoPoint startPoint = new GeoPoint(Config.MAP_DEFAULT_LATITUDE, Config.MAP_DEFAULT_LONGITUDE);
        mapController.setCenter(startPoint);
    }


    @Override
    protected void onResume() {
        super.onResume();
        map.onResume(); // Nécessaire pour OSMDroid
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause(); // Nécessaire pour OSMDroid
    }

    @Override
    public void onBackPressed() {
        // Ne rien faire ici pour bloquer l'action du bouton de retour
    }
}
