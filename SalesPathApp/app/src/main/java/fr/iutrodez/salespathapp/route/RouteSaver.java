package fr.iutrodez.salespathapp.route;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import fr.iutrodez.salespathapp.entity.Route;

public class RouteSaver {
    private static final String PREFS_NAME = "salespath_prefs";
    private static final String ROUTE_KEY = "saved_route";

    public static void saveRoute(Context context, Route route) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String routeJson = gson.toJson(route);
        editor.putString(ROUTE_KEY + "#" + route.getAccountId(), routeJson);
        editor.apply();
        Log.d("RouteSaver", "Route saved: " + routeJson);
    }

    public static Route loadRoute(Context context, String accountId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String routeJson = prefs.getString(ROUTE_KEY + "#" + accountId, null);
        Log.d("RouteSaver", "Route saved: " + routeJson);
        if (routeJson == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(routeJson, Route.class);
    }

    public static void clearRoute(Context context, Route route) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(ROUTE_KEY + "#" + route.getAccountId());
        editor.apply();
    }
}
