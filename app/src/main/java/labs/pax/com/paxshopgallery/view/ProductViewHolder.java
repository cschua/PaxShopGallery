package labs.pax.com.paxshopgallery.view;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.Set;

import labs.pax.com.paxshopgallery.R;
import labs.pax.com.paxshopgallery.controller.SharedPreferenceManager;
import labs.pax.com.paxshopgallery.model.PaxProducts.Product;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public final static int STAR_BUTTON_TAG = R.id.star;
    public final static int PRODUCT_TAG = R.layout.product_detail_layout;
    private final View.OnClickListener onClickListener;
    public final ImageView thumbnailView;
    public final ImageView largeImageView;
    public final TextView nameTextView;
    public final TextView descTextView;
    public final TextView priceTextView;
    public final SparkButton starButton;

    public ProductViewHolder(final View itemView, final View.OnClickListener onClickListener) {
        super(itemView);
        this.onClickListener = onClickListener;
        thumbnailView = (ImageView) itemView.findViewById(R.id.thumbnail);
        largeImageView = (ImageView) itemView.findViewById(R.id.largeImage);
        nameTextView = (TextView) itemView.findViewById(R.id.name);
        descTextView = (TextView) itemView.findViewById(R.id.description);
        priceTextView = (TextView) itemView.findViewById(R.id.price);
        starButton = (SparkButton) itemView.findViewById(R.id.star);
    }

    public void populate(final Activity activity, final Product product, final boolean isDetails) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        final String productId = product.id;
        if (productId == null || productId.length() <= 0) {
            return;
        }

        initMainOnclick(activity, product, isDetails);
        initImage(product, isDetails);
        initStar(activity, product, isDetails);
        initTexts(product, isDetails);
    }

    private void initMainOnclick(final Activity activity, final Product product, final boolean isDetails) {
        if (isDetails) {
            itemView.setOnClickListener(null);
        } else {
            itemView.setTag(PRODUCT_TAG, product);
            itemView.setOnClickListener(new ProductOnClickListener((FragmentActivity) activity));
        }
    }

    private void initTexts(Product product, final boolean isDetails) {
        nameTextView.setText(product.name);

        final double price = product.price / 100d;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        priceTextView.setText(formatter.format(price));

        if (isDetails) {
            descTextView.setText(product.description);
            descTextView.setVisibility(View.VISIBLE);
        } else {
            descTextView.setVisibility(View.GONE);
        }
    }

    private void initImage(final Product product, final boolean isDetails) {
        thumbnailView.setVisibility(isDetails ? View.GONE : View.VISIBLE);
        largeImageView.setVisibility(isDetails ? View.VISIBLE : View.GONE);
        final ImageView imageView = isDetails ? largeImageView : thumbnailView;
        final String imageURL = isDetails ? product.imageURL : product.thumbnailURL;
        Glide.with(imageView.getContext())
                .load(imageURL)
                .into(imageView);
    }

    private void initStar(final Activity activity, final Product product, final boolean isDetails) {
        if (starButton == null) {
            return;
        }

        if (isDetails) {
            starButton.setVisibility(View.GONE);
            return;
        }

        final Set<String> favorites = SharedPreferenceManager.getFavorites(activity);
        if (favorites.contains(product.id)) {
            starButton.setChecked(true);
        } else {
            starButton.setChecked(false);
        }
        starButton.setVisibility(View.VISIBLE);
        starButton.findViewById(R.id.ivImage).setTag(STAR_BUTTON_TAG, product);
        starButton.setEventListener(new StarButtonListener(activity, onClickListener));
    }

    public static class ProductOnClickListener implements View.OnClickListener {

        final WeakReference<FragmentActivity> activityWeakReference;

        public ProductOnClickListener(final FragmentActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onClick(final View view) {
            final FragmentActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            final String tag = ProductDetailDialogFragment.class.getSimpleName();
            final FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            final Fragment productionDetailFragmentDialog = activity.getSupportFragmentManager().findFragmentByTag(tag);
            if (productionDetailFragmentDialog != null) {
                fragmentTransaction.remove(productionDetailFragmentDialog);
            }
            fragmentTransaction.addToBackStack(null);

            final DialogFragment newFragment = ProductDetailDialogFragment.newInstance((Product) view.getTag(PRODUCT_TAG));
            newFragment.show(fragmentTransaction, tag);
        }
    }

    public static class StarButtonListener implements SparkEventListener {
        final WeakReference<Activity> activityWeakReference;
        final View.OnClickListener onClickListener;

        public StarButtonListener(final Activity activity, final View.OnClickListener onClickListener) {
            activityWeakReference = new WeakReference<>(activity);
            this.onClickListener = onClickListener;
        }

        @Override
        public void onEvent(ImageView button, boolean buttonState) {
            final Product product = (Product) button.getTag(STAR_BUTTON_TAG);
            if (buttonState) {
                Snackbar.make(button, "Adding " + product.name, Snackbar.LENGTH_SHORT).show();
                SharedPreferenceManager.saveFavorite(activityWeakReference.get(), product.id);
            } else {
                SharedPreferenceManager.removeFavorite(activityWeakReference.get(), product.id);
                Snackbar.make(button, "Removing " + product.name, Snackbar.LENGTH_SHORT).show();
            }
            if (onClickListener != null) {
                onClickListener.onClick(button);
            }
        }
    }
}
