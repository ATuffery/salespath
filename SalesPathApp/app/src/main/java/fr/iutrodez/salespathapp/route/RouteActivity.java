package fr.iutrodez.salespathapp.route;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Collections;

import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.contact.ContactCheckbox;
import fr.iutrodez.salespathapp.data.ItineraryData;
import fr.iutrodez.salespathapp.itinerary.Itinerary;
import fr.iutrodez.salespathapp.itinerary.Step;
import fr.iutrodez.salespathapp.utils.Utils;

public class RouteActivity extends AppCompatActivity {

    private MapView map;
    private String itineraryId;
    private TextView title;
    private ArrayList<Contact> contacts;
    private String apiKey;
    private String accountId;
    private TextView nextContact;
    private TextView nextAddress;
    private TextView nextContactType;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.contacts = new ArrayList<>();

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_route);

        // Récupérer l'ID de l'itinéraire depuis l'intent
        Intent intent = getIntent();
        this.itineraryId = intent.getStringExtra("itineraryId");
        this.apiKey = intent.getStringExtra("apiKey");
        this.accountId = intent.getStringExtra("accountId");

        // Initialisation des composants UI
        this.title = findViewById(R.id.itineraryTitle);
        this.nextAddress = findViewById(R.id.prospectAddress);
        this.nextContact = findViewById(R.id.prospectName);
        this.nextContactType = findViewById(R.id.contactType);
        this.map = findViewById(R.id.map);

        // Charger les données de l'itinéraire
        loadItineraryData();

        this.route = new Route(this.itineraryId, contacts);
        displayContactInfo();
    }

    private void displayContactInfo() {
        Contact current = this.route.getCurrentContact();
        this.nextContact.setText(current.getName());
        this.nextAddress.setText(current.getAddress());
    }

    private void loadItineraryData() {
        ItineraryData.getItinerariesInfos(getBaseContext(), this.apiKey, itineraryId, new ItineraryData.OnItineraryDetailsLoadedListener() {
            @Override
            public void OnItineraryDetailsLoaded(Itinerary itinerary) {
                runOnUiThread(() -> {
                    title.setText(itinerary.getNameItinerary());

                    addMarkers(itinerary);

                    contacts.clear();
                    for (Step step : itinerary.getSteps()) {
                        contacts.add(new Contact(
                                step.getContact().getId(),
                                step.getContact().getName(),
                                step.getContact().getAddress(),
                                step.getContact().getLatitude(),
                                step.getContact().getLongitude(),
                                ContactCheckbox.NO_CHECKBOX,
                                step.getContact().isClient()
                        ));
                    }
                    // Met dans l'ordre inverse pour commencer par l'étape 1
                    Collections.reverse(contacts);
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Utils.displayError(getBaseContext(), errorMessage));
            }
        });
    }

    private void addMarkers(Itinerary itinerary) {
        Drawable markerIcon = ContextCompat.getDrawable(this, R.drawable.marker);

        for (Step step : itinerary.getSteps()) {
            GeoPoint point = new GeoPoint(step.getContact().getLatitude(), step.getContact().getLongitude());
            Marker marker = new Marker(map);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(step.getContact().getName());
            marker.setIcon(markerIcon);
            map.getOverlays().add(marker);
        }

        // Configuration de la carte
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(Config.MAP_DEFAULT_ZOOM - 2);
        mapController.setCenter(new GeoPoint(Config.MAP_DEFAULT_LATITUDE, Config.MAP_DEFAULT_LONGITUDE));
    }

}
