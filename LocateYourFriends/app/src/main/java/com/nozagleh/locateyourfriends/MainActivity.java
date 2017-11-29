package com.nozagleh.locateyourfriends;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nozagleh.locateyourfriends.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentListener {

    // Activity tag
    private static final String TAG = "MainActivity";
    // Fragment tags
    private static final String FRAG_TAG_MAP = "FRAG_MAP";
    private static final String FRAG_TAG_LIST = "FRAG_LIST";
    private static final String FRAG_TAG_LIST_GROUP = "FRAG_GROUP_LIST";
    private static final String FRAG_TAG_ADD_GROUP = "FRAG_GROUP_ADD";
    private static final String FRAG_TAG_CREATE_GROUP = "FRAG_GROUP_CREATE";

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Sets the map view to default
        navigationView.getMenu().getItem(0).setChecked(true);

        if(!LocalDataManager.hasUserId(this)) {
            Log.w(TAG, "no uid registered");
        }

        FragmentManager fm = getSupportFragmentManager();
        fragment = new MapFragment();

        startFragment(fragment, FRAG_TAG_MAP);

        if (!PermissionManager.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionManager.askPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            NotificationManager.notifyNoGPS(
                    this.findViewById(android.R.id.content),
                    getString(R.string.notification_no_gps));
        }

        if (!NetworkManager.hasInternetConnection(this)) {
            NotificationManager.notifyNoInternet(
                    this.findViewById(android.R.id.content),
                    getString(R.string.notification_no_internet));
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        fragment = null;
        String fragTag = "NO_FRAG";

        if (id == R.id.nav_map) {
            fragment = new MapFragment();
            fragTag = FRAG_TAG_MAP;
        } else if (id == R.id.nav_list) {
            fragment = new PersonFragment();
            fragTag = FRAG_TAG_LIST;
        } else if (id == R.id.nav_groups) {
            fragment = new GroupListFragment();
            fragTag = FRAG_TAG_LIST_GROUP;
        } else if (id == R.id.nav_add_group) {
            fragment = new AddGroupFragment();
            fragTag = FRAG_TAG_ADD_GROUP;
        } else if (id == R.id.nav_create_group) {
            fragment = new CreateGroupFragment();
            fragTag = FRAG_TAG_CREATE_GROUP;
        } else if (id == R.id.nav_settings) {
            // TODO add settings
        }

        startFragment(fragment, fragTag);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.askPermissionResults(requestCode, permissions, grantResults);
    }

    private void startFragment(Fragment fragment, String fragTag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFrag = getSupportFragmentManager().findFragmentByTag(fragTag);

        if (fragment != null && currentFrag != fragment) {
            fm.beginTransaction()
                    .replace(R.id.fragContainer, fragment, FRAG_TAG_MAP)
                    .commit();
        }
    }
}
