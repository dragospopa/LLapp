package georgia.languagelandscape;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import georgia.languagelandscape.fragments.MapFragment;
import georgia.languagelandscape.fragments.MyProjectsFragment;
import georgia.languagelandscape.fragments.NewProjectFragment;
import georgia.languagelandscape.fragments.ProfileFragment;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnFragmentInteractionListener,
        MyProjectsFragment.OnFragmentInteractionListener ,
        NewProjectFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,
        HelpFragment.OnFragmentInteractionListener{

    private ActionBarDrawerToggle toggle = null;
    private DrawerLayout drawer = null;

    protected void onDrawerCreated() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
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
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Intent myIntent;


        switch (id) {
            case R.id.nav_log_out:
                myIntent = new Intent(BaseActivity.this, MainActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(myIntent);
                finish();
                break;
            case R.id.nav_browse_the_map:
                if (!MapActivity.isActive()) {
                    myIntent = new Intent(BaseActivity.this, MapActivity.class);
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(myIntent);
                    finish();
                } else {
                    MapFragment mapFragment = new MapFragment();
                    Bundle bundle = new Bundle();
                    bundle.putDouble(MapActivity.GEO_LONGITUDE, MapActivity.getLongitude());
                    bundle.putDouble(MapActivity.GEO_LATITUDE, MapActivity.getLatitude());
                    mapFragment.setArguments(bundle);
                    ft.replace(R.id.content_replace, mapFragment);
                }
                break;
            case R.id.nav_feed:
                // TODO: go to all recordings page
                break;
            case R.id.nav_my_profile:
                myIntent = new Intent(BaseActivity.this, MapActivity.class);
                myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_PROFILE);
                startActivity(myIntent);
                finish();
                break;
            case R.id.nav_new_project:
                myIntent = new Intent(BaseActivity.this, MapActivity.class);
                myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_NEW_PROJECT);
                startActivity(myIntent);
                finish();
                break;
            case R.id.nav_my_projects:
                myIntent = new Intent(BaseActivity.this, MapActivity.class);
                myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_MY_PROJECT);
                startActivity(myIntent);
                finish();
                break;
            case R.id.nav_seetings:
                // TODO: go to setting page
                break;
            case R.id.nav_my_recordings:
                myIntent = new Intent(BaseActivity.this, MyRecordingsActivity.class);
                startActivity(myIntent);
                finish();
                break;
            case R.id.nav_about:
                myIntent = new Intent(BaseActivity.this, MainActivity.class);
                myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_ABOUT);
                startActivity(myIntent);
                finish();
                break;
            case R.id.nav_help:
                myIntent = new Intent(BaseActivity.this, MainActivity.class);
                myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_HELP);
                startActivity(myIntent);
                finish();
                break;
        }

        ft.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}
