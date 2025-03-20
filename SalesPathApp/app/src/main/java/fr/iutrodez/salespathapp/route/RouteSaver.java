package fr.iutrodez.salespathapp.route;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import fr.iutrodez.salespathapp.entity.Route;

public class RouteSaver {
    private static final String PREFS_NAME = "salespath_prefs";
    private static final String ROUTE_KEY = "saved_route";

    /**
     * Sauvegarde en local une tournée
     * @param context le contexte de l'activité
     * @param route la tournée a sauvegarder
     */
    public static void saveRoute(Context context, Route route) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String routeJson = gson.toJson(route);
        editor.putString(ROUTE_KEY + "#" + route.getAccountId(), routeJson);
        editor.apply();
    }

    /**
     * Charge une sauvegarde locale
     * @param context le contexte de l'activité
     * @param accountId le numero de compte du commercial
     * @return l'objet route restauré
     */
    public static Route loadRoute(Context context, String accountId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String routeJson = prefs.getString(ROUTE_KEY + "#" + accountId, null);
        if (routeJson == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(routeJson, Route.class);
    }

    /**
     * Supprime la sauvegarde locale
     * @param context le contexte de l'activité
     * @param route la tournée à supprimer
     */
    public static void clearRoute(Context context, Route route) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(ROUTE_KEY + "#" + route.getAccountId());
        editor.apply();
    }
}
