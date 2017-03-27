package labs.pax.com.paxshopgallery.model;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import labs.pax.com.paxshopgallery.controller.SharedPreferenceManager;
import labs.pax.com.paxshopgallery.model.PaxProducts.Product;

public class PAXModel {
    private PaxProducts paxProducts;

    private PAXModel() {
    }

    private static class PAXModelSingleton {
        private static final PAXModel INSTANCE = new PAXModel();
    }

    public static PAXModel getInstance() {
        return PAXModelSingleton.INSTANCE;
    }

    public void setPaxProducts(final PaxProducts paxProducts) {
        this.paxProducts = paxProducts;
    }

    public PaxProducts getPaxProducts() {
        return paxProducts;
    }

    public List<Product> getProducts() {
        if (paxProducts == null) {
            return new ArrayList<>();
        }
        return paxProducts.pods;
    }

    public List<Product> getFavoriteProducts(final Activity activity) {
        final List<Product> currentProducts = paxProducts.pods;
        final List<Product> filteredProducts = new ArrayList<>();
        final Set<String> favorites = SharedPreferenceManager.getFavorites(activity);
        for (final Product product : currentProducts) {
            if (favorites.contains(product.id)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
}
