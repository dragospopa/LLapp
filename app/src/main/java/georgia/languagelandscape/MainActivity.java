/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import georgia.languagelandscape.fragments.ForgotPassFragment;
import georgia.languagelandscape.fragments.OnBoardFragment;
import georgia.languagelandscape.fragments.SignUpFragment;
import georgia.languagelandscape.fragments.TermsFragment;

/**
 * MainActivity deals with the onboarding page and all the interaction between
 * {@link OnBoardFragment}, {@link SignUpFragment} and {@link ForgotPassFragment}.
 *
 * It's also the activity where we check for permission to use location.
 */
public class MainActivity extends AppCompatActivity
        implements OnBoardFragment.OnBoardFragmentListener,
        SignUpFragment.SignUpFragmentListener, ForgotPassFragment.ForgotPassFragmentListener{

    public static final int REQUEST_LOCATION = 1002;
    TextView content;

    RequestQueue mRequestQueue;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new OnBoardFragment()).commit();
        // ask for fine location access
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    public void sendMessage(JSONObject object){
        try{
            String url = "http://7602f906.ngrok.io/";
            JsonObjectRequest req = new JsonObjectRequest(url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                VolleyLog.v("Response:%n %s", response.toString(4));
                                doSthWithResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });
            mRequestQueue.add(req);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void doSthWithResponse(JSONObject object){
        content.setText(object.toString());
    }

    @Override
    public void onLoginClick(JSONObject jsonObject) {
        sendMessage(jsonObject);
        Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
        myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_MAP);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onCreateAccountClick() {
        fm.beginTransaction()
                .replace(R.id.container, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onForgotPassClick() {
        fm.beginTransaction()
                .replace(R.id.container, new ForgotPassFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSignUpClick() {
        // TODO: should create a user here
        Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
        myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_MAP);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onLoginTextClick() {
        fm.beginTransaction()
                .replace(R.id.container, new OnBoardFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onTermsClick() {
        fm.beginTransaction()
                .replace(R.id.container, new TermsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSendClick() {
        // TODO: send the email
        fm.beginTransaction()
                .replace(R.id.container, new OnBoardFragment())
                .commit();
    }


    class MyRequest {
        String message;
        String username;
        String longitute;
        String latitude;

        public MyRequest(String message, String longitute, String latitude) {
            this.message = message;
            this.longitute = longitute;
            this.latitude = latitude;
            this.username = "user";
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                // TODO: do something if location access denied.
        }
    }
}
