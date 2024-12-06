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

public class ContactsActivity extends AppCompatActivity {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();
            return true;
        } else if (id == R.id.nav_account) {
            return true;
        } else if (id == R.id.nav_contacts) {
            return true;
        } else if (id == R.id.nav_parcours) {
            return true;
        } else if (id == R.id.nav_itineraires) {
            return true;
        } else if (id == R.id.nav_logout) {
            return true;
        }

        // Si aucun des cas ne correspond, appelle la méthode parent.
        return super.onOptionsItemSelected(item);
    }

    public void goToCreateContact(View btn) {
        Log.d("test", "onclick");
        Intent intent = new Intent(this, CreateContactActivity.class);
        startActivity(intent);
    }

}