package fr.iutrodez.salespathapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    /**
     * Permet d'accéder aux données mémorisées sur l'appareil client
     * @param activity
     * @param name
     * @return
     */
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

    /** Méthode pour afficher une erreur  */
    public static void displayToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Formate une date en français avec l'heure, les minutes et les secondes.
     *
     * @param date La date à formater.
     * @return Une chaîne représentant la date formatée en français.
     */
    public static String formatDateFr(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM yyyy à HH:mm:ss", Locale.FRANCE);
        return dateFormat.format(date);
    }

    /**
     * Parse une string en Date
     * @param dateStr date en string
     * @param pattern le format de la date en string
     * @return la date parsé en Date
     */
    public static Date parseStringToDate(String dateStr, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.FRANCE);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Convertie un drawable en Bitmap
     * @param drawable Le drawable à convertir
     * @return Le bitmap converti à partir du drawable
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * Vérifie si deux points sont identique
     * @param pointA point A
     * @param pointB point B
     * @return true si le point A et B sont identique, false sinon
     */
    public static boolean isSameLocation(GeoPoint pointA, GeoPoint pointB) {
        return pointA.getLatitude() == pointB.getLatitude() && pointA.getLongitude() == pointB.getLongitude();
    }

    /**
     * Converti une date en ISO date pour l'API
     * @param date la date à convertir
     * @return la date convertie
     */
    public static String formatDateToISOString(Date date) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return isoFormat.format(date);
    }
}
