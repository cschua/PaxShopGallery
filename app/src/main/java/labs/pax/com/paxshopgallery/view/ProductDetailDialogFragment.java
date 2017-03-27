package labs.pax.com.paxshopgallery.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import labs.pax.com.paxshopgallery.R;
import labs.pax.com.paxshopgallery.model.PaxProducts.Product;

public class ProductDetailDialogFragment extends DialogFragment {

    public static ProductDetailDialogFragment newInstance(final Product product) {
        final ProductDetailDialogFragment fragment = new ProductDetailDialogFragment();
        final Bundle args = new Bundle();
        args.putSerializable(Product.class.getSimpleName(), product);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.product_detail_layout, container, false);
        final Bundle args = getArguments();
        if (args != null) {
            final Product product = (Product) args.getSerializable(Product.class.getSimpleName());
            final ProductViewHolder viewHolder = new ProductViewHolder(view, null);
            viewHolder.populate(getActivity(), product, true);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.ProductDetailsAnimation;
    }
}
