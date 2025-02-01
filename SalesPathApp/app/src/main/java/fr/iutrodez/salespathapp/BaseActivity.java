package fr.iutrodez.salespathapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import fr.iutrodez.salespathapp.auth.LoginActivity;
import fr.iutrodez.salespathapp.contact.ContactsActivity;
import fr.iutrodez.salespathapp.itinerary.ItinerariesActivity;
import fr.iutrodez.salespathapp.user.MyAccountActivity;
import fr.iutrodez.salespathapp.utils.Utils;

public class BaseActivity extends AppCompatActivity {

    private String apiKey;
    private String accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.apiKey = Utils.dataAccess(this, "apiKey");
        this.accountID = Utils.dataAccess(this, "accountId");
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getAccountId() {
        return this.accountID;
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
            // Action pour la page d'accueil
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_account) {
            // Action pour la page "Mon compte"
            Intent intent = new Intent(this, MyAccountActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_contacts) {
            // Action pour la page "Mes contacts"
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_parcours) {
            // Action pour "Mes parcours"
            return true;

        } else if (id == R.id.nav_itineraires) {
            // Action pour la page "Mes itinéraires"
            Intent intent = new Intent(this, ItinerariesActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_logout) {
            // Action de déconnexion
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
