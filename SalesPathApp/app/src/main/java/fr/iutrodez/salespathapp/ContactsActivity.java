package fr.iutrodez.salespathapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class ContactsActivity extends BaseActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHorizontal);

        List<CardWithTwoLines> itineraryList = Arrays.asList(
                new CardWithTwoLines(
                        "Lohan Vignals",
                        "Client",
                        "25 Avenue de Bordeaux 12000 Rodez",
                        "Pierre Fabre",
                        "Voir détails",
                        () -> {
                            // Action à exécuter au clic sur le bouton
                            Log.d("Lohan", "Lohan");
                        }
                ),
                new CardWithTwoLines(
                        "Antoine Tuffery",
                        "Prospect",
                        "25 Avenue de Bordeaux 12000 Rodez",
                        "Doxallia",
                        "Voir détails",
                        () -> {
                            // Autre action
                            Log.d("Antoine", "Antoine");
                        }
                )
        );


        CardWithTwoLinesAdapteur adapter = new CardWithTwoLinesAdapteur(itineraryList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void goToCreateContact(View btn) {
        Log.d("test", "onclick");
        Intent intent = new Intent(this, CreateContactActivity.class);
        startActivity(intent);
    }

}