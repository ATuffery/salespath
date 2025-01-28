package fr.iutrodez.salespathapp.itinerary;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.contact.ContactAdapter;
import fr.iutrodez.salespathapp.contact.ContactCheckbox;
import fr.iutrodez.salespathapp.utils.Utils;

public class DetailsItinerary extends BaseActivity {

    private MapView map;
    private String apiKey;
    private String itineraryId;
    private Intent intent;
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

        this.apiKey = Utils.dataAccess(this, "apiKey");

        this.intent = getIntent();
        this.itineraryId = intent.getStringExtra("itineraryId");
        this.title = findViewById(R.id.itineraryTitle);
        this.title.setText("Itineraire #" + this.itineraryId);

        // Initialiser la carte
        map = findViewById(R.id.map);
        map.setMultiTouchControls(true); // Permet le zoom avec deux doigts

        IMapController mapController = map.getController();
        mapController.setZoom(13.0);
        GeoPoint startPoint = new GeoPoint(Config.MAP_DEFAULT_LATITUDE, Config.MAP_DEFAULT_LONGITUDE);
        mapController.setCenter(startPoint);

        // Ajouter des marqueurs
        addMarkers();

        // Ajoute les propects/clients de l'itineraire
        customers = findViewById(R.id.itineraryCustomers);
        customers.setLayoutManager(new LinearLayoutManager(this));

        contacts = new ArrayList<>();
        contacts.add(new Contact("MARTIN Guillaume", "Prospect - Pomme", ContactCheckbox.NO_CHECKBOX));
        contacts.add(new Contact("SERRES Patrice", "Client - Miracle", ContactCheckbox.NO_CHECKBOX));
        contacts.add(new Contact("POSTMAN Nathalie", "Client - Microflop", ContactCheckbox.NO_CHECKBOX));
        contacts.add(new Contact("DENAMIEL JP", "Prospect - Pell", ContactCheckbox.NO_CHECKBOX));
        contacts.add(new Contact("DENAMIEL JP", "Prospect - Pell", ContactCheckbox.NO_CHECKBOX));
        contacts.add(new Contact("DENAMIEL JP", "Prospect - Pell", ContactCheckbox.NO_CHECKBOX));
        contacts.add(new Contact("DENAMIEL JP", "Prospect - Pell", ContactCheckbox.NO_CHECKBOX));
        contacts.add(new Contact("DENAMIEL JP", "Prospect - Pell", ContactCheckbox.NO_CHECKBOX));

        contactAdapter = new ContactAdapter(contacts);
        customers.setAdapter(contactAdapter);
    }

    private void addMarkers() {
        ArrayList<GeoPoint> points = new ArrayList<>();
        points.add(new GeoPoint(44.3500, 2.5750));
        points.add(new GeoPoint(44.3400, 2.5650));
        points.add(new GeoPoint(44.3600, 2.5850));
        points.add(new GeoPoint(44.3550, 2.5700));
        points.add(new GeoPoint(44.3450, 2.5800));

        Drawable markerIcon = ContextCompat.getDrawable(this, R.drawable.marker);

        for (GeoPoint point : points) {
            Marker marker = new Marker(map);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Point d'intérêt");
            marker.setIcon(markerIcon);
            map.getOverlays().add(marker);
        }
    }
}
