package labs.pax.com.paxshopgallery;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import labs.pax.com.paxshopgallery.PaxProducts.Product;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bumptech.glide.Glide;
import com.varunest.sparkbutton.SparkButton;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {
    final String jsonDataUrl = "https://s3-us-west-2.amazonaws.com/android-coding-test/products.json";

    public static ProductsFragment newInstance() {
        final ProductsFragment fragment = new ProductsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.products_layout, container, false);
        final RetrieveProductDataTask task = new RetrieveProductDataTask(fragmentView);
        task.execute(jsonDataUrl);
        return fragmentView;
    }

    public static class RetrieveProductDataTask extends AsyncTask<String, Void, PaxProducts> {
        final View view;

        public RetrieveProductDataTask (final View view) {
            this.view = view;
        }

        @Override
        protected PaxProducts doInBackground(String... urls) {
            try {
                final InputStream inputStream = new URL(urls[0]).openStream();
                return LoganSquare.parse(inputStream, PaxProducts.class);
            } catch (final Exception e) {
                Log.w("initializeAdapter", "Failed to get json data");
                return null;
            }
        }

        @Override
        protected void onPostExecute(PaxProducts paxProducts) {
            Log.i("RetrieveProductDataTask", paxProducts.toString());
            final ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(view.getContext(), paxProducts.pods);
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.products_recyclerview);
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
            recyclerView.setAdapter(adapter);
        }
    }

    public static class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductViewHolder> {

        private final List<Product> products;
        private final LayoutInflater inflater;

        // data is passed into the constructor
        public ProductRecyclerViewAdapter(final Context context, final List<Product> products) {
            this.inflater = LayoutInflater.from(context);
            this.products = products;
        }

        // inflates the cell layout from xml when needed
        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.product_summary_layout, parent, false);
            ProductViewHolder viewHolder = new ProductViewHolder(view);
            return viewHolder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            final Product product = products.get(position);
            holder.init(product);
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return products.size();
        }
    }

    // stores and recycles views as they are scrolled off screen
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final static int STAR_BUTTON_TAG = R.id.star;
        public final ImageView thumbnailView;
        public final TextView nameTextView;
        public final TextView priceTextView;
        public final SparkButton starButton;

        public ProductViewHolder(final View itemView) {
            super(itemView);
            thumbnailView = (ImageView) itemView.findViewById(R.id.thumbnail);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            priceTextView = (TextView) itemView.findViewById(R.id.price);
            starButton = (SparkButton) itemView.findViewById(R.id.star);
        }

        public void init(final Product product) {
            nameTextView.setText(product.name);
            //priceTextView.setText(product.price);
            //setStarOnClickListener(product.id);
            Glide.with(thumbnailView.getContext())
                    .load(product.thumbnailURL)
                    .into(thumbnailView);
        }

        private void setStarOnClickListener(final String id) {
            if (starButton == null) {
                return;
            }
            starButton.setTag(STAR_BUTTON_TAG, id);
            final View.OnClickListener onStarClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String productId = (String) v.getTag(STAR_BUTTON_TAG);
                }
            };
            starButton.setOnClickListener(onStarClickListener);
        }
    }
}
