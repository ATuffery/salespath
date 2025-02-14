package fr.iutrodez.salespathapp.tests.auth;

import static org.junit.Assert.*;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.iutrodez.salespathapp.R;
import fr.iutrodez.salespathapp.auth.CreateAccountActivity;

@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityTest {

    @Rule
    public ActivityScenarioRule<CreateAccountActivity> activityRule = new ActivityScenarioRule<>(CreateAccountActivity.class);

    @Test
    public void goToLoginCasNominal() {
        activityRule.getScenario().onActivity(activity -> {
            activity.goToLogin();
            assertTrue(activity.isFinishing());
        });
    }

    @Test
    public void createAccountCasErreurChampsVides() {
        activityRule.getScenario().onActivity(activity -> {
            activity.createAccount(null);
            TextView msgError = activity.findViewById(R.id.msgError);
            assertEquals(activity.getString(R.string.typing_error), msgError.getText().toString());
        });
    }

    @Test
    public void createAccountCasNominal() {
        activityRule.getScenario().onActivity(activity -> {
            EditText lastNameInput = activity.findViewById(R.id.lastName);
            EditText firstNameInput = activity.findViewById(R.id.firstName);
            EditText addressInput = activity.findViewById(R.id.address);
            EditText emailInput = activity.findViewById(R.id.email);
            EditText passwordInput = activity.findViewById(R.id.password);
            TextView msgError = activity.findViewById(R.id.msgError);

            lastNameInput.setText("Doe");
            firstNameInput.setText("John");
            addressInput.setText("123 Street");
            emailInput.setText("john.doe@example.com");
            passwordInput.setText("password123");
            activity.createAccount(null);

            assertNotEquals(activity.getString(R.string.typing_error), msgError.getText().toString());
        });
    }

    @Test
    public void createAccountCasErreurEmailInvalide() {
        activityRule.getScenario().onActivity(activity -> {
            EditText emailInput = activity.findViewById(R.id.email);
            TextView msgError = activity.findViewById(R.id.msgError);

            emailInput.setText("invalidemail");
            activity.createAccount(null);

            assertEquals(activity.getString(R.string.typing_error), msgError.getText().toString());
        });
    }

    @Test
    public void createAccountCasErreurMotDePasseCourt() {
        activityRule.getScenario().onActivity(activity -> {
            EditText passwordInput = activity.findViewById(R.id.password);
            TextView msgError = activity.findViewById(R.id.msgError);

            passwordInput.setText("short");
            activity.createAccount(null);

            assertEquals(activity.getString(R.string.typing_error), msgError.getText().toString());
        });
    }
}
