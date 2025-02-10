package fr.iutrodez.salespathapp.route;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iutrodez.salespathapp.Config;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.contact.Contact;
import fr.iutrodez.salespathapp.contact.ContactCheckbox;
import fr.iutrodez.salespathapp.contact.ContactStatus;
import fr.iutrodez.salespathapp.data.RouteData;
import fr.iutrodez.salespathapp.itinerary.Itinerary;
import fr.iutrodez.salespathapp.itinerary.Step;
import fr.iutrodez.salespathapp.utils.Utils;

public class RouteActivity extends AppCompatActivity {

    private MapView map;
    private String routeId;
    private TextView title;
    private ArrayList<Contact> contacts;
    private String apiKey;
    private String accountId;
    private TextView nextContact;
    private TextView nextAddress;
    private TextView nextContactType;
    private TextView nextStop;
    private TextView nextCompany;
    private TextView startedDate;
    private TextView nbVisit;
    private Route route;
    private Button btnVisited;
    private Button btnCancelVisit;
    private Button btnPlayPause;

    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private MyLocationNewOverlay myLocationOverlay;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.contacts = new ArrayList<>();

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_route);

        // Récupérer l'ID du parcours depuis l'intent
        Intent intent = getIntent();
        this.routeId = intent.getStringExtra("routeId");
        this.apiKey = intent.getStringExtra("apiKey");
        this.accountId = intent.getStringExtra("accountId");

        // Initialisation des composants UI
        this.title = findViewById(R.id.itineraryTitle);
        this.nextAddress = findViewById(R.id.prospectAddress);
        this.nextContact = findViewById(R.id.prospectName);
        this.nextContactType = findViewById(R.id.contactType);
        this.nextCompany = findViewById(R.id.prospectCompany);
        this.btnVisited = findViewById(R.id.visitedBtn);
        this.btnCancelVisit = findViewById(R.id.cancelVisitBtn);
        this.nbVisit = findViewById(R.id.nbSteps);
        this.startedDate = findViewById(R.id.startedAt);
        this.btnPlayPause = findViewById(R.id.playPause);
        this.nextStop = findViewById(R.id.nextStop);
        this.map = findViewById(R.id.map);

        // Initialiser la localisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();

        // Charger les données de l'itinéraire
        loadItineraryData();
    }


    private void displayContactInfo() {
        Contact current = this.route.getCurrentContact();
        this.nextContact.setText(current.getName());
        this.nextAddress.setText(current.getAddress());
        this.nextAddress.setText(current.getAddress());
        this.nextCompany.setText(current.getCompany());
        this.nextContactType.setText(current.isClient() ? "Client" : "Prospect");
        this.startedDate.setText("Tournée commencée le " + Utils.formatDateFr(this.route.getStartDate()));
    }

    private void displayEndInfos() {
        this.nextContact.setText("Domicile");
        this.nextAddress.setText("");
        this.nextContactType.setVisibility(View.GONE);
        this.btnCancelVisit.setVisibility(View.GONE);
        this.btnVisited.setText("Terminer la tournée");
        this.btnVisited.setOnClickListener((e) -> {
            Utils.displayError(getBaseContext(), "Pour terminer la tournée, faites un appui long sur ce bouton.");
        });
        this.btnVisited.setOnLongClickListener((e) -> {
            this.route.setStatus(RouteStatus.FINISHED);
            save();
            finish();
            return true;
        });
    }

    private void loadItineraryData() {
        RouteData.getRouteInfos(getBaseContext(), this.apiKey, routeId, new RouteData.OnRouteDetailsLoadedListener() {
            @Override
            public void OnRouteDetailsLoaded(Route data) {
                runOnUiThread(() -> {
                    route = data;
                    fetchRoute();
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


    /**
     * Marque le contact courant en visité ou skipped
     * @param btn bouton avec lequel l'utilisateur a réalisé l'action
     */
    public void contactAction(View btn) {
        ContactStatus status = btn.getId() == R.id.cancelVisitBtn ? ContactStatus.SKIPPED : ContactStatus.VISITED;
        Contact current = this.route.getCurrentContact();
        current.setVisited(status);
        this.nbVisit.setText("Contacts visités : " + this.route.nbVisit());
        if (!this.route.nextStep()) {
            this.displayEndInfos();
        } else {
            this.displayContactInfo();
        }
    }

    private void save() {
        String url = Config.API_URL + "route";

        try {
            JSONObject body = new JSONObject();
            body.put("routeId", this.route.getRouteId());
            body.put("idSalesPerson", this.route.getRouteId());

            if (this.route.getStatus() == RouteStatus.FINISHED) {
                body.put("endDate", new Date());
            }

            JSONArray steps = new JSONArray();
            for (Contact c:
                 this.route.getSteps()) {
                JSONObject contact = new JSONObject();
                JSONObject contactDetails = new JSONObject();
                contactDetails.put("enterpriseName", c.getName());
            }


        } catch (Exception e) {
            Utils.displayError(getBaseContext(), "Une erreur s'est produite lors de la sauvegarde.");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.displayError(getBaseContext(), "Sauvegarde éffectuée !");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.displayError(getBaseContext(), "Une erreur s'est produite lors de la sauvegarde.");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-API-KEY", apiKey);
                return headers;
            }
        };

        // Ajouter la requête à la file d'attente Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void uiOpacity() {
        float opacity = this.route.getStatus() == RouteStatus.PAUSED ? 0.3f : 1.0f;
        this.btnVisited.setAlpha(opacity);
        this.btnVisited.setEnabled(this.route.getStatus() == RouteStatus.STARTED);

        this.btnCancelVisit.setAlpha(opacity);
        this.btnCancelVisit.setEnabled(this.route.getStatus() == RouteStatus.STARTED);

        this.nextStop.setAlpha(opacity);
        this.nextContact.setAlpha(opacity);
        this.nextAddress.setAlpha(opacity);
        this.nextContactType.setAlpha(opacity);
    }

    public void playPause(View btn) {
        if (this.route.getStatus() == RouteStatus.PAUSED) {
            this.route.setStatus(RouteStatus.STARTED);
            this.btnPlayPause.setText("Pause");
            this.btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
            uiOpacity();
        } else if (this.route.getStatus() == RouteStatus.STARTED) {
            this.route.setStatus(RouteStatus.PAUSED);
            this.btnPlayPause.setText("Reprendre");
            this.btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_circle, 0, 0, 0);
            uiOpacity();
            save();
        } else {
            this.btnPlayPause.setVisibility(View.GONE);
        }
    }

    // Demander la permission de localisation
    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            startLocationUpdates();
        }
    }

    // Gérer la réponse utilisateur pour la permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }

    private void fetchRoute() {
        String API_URL = "https://router.project-osrm.org/route/v1/driving/";

        StringBuilder urlBuilder = new StringBuilder(API_URL);

        for (Contact contact : contacts) {
            urlBuilder.append(contact.getLongitude()).append(",").append(contact.getLatitude()).append(";");
        }

        // Supprimer le dernier ";"
        urlBuilder.setLength(urlBuilder.length() - 1);

        urlBuilder.append("?overview=full&geometries=geojson");

        String finalUrl = urlBuilder.toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, finalUrl, null,
                response -> {
                    processRouteResponse(response);
                },
                error -> {
                    Utils.displayError(getBaseContext(), "Erreur lors de la récupération de l'itinéraire.");
                }
        );

        queue.add(jsonObjectRequest);
    }

    private void processRouteResponse(JSONObject response) {
        try {
            JSONArray routes = response.getJSONArray("routes");
            if (routes.length() > 0) {
                JSONArray coordinates = routes.getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONArray("coordinates");

                List<GeoPoint> routePoints = new ArrayList<>();
                for (int i = 0; i < coordinates.length(); i++) {
                    JSONArray coord = coordinates.getJSONArray(i);
                    double lon = coord.getDouble(0);
                    double lat = coord.getDouble(1);
                    routePoints.add(new GeoPoint(lat, lon));
                }

                drawRouteOnMap(routePoints);
            } else {
                Utils.displayError(getBaseContext(), "Aucun itinéraire trouvé !");
            }
        } catch (Exception e) {
            Utils.displayError(getBaseContext(),getString(R.string.error_server));
        }
    }


    private void drawRouteOnMap(List<GeoPoint> points) {
        runOnUiThread(() -> {
            if (points.isEmpty()) {
                return;
            }

            Polyline routeLine = new Polyline();
            routeLine.setPoints(points);
            routeLine.setColor(R.color.grey);
            routeLine.setWidth(8);

            map.getOverlays().add(routeLine);
            map.invalidate(); // Rafraîchir la carte
        });
    }

    private void startLocationUpdates() {
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setPersonIcon(Utils.drawableToBitmap(ContextCompat.getDrawable(this, R.drawable.my_location)));
        map.getOverlays().add(myLocationOverlay);
    }

    public void showNextContactConfirmation(View btn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Êtes-vous sûr de vouloir passer au contact suivant ?");

        builder.setPositiveButton("Oui", (dialog, which) -> {
            this.contactAction(btn);
        });

        builder.setNegativeButton("Non", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
