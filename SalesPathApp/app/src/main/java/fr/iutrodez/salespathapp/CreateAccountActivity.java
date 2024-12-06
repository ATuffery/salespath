package fr.iutrodez.salespathapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void goToLogin(View button) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
