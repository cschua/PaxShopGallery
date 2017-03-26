package labs.pax.com.paxshopgallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import labs.pax.com.paxshopgallery.PaxProducts.Product;
import com.bluelinelabs.logansquare.LoganSquare;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PAXMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        final TextView messageTextView = (TextView) findViewById(R.id.message);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final int id = item.getItemId();

        if (id == R.id.nav_products) {
            messageTextView.setVisibility(View.INVISIBLE);
            final String productsFragmentTag = ProductsFragment.class.getSimpleName();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, ProductsFragment.newInstance(), productsFragmentTag).commit();
        } else if (id == R.id.nav_favorites) {
            messageTextView.setVisibility(View.INVISIBLE);
            final String favoritesFragmentTag = FavoritesFragment.class.getSimpleName();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, FavoritesFragment.newInstance("Favorites"), favoritesFragmentTag).commit();

        } else {
            messageTextView.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
