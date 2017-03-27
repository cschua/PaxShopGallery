package labs.pax.com.paxshopgallery.view;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import labs.pax.com.paxshopgallery.R;
import labs.pax.com.paxshopgallery.model.PAXModel;
import labs.pax.com.paxshopgallery.model.PaxProducts;
import labs.pax.com.paxshopgallery.model.PaxProducts.Product;

public class ProductsFragment extends Fragment {
    public final static String ARG_IS_FAVORITES = "argIsFavorites";
    private final static String PAX_PRODUCTS_JSON_URL = "https://s3-us-west-2.amazonaws.com/android-coding-test/products.json";
    private boolean isFavorites;

    public static ProductsFragment newInstance(final boolean isFavorites) {
        final ProductsFragment fragment = new ProductsFragment();
        final Bundle arguments = new Bundle();
        arguments.putBoolean(ARG_IS_FAVORITES, isFavorites);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.products_layout, container, false);
        final Bundle args = getArguments();
        isFavorites = args == null ? false : args.getBoolean(ARG_IS_FAVORITES, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshProducts(isFavorites);
    }

    public void refreshProducts(final boolean isFavorites) {
        final PaxProducts paxProducts = PAXModel.getInstance().getPaxProducts();
        final ShowProductsDataTask productsTask = new ShowProductsDataTask(getActivity(), getView(), isFavorites);
        if (paxProducts == null) {
            productsTask.execute(PAX_PRODUCTS_JSON_URL);
        } else {
            productsTask.updateUI(isFavorites);
        }
    }

    public static class ShowProductsDataTask extends AsyncTask<String, Void, PaxProducts> {
        private final boolean isFavorites;
        private final WeakReference<Activity> activityWeakReference;
        private final ProgressBar progressBar;
        private final View.OnClickListener onClickListener;

        public ShowProductsDataTask(final Activity activity, final View view, final boolean isFavorites) {
            if (activity != null && !activity.isFinishing()) {
                activityWeakReference = new WeakReference<>(activity);
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            } else {
                activityWeakReference = new WeakReference<>(null);
                progressBar = null;
            }
            this.isFavorites = isFavorites;

            // only used to refresh favorites list when star is updated
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (isFavorites) {
                        // TODO a better way to delete favorites
                        updateUI(isFavorites);
                    }
                }
            };
        }

        @Override
        protected PaxProducts doInBackground(String... urls) {
            updateProgress(true);
            try {
                final InputStream inputStream = new URL(urls[0]).openStream();
                return LoganSquare.parse(inputStream, PaxProducts.class);
            } catch (final Exception e) {
                Log.w("ShowProductsDataTask", "Failed to get json data");
                return null;
            }
        }

        @Override
        protected void onPostExecute(PaxProducts paxProducts) {
            PAXModel.getInstance().setPaxProducts(paxProducts);
            updateUI(isFavorites);
        }

        private void updateProgress(final boolean show) {
            if (progressBar == null) {
                return;
            }
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.clearAnimation();
                progressBar.setVisibility(View.GONE);
            }
        }

        public void updateUI(final boolean isFavorites) {
            try {
                final Activity activity = activityWeakReference.get();
                if (activity != null && !activity.isFinishing()) {

                    final List<Product> products;
                    if (isFavorites) {
                        products = PAXModel.getInstance().getFavoriteProducts(activity);
                    } else {
                        products = PAXModel.getInstance().getProducts();
                    }

                    final TextView messageTextView = (TextView) activity.findViewById(R.id.message);
                    final RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.products_recyclerview);
                    if (products.size() <= 0) {
                        recyclerView.setVisibility(View.GONE);
                        messageTextView.setVisibility(View.VISIBLE);
                    } else {
                        messageTextView.setVisibility(View.GONE);
                        final ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(activity, products, onClickListener);
                        recyclerView.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(), 2));
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            } catch (final Exception e) {
                Log.w(ShowProductsDataTask.class.getSimpleName(), "Failed to show products: ");
                e.printStackTrace();
            } finally {
                updateProgress(false);
            }
        }
    }
}
