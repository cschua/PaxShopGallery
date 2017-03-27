package labs.pax.com.paxshopgallery.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import labs.pax.com.paxshopgallery.R;
import labs.pax.com.paxshopgallery.model.PaxProducts;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private final WeakReference<Activity> activityWeakReference;
    private final View.OnClickListener onClickListener;
    private final List<PaxProducts.Product> products;
    private final LayoutInflater inflater;

    public ProductRecyclerViewAdapter(final Activity activity, final List<PaxProducts.Product> products, final View.OnClickListener onClickListener) {
        activityWeakReference = new WeakReference<>(activity);
        this.onClickListener = onClickListener;
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.products = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_summary_layout, parent, false);
        ProductViewHolder viewHolder = new ProductViewHolder(view, onClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final PaxProducts.Product product = products.get(position);
        holder.populate(activityWeakReference.get(), product, false);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}