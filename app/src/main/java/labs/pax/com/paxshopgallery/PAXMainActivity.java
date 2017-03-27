package labs.pax.com.paxshopgallery;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import labs.pax.com.paxshopgallery.view.ProductsFragment;

public class PAXMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static String ARGS_IS_FAVORITES = "argsIsFavorites";
    private boolean isFavorites;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            // first time activity startup
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        } else {
            isFavorites = savedInstanceState.getBoolean(ARGS_IS_FAVORITES, false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ARGS_IS_FAVORITES, isFavorites);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        final TextView messageTextView = (TextView) findViewById(R.id.message);
        final int id = item.getItemId();

        if (id == R.id.nav_products) {
            messageTextView.setVisibility(View.INVISIBLE);
            showProducts(false);
        } else if (id == R.id.nav_favorites) {
            showProducts(true);
            messageTextView.setVisibility(View.INVISIBLE);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void showProducts(final boolean isFavorites) {
        this.isFavorites = isFavorites;
        final String productsFragmentTag = ProductsFragment.class.getSimpleName();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final ProductsFragment productsFragment = (ProductsFragment) fragmentManager.findFragmentByTag(productsFragmentTag);
        if (productsFragment != null) {
            // we want to remove old fragment to refresh the data properly
            getSupportFragmentManager().beginTransaction().remove(productsFragment).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, ProductsFragment.newInstance(isFavorites), productsFragmentTag).commit();
    }
}
