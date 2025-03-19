package fr.iutrodez.salespathapp.route;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.card.CardWithTwoLines;
import fr.iutrodez.salespathapp.card.CardWithTwoLinesAdapteur;
import fr.iutrodez.salespathapp.data.RouteData;
import fr.iutrodez.salespathapp.entity.Route;
import fr.iutrodez.salespathapp.utils.Utils;

public class RouteListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private CardWithTwoLinesAdapteur adapter;
    private ArrayList<CardWithTwoLines> routes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        this.routes = new ArrayList<>();

        this.recyclerView = findViewById(R.id.recyclerView);
        adapter = new CardWithTwoLinesAdapteur(routes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getRoutes();
    }

    private void getRoutes() {
        new Thread(() -> {
            RouteData.getRoutesForAccount(getBaseContext(), getApiKey(), getAccountId(), new RouteData.OnRouteListLoadedListener() {
                @Override
                public void OnRouteListLoaded(ArrayList<Route> data) {
                    runOnUiThread(() -> {
                        for (Route route : data) {
                            String steps = route.countVisitedContact() + " contacts visités";
                            String date = "Commencé le " + Utils.formatDateFr(route.getStartDate());
                            CardWithTwoLines card = new CardWithTwoLines(route.getName(), "TERMINÉE", date, steps, "Détails", () -> {
                                Intent intent = new Intent(RouteListActivity.this, RouteDetailsActivity.class);
                                intent.putExtra("routeId", route.getRouteId());
                                startActivity(intent);
                            });
                            routes.add(card);
                        }
                        adapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> Utils.displayToast(getBaseContext(), errorMessage));
                }
            });
        }).start();
    }

}
