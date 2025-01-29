package fr.iutrodez.salespathapp;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.iutrodez.salespathapp.card.CardWithTwoLines;
import fr.iutrodez.salespathapp.card.CardWithTwoLinesAdapteur;
import fr.iutrodez.salespathapp.itinerary.CreateItineraryActivity;
import fr.iutrodez.salespathapp.itinerary.DetailsItineraryActivity;

public class MainActivity extends BaseActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_home);

        // Initialiser la carte
        map = findViewById(R.id.map);
        map.setMultiTouchControls(true); // Permet le zoom avec deux doigts

        IMapController mapController = map.getController();
        mapController.setZoom(13.0);
        GeoPoint startPoint = new GeoPoint(Config.MAP_DEFAULT_LATITUDE, Config.MAP_DEFAULT_LONGITUDE);
        mapController.setCenter(startPoint);

        // Ajouter des marqueurs
        addMarkers();

        // Initialiser le RecyclerView horizontal
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHorizontal);

        List<CardWithTwoLines> itineraryList = Arrays.asList(
                new CardWithTwoLines(
                        "IUT Rodez",
                        "Enregistré",
                        "Créé le 01/05/2021",
                        "4 clients/prospects à visiter",
                        "Détails",
                        () -> {
                            // Action pour l'itinéraire "IUT Rodez"
                            System.out.println("Action pour IUT Rodez exécutée !");
                        }
                ),
                new CardWithTwoLines(
                        "Itinéraire 1",
                        "Enregistré",
                        "Créé le 10/10/2024",
                        "5 clients/prospects à visiter",
                        "Détails",
                        () -> {
                            // Action pour l'itinéraire "Itinéraire 1"
                            System.out.println("Action pour Itinéraire 1 exécutée !");
                        }
                ),
                new CardWithTwoLines(
                        "Itinéraire 2",
                        "Brouillon",
                        "Créé le 10/10/2024",
                        "5 clients/prospects à visiter",
                        "Détails",
                        () -> {
                            // Action pour l'itinéraire "Itinéraire 2"
                            System.out.println("Action pour Itinéraire 2 exécutée !");
                        }
                ),
                new CardWithTwoLines(
                        "Itinéraire 3",
                        "Enregistré",
                        "Créé le 10/10/2024",
                        "5 clients/prospects à visiter",
                        "Détails",
                        () -> {
                            goToItineraryDetails("3");
                        }
                )
        );

        // Configurer l'adaptateur
        CardWithTwoLinesAdapteur adapter = new CardWithTwoLinesAdapteur(itineraryList);
        recyclerView.setAdapter(adapter);

        // Configurer un LinearLayoutManager en mode horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void goToItineraryDetails(String itineraryId) {
        Intent intent = new Intent(this, DetailsItineraryActivity.class);

        Intent intentParent = getIntent();
        intent.putExtra("itineraryId", itineraryId);

        startActivity(intent);
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

    public void gotToAddItinerary(View btn) {
        Intent intent = new Intent(this, CreateItineraryActivity.class);
        startActivity(intent);
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
