package fr.iutrodez.salespathapp.route;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.data.RouteData;
import fr.iutrodez.salespathapp.entity.Contact;
import fr.iutrodez.salespathapp.entity.Route;
import fr.iutrodez.salespathapp.utils.Utils;

public class RouteDetailsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RouteStepAdapter adapter;
    private ArrayList<Contact> steps;
    private Route route;
    private TextView title;
    private TextView startDate;
    private TextView nbSteps;
    private Intent intent;
    private String routeId;
    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());

        // Initialiser la carte
        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

        this.steps = new ArrayList<>();
        this.recyclerView = findViewById(R.id.recyclerView);
        adapter = new RouteStepAdapter(steps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.intent = getIntent();
        this.routeId = intent.getStringExtra("routeId");

        this.title = findViewById(R.id.itineraryTitle);
        this.startDate = findViewById(R.id.startedAt);
        this.nbSteps = findViewById(R.id.nbSteps);

        // Charger la liste des contacts
        RouteData.getRouteInfos(getBaseContext(), getApiKey(), this.routeId, new RouteData.OnRouteDetailsLoadedListener() {
            @Override
            public void OnRouteDetailsLoaded(Route data) {
                route = data;
                steps = route.getSteps();
                displayInfos();
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayToast(getBaseContext(), errorMessage);
            }
        });
    }

    private void addMarkers(ArrayList<Contact> contacts) {
        Drawable markerIcon = ContextCompat.getDrawable(this, R.drawable.marker);
        for (Contact contact:
             contacts) {
                double latitude = contact.getLatitude();
                double longitude = contact.getLongitude();
                GeoPoint contactPoint = new GeoPoint(latitude, longitude);

                Marker marker = new Marker(map);
                marker.setPosition(contactPoint);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle(contact.getName());
                marker.setIcon(markerIcon);
                map.getOverlays().add(marker);
        }

        IMapController mapController = map.getController();
        mapController.setZoom(Config.MAP_DEFAULT_ZOOM);
        GeoPoint startPoint = new GeoPoint(Config.MAP_DEFAULT_LATITUDE, Config.MAP_DEFAULT_LONGITUDE);
        mapController.setCenter(startPoint);
    }

    private void displayInfos() {
        recyclerView.setAdapter(adapter);
        // Mettre à jour l'adaptateur
        adapter.notifyDataSetChanged();

        this.title.setText(this.route.getName());
        this.startDate.setText("Commencé le " + Utils.formatDateFr(this.route.getStartDate()));
        this.nbSteps.setText(this.steps.size() > 1 ? this.steps.size() + " étapes" : this.steps.size() + " étape");
        addMarkers(this.steps);
        addLine();
    }

    private void addLine() {
        ArrayList<GeoPoint> points = this.route.getLocalisation();

        if (points == null || points.size() < 2) {
            Log.e("MapDebug", "Pas assez de points pour tracer la route.");
            return;
        }

        // Création de la polyline
        Polyline routeLine = new Polyline();
        routeLine.setPoints(points);
        routeLine.setColor(ContextCompat.getColor(this, R.color.primary));
        routeLine.setWidth(8);
        routeLine.setGeodesic(true);

        // Ajout de la ligne à la carte
        map.getOverlayManager().add(routeLine);

        // Zoom sur le tracé
        BoundingBox boundingBox = BoundingBox.fromGeoPoints(points);
        map.zoomToBoundingBox(boundingBox, true);

        // Rafraîchir la carte
        map.postInvalidate();

    }

}
