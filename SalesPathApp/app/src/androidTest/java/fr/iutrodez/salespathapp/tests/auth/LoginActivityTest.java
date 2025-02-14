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
import fr.iutrodez.salespathapp.auth.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void goToCreateAccountCasNominal() {
        activityRule.getScenario().onActivity(activity -> {
            activity.goToCreateAccount(null);
            assertNotNull(activity);
        });
    }

    @Test
    public void connectCasNominal() {
        activityRule.getScenario().onActivity(activity -> {
            EditText loginEntry = activity.findViewById(R.id.email);
            EditText passwordEntry = activity.findViewById(R.id.password);
            TextView errorMsg = activity.findViewById(R.id.error);

            loginEntry.setText("test@example.com");
            passwordEntry.setText("password123");
            activity.checkLogin(null);

            assertNotEquals(activity.getString(R.string.error_loginEmpty), errorMsg.getText().toString());
        });
    }
}