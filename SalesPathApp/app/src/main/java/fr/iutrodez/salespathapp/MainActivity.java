package fr.iutrodez.salespathapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView map;

    @SuppressLint("MissingInflatedId")
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
        GeoPoint startPoint = new GeoPoint(44.3500, 2.5750); // Exemple coordonnées (Rodez)
        mapController.setCenter(startPoint);

        // Ajouter des marqueurs
        addMarkers();

        // Initialiser le RecyclerView horizontal
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHorizontal);

        // Exemple de liste d'itinéraires
        List<CardWithTwoLines> itineraryList = Arrays.asList(
                new CardWithTwoLines("IUT Rodez", "Enregistré", "Créé le 01/05/2021", "4 clients/prospects à visiter"),
                new CardWithTwoLines("Itinéraire 1", "Enregistré", "Créé le 10/10/2024", "5 clients/prospects à visiter"),
                new CardWithTwoLines("Itinéraire 2", "Brouillon", "Créé le 10/10/2024", "5 clients/prospects à visiter"),
                new CardWithTwoLines("Itinéraire 3", "Enregistré", "Créé le 10/10/2024", "5 clients/prospects à visiter")
                // Ajouter d'autres cartes ici
        );

        // Configurer l'adaptateur
        CardWithTwoLinesAdapteur adapter = new CardWithTwoLinesAdapteur(itineraryList);
        recyclerView.setAdapter(adapter);

        // Configurer un LinearLayoutManager en mode horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }


    private void addMarkers() {
        ArrayList<GeoPoint> points = new ArrayList<>();
        points.add(new GeoPoint(44.3500, 2.5750));
        points.add(new GeoPoint(44.3400, 2.5650));
        points.add(new GeoPoint(44.3600, 2.5850));
        points.add(new GeoPoint(44.3550, 2.5700));
        points.add(new GeoPoint(44.3450, 2.5800));

        for (GeoPoint point : points) {
            Marker marker = new Marker(map);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Point d'intérêt");
            map.getOverlays().add(marker);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
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

}