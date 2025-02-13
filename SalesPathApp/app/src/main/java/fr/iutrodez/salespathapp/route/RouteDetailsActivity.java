package fr.iutrodez.salespathapp.route;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.card.CardWithTwoLinesAdapteur;
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
    private TextView endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        this.recyclerView = findViewById(R.id.recyclerView);
        adapter = new RouteStepAdapter(steps);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Charger la liste des contacts
        RouteData.getRouteInfos(getBaseContext(), getApiKey(), "12", new RouteData.OnRouteDetailsLoadedListener() {
            @Override
            public void OnRouteDetailsLoaded(Route data) {
                route = data;
                steps = route.getSteps();
                displayInfos();

                // Mettre à jour l'adaptateur
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Utils.displayToast(getBaseContext(), errorMessage);
            }
        });
    }

    private void displayInfos() {

    }

}
