package fr.iutrodez.salespathapp.itinerary;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.entity.Contact;
import fr.iutrodez.salespathapp.contact.ContactAdapter;
import fr.iutrodez.salespathapp.data.ItineraryData;
import fr.iutrodez.salespathapp.data.RouteData;
import fr.iutrodez.salespathapp.entity.Itinerary;
import fr.iutrodez.salespathapp.route.RouteActivity;
import fr.iutrodez.salespathapp.utils.Utils;

public class DetailsItineraryActivity extends BaseActivity {

    private MapView map;
    private String itineraryId;
    private TextView title;
    private RecyclerView customers;
    private ArrayList<Contact> contacts;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_display_itinerary);

        // Récupérer l'ID de l'itinéraire depuis l'intent
        Intent intent = getIntent();
        this.itineraryId = intent.getStringExtra("itineraryId");

        // Initialisation des composants UI
        this.title = findViewById(R.id.itineraryTitle);
        this.map = findViewById(R.id.map);
        this.customers = findViewById(R.id.itineraryCustomers);

        // Configuration de la liste de contacts
        customers.setLayoutManager(new LinearLayoutManager(this));
        contacts = new ArrayList<>();
        contactAdapter = new ContactAdapter(contacts);
        customers.setAdapter(contactAdapter);

        // Charger les données de l'itinéraire
        loadItineraryData();
    }

    private void loadItineraryData() {
        ItineraryData.getItinerariesInfos(getBaseContext(), getApiKey(), itineraryId, new ItineraryData.OnItineraryDetailsLoadedListener() {
            @Override
            public void OnItineraryDetailsLoaded(Itinerary itinerary) {
                runOnUiThread(() -> {
                    title.setText(itinerary.getNameItinerary());

                    addMarkers(itinerary);

                    contacts.clear();
                    contacts.addAll(itinerary.getSteps());
                    contactAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Utils.displayToast(getBaseContext(), errorMessage));
            }
        });
    }

    private void addMarkers(Itinerary itinerary) {
        Drawable markerIcon = ContextCompat.getDrawable(this, R.drawable.marker);

        for (Contact contact : itinerary.getSteps()) {
            GeoPoint point = new GeoPoint(contact.getLatitude(), contact.getLongitude());
            Marker marker = new Marker(map);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(contact.getName());
            marker.setIcon(markerIcon);
            map.getOverlays().add(marker);
        }

        // Configuration de la carte
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(Config.MAP_DEFAULT_ZOOM - 2);
        mapController.setCenter(new GeoPoint(Config.MAP_DEFAULT_LATITUDE, Config.MAP_DEFAULT_LONGITUDE));
    }

    /**
     * Débute la tournée à partir de l'itinéraire courant
     * @param btn clic sur le bouton "Commencer la tournée"
     */
    public void startRoute(View btn) {
        RouteData.createRoute(this, getApiKey(), getAccountId(), itineraryId, new RouteData.OnRouteCreatedListener() {
            @Override
            public void onRouteCreated(String routeId) {
                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                intent.putExtra("routeId", routeId);
                intent.putExtra("apiKey", getApiKey());
                intent.putExtra("accountId", getAccountId());
                startActivity(intent);
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayToast(getBaseContext(), errorMessage);
            }
        });
    }


}
