package fr.iutrodez.salespathapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialiser la MapView
        mapView = findViewById(R.id.map);

        // Défini le zoom et le centre initial de la carte
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);  // Activer les contrôles de zoom
        mapView.setMultiTouchControls(true);    // Activer le zoom avec pincement
        mapView.zoomToBoundingBox(new org.osmdroid.util.BoundingBox(48.0, 2.0, 47.0, 1.0), true);

        // Définir le point de départ de la carte
        IGeoPoint startPoint = new GeoPoint(48.8566, 2.3522);  // Paris, France
        mapView.getController().setCenter(startPoint);

        // Ajouter un marqueur
        addMarker(startPoint, "Paris", "Bienvenue à Paris!");
    }

    private void addMarker(IGeoPoint point, String title, String description) {
        // Créer un marqueur
        Marker marker = new Marker(mapView);
        marker.setPosition((GeoPoint) point);
        marker.setTitle(title);
        marker.setSnippet(description);

        // Ajoute le marqueur à la carte
        mapView.getOverlays().add(marker);
    }
}