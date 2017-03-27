package labs.pax.com.paxshopgallery.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferenceManager {
    public final static String FAVORITES = "favorites";

    public static void saveFavorite(final Activity activity, final String productId) {
        updateFavorite(activity, productId, true);
    }

    public static void removeFavorite(final Activity activity, final String productId) {
        updateFavorite(activity, productId, false);
    }

    private static void updateFavorite(final Activity activity, final String productId, final boolean isAdding) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        final SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        final Set<String> favorites = new HashSet<>(getFavorites(activity));
        if (isAdding) {
            favorites.add(productId);
        } else {
            favorites.remove(productId);
        }
        editor.putStringSet(FAVORITES, favorites);
        editor.commit();
    }

    public static Set<String> getFavorites(final Activity activity) {
        final SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getStringSet(FAVORITES, new HashSet<String>());
    }
}
