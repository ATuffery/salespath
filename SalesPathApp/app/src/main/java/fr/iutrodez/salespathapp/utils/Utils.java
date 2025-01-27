package fr.iutrodez.salespathapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

public class Utils {

    public static String dataAccess(Activity activity, String name) {
        SharedPreferences mesPreferences = activity.getSharedPreferences("me.xml", Activity.MODE_PRIVATE);
        return mesPreferences.getString(name, "");
    }

    /**
     * Récupère la valeur d'un input en supprimant les espaces en
     * trop à la fin
     * @param input
     * @return la valeur de l'input formattée
     */
    public static String inputValueFormatted(EditText input) {
        return input.getText().toString().trim();
    }

    /** Méthode pour afficher une erreur serveur */
    public static void displayServerError(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }



}
