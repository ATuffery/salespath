package fr.iutrodez.salespathapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MyAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
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
}