package georgia.languagelandscape;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import georgia.languagelandscape.data.User;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_LOCATION = 1002;
    TextView content;

    RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        content = (TextView) findViewById(R.id.content);

        // ask for fine location access
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                EditText userText = (EditText) findViewById(R.id.nameText);
                EditText passwordText = (EditText) findViewById(R.id.passText);



                if (mRequestQueue == null) {
                    mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                }


                String name = userText.getText().toString();
                String pass = passwordText.getText().toString();

                User user = new User();

                user.setUsername(name);
                user.setPassword(pass);

                Map<String, String> map = new HashMap<>();

                map.put("username", user.getUsername());
                map.put("password", user.getPassword());

                JSONObject json = new JSONObject(map);


                sendMessage(json);
                Toast.makeText(MainActivity.this, "Username: "+ user.getUsername() + " Password: " + user.getPassword() , Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
//                myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.Frags.MAP);
                myIntent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_MAP);
                startActivity(myIntent);
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mainFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "what ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        TextView signup = (TextView) findViewById(R.id.signUp);
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                // intent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_MAP);
                startActivity(intent);
                finish();
            }
        });

        TextView forgotpass = (TextView) findViewById(R.id.forgotPass);
        forgotpass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, ForgotPassActivity.class);
                // intent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_MAP);
                startActivity(intent);
                finish();
            }
        });

    }

    public void sendMessage(JSONObject object){
        try{
            String url = "http://d2ddaedd.ngrok.io";
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
