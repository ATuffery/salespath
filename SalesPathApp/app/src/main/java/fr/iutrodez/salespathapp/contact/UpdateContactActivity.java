package fr.iutrodez.salespathapp.contact;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import fr.iutrodez.salespathapp.BaseActivity;
import fr.iutrodez.salespathapp.R;

public class UpdateContactActivity extends BaseActivity {

    private EditText companyNameInput;
    private EditText companyAddressInput;
    private EditText companyDescriptionInput;
    private EditText latInput;
    private EditText lonInput;
    private EditText lastNameInput;
    private EditText firstNameInput;
    private EditText phoneNumberInput;
    private RadioGroup typeInput;
    private TextView msgError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        this.companyAddressInput = findViewById(R.id.companyAddress);
        this.companyNameInput = findViewById(R.id.companyName);
        this.companyDescriptionInput = findViewById(R.id.companyDescription);
        this.latInput = findViewById(R.id.lat);
        this.lonInput = findViewById(R.id.lon);
        this.lastNameInput = findViewById(R.id.lastName);
        this.firstNameInput = findViewById(R.id.firstName);
        this.phoneNumberInput = findViewById(R.id.phoneNumber);
        this.typeInput = findViewById(R.id.contactType);
        this.msgError = findViewById(R.id.msgError);
    }


}
