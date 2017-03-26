package labs.pax.com.paxshopgallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {
    private final static String ARG_TITLE = "ARGTITLE";

    public static FavoritesFragment newInstance(final String title) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String title = getArguments().getString(ARG_TITLE, FavoritesFragment.class.getSimpleName());
        /**
         * If set to true then when your layout is inflated it will be automatically added to the
         * view hierarchy of the ViewGroup specified in the 2nd parameter as a child. For example
         * if the root parameter was a LinearLayout then your inflated view will be automatically
         * added as a child of that view.
         *
         * If it is set to false then your layout will be inflated but won't be attached to any
         * other layout (so it won't be drawn, receive touch events etc).
         */
        final View fragmentView = inflater.inflate(R.layout.products_layout, container, false);
        return fragmentView;
    }

}
