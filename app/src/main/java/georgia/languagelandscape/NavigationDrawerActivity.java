package georgia.languagelandscape;

//import android.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.view.View;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnFragmentInteractionListener,
        MyProjectsFragment.OnFragmentInteractionListener ,
        NewProjectFragment.OnFragmentInteractionListener{

    MapFragment mapFragment= new MapFragment();
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(NavigationDrawerActivity.this, RecordingActivity.class);
                startActivity(myIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //me
        navigationView.setItemIconTintList(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_replace, mapFragment);
        ft.commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        FragmentTransaction ft = fm.beginTransaction();


        if (id == R.id.nav_feed) {
            // Handle the camera action
        } else if(id == R.id.nav_browse_the_map) {

            ft.replace(R.id.content_replace, mapFragment);

        } else if (id == R.id.nav_my_profile) {

            ProfileFragment profileFragment= new ProfileFragment();
            ft.replace(R.id.content_replace, profileFragment);

        } else if (id==R.id.nav_new_project) {

            NewProjectFragment newProjectFragment= new NewProjectFragment();
            ft.replace(R.id.content_replace, newProjectFragment);

        } else if (id == R.id.nav_my_projects) {

            MyProjectsFragment myProjectsFragment= new MyProjectsFragment();
            ft.replace(R.id.content_replace, myProjectsFragment);

        } else if (id == R.id.nav_seetings) {

        } else if (id == R.id.nav_log_out) {
            Intent myIntent = new Intent(NavigationDrawerActivity.this, MainActivity.class);
            startActivity(myIntent);
            finish();

        }

        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
