package fr.iutrodez.salespathapp.utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class Utils {

    public static String dataAccess(Activity activity, String name) {
        SharedPreferences mesPreferences = activity.getSharedPreferences("me.xml", Activity.MODE_PRIVATE);
        return mesPreferences.getString(name, "");
    }

}
